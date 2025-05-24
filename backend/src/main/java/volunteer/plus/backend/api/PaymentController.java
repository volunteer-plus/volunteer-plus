package volunteer.plus.backend.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.MonobankInvoiceResponseDTO;
import volunteer.plus.backend.domain.dto.MonobankInvoiceStatusDTO;
import volunteer.plus.backend.domain.dto.PaymentCreationDTO;
import volunteer.plus.backend.domain.entity.PaymentOrder;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.enums.PaymentStatus;
import volunteer.plus.backend.repository.PaymentOrderRepository;
import volunteer.plus.backend.service.payment.impl.LiqPayService;
import volunteer.plus.backend.service.payment.impl.MonobankService;

import java.util.Map;

@Validated
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final LiqPayService liqPayService;
    private final PaymentOrderRepository orderRepository;
    private final MonobankService monobankService;

    @PostMapping(value = "/liq-pay/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    @Operation(description = "Create liq pay payload")
    @ResponseBody
    public String createLiqPayPayload(@AuthenticationPrincipal final User user,
                                                                 @RequestBody @Valid final PaymentCreationDTO paymentCreationDTO,
                                                                 Model model) throws JsonProcessingException {
        var payload = liqPayService.createLiqPayPayload(user, paymentCreationDTO);
        model.addAttribute("data", payload.getData());
        model.addAttribute("signature", payload.getSignature());
        return "liqpay-redirect";
    }

    @PostMapping("/no-auth/liqpay/callback")
    @ResponseBody
    public ResponseEntity<String> callback(@RequestParam("data") String data,
                                           @RequestParam("signature") String signature) throws Exception {

        if (!liqPayService.validateSignature(data, signature)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid signature");
        }

        Map<String, Object> payload = liqPayService.parseCallbackData(data);
        String orderId = payload.get("order_id").toString();
        String liqStatus = payload.get("status").toString();

        PaymentOrder order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

        PaymentStatus newStatus = switch (liqStatus.toLowerCase()) {
            case "success"  -> PaymentStatus.SUCCESS;
            case "failure", "error" -> PaymentStatus.FAILED;
            default          -> PaymentStatus.PENDING;
        };

        order.setStatus(newStatus);
        orderRepository.save(order);

        return ResponseEntity.ok("OK");
    }

    @PostMapping(value = "/create-invoice",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, String>> createInvoice(@AuthenticationPrincipal User user,
                                @RequestBody @Valid PaymentCreationDTO dto,
                                Model model) {
        MonobankInvoiceResponseDTO resp = monobankService.createInvoice(dto, user);
        model.addAttribute("payUrl", resp.getPayUrl());
        return  ResponseEntity.ok(Map.of("payUrl", resp.getPayUrl()));
    }

    // Обробка редіректу, оновлення статусу та показ успіху
    @PostMapping("/no-auth/monobank/success")
    @Transactional
    @ResponseBody
    public ResponseEntity<String> success(@RequestBody MonobankInvoiceStatusDTO status) {
        String invoiceId = status.getInvoiceId();
        PaymentOrder order = orderRepository.findByOrderId(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + invoiceId));
        PaymentStatus paymentStatus = mapMonobankStatus(status.getStatus());
        order.setStatus(paymentStatus);
        orderRepository.save(order);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    private PaymentStatus mapMonobankStatus(String mbStatus) {
        return switch (mbStatus.toLowerCase()) {
            case "created", "processing", "hold"  -> PaymentStatus.PENDING;
            case "success"                        -> PaymentStatus.SUCCESS;
            case "failure"                        -> PaymentStatus.FAILED;
            case "reversed", "expired"            -> PaymentStatus.CANCELLED;
            default                               -> throw new IllegalArgumentException(
                    "Unknown Monobank status: " + mbStatus);
        };
    }
}
