package volunteer.plus.backend.service.payment.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.PaymentCreationDTO;
import volunteer.plus.backend.domain.dto.LiqPayResponseDTO;
import volunteer.plus.backend.domain.entity.Levy;
import volunteer.plus.backend.domain.entity.PaymentOrder;
import volunteer.plus.backend.domain.entity.Request;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.enums.*;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.LevyRepository;
import volunteer.plus.backend.repository.PaymentOrderRepository;
import volunteer.plus.backend.repository.RequestRepository;
import volunteer.plus.backend.service.email.EmailNotificationBuilderService;
import volunteer.plus.backend.service.payment.LiqPayApi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import static volunteer.plus.backend.util.LiqPayUtil.base64_encode;
import static volunteer.plus.backend.util.LiqPayUtil.sha1;

@Service
public class LiqPayService implements LiqPayApi {

    @Value("${LIQPAY_PUBLIC_KEY}")
    private String publicKey;
    @Value("${LIQPAY_PRIVATE_KEY}")
    private String privateKey;
    @Getter
    private boolean cnbSandbox;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final EmailNotificationBuilderService emailNotificationBuilderService;
    private final LevyRepository levyRepository;
    private final RequestRepository requestRepository;
    private final PaymentOrderRepository orderRepository;

    public LiqPayService(final EmailNotificationBuilderService emailNotificationBuilderService,
                         final LevyRepository levyRepository,
                         final RequestRepository requestRepository, PaymentOrderRepository orderRepository) {
        this.emailNotificationBuilderService = emailNotificationBuilderService;
        this.levyRepository = levyRepository;
        this.requestRepository = requestRepository;
        this.orderRepository = orderRepository;
    }


    @Override
    @Transactional
    public LiqPayResponseDTO createLiqPayPayload(final User user,
                                                 final PaymentCreationDTO paymentCreationDTO) throws JsonProcessingException {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("amount", paymentCreationDTO.getAmount().toString());

        checkCnbParams(params);
        var liqPayParamPayload = withSandboxParam(withBasicApiParams(params));
        String data = base64_encode(objectMapper.writeValueAsString(liqPayParamPayload));
        String signature = createSignature(data);

        if (user != null) {
            emailNotificationBuilderService.createUserPaymentEmail(paymentCreationDTO, user);
        }

        if (paymentCreationDTO.getLevyId() != null) {
            handleLevy(paymentCreationDTO);
        }

        PaymentOrder order = PaymentOrder.builder()
                .currencyName(paymentCreationDTO.getCurrency())
                .amount(paymentCreationDTO.getAmount())
                .orderId(liqPayParamPayload.get("order_id"))
                .user(user)
                .provider(PaymentProvider.LIQPAY)
                .status(PaymentStatus.PENDING)
                .build();

        orderRepository.save(order);

        return LiqPayResponseDTO.builder()
                .data(data)
                .signature(signature)
                .build();
    }

    private void handleLevy(final PaymentCreationDTO paymentCreationDTO) {
        final Levy levy = levyRepository.findById(paymentCreationDTO.getLevyId())
                .orElseThrow(() -> new ApiException(ErrorCode.LEVY_NOT_FOUND));

        levy.setAccumulated(
                levy.getAccumulated() == null ?
                        paymentCreationDTO.getAmount() :
                        levy.getAccumulated().add(paymentCreationDTO.getAmount())
        );

        if (levy.getAccumulated().compareTo(levy.getGoalAmount()) >= 0) {

            levy.setStatus(LevyStatus.FINISHED);
            final Request request = levy.getRequest();

            final boolean allRequestLeviesCompleted = request.getLevies()
                    .stream()
                    .allMatch(el -> el.getStatus() == LevyStatus.FINISHED || el.getStatus() == LevyStatus.REPORT_PRESENT);

            if (allRequestLeviesCompleted) {
                request.setStatus(RequestStatus.DONE);
                requestRepository.save(request);
            }
        }

        levyRepository.save(levy);
    }

    protected void checkCnbParams(Map<String, String> params) {
        if (params.get("amount") == null)
            throw new NullPointerException("amount can't be null");
    }

    protected String createSignature(String base64EncodedData) {
        return stringToSign(privateKey + base64EncodedData + privateKey);
    }

    protected String stringToSign(String str) {
        return base64_encode(sha1(str));
    }

    protected TreeMap<String, String> withBasicApiParams(Map<String, String> params) {
        TreeMap<String, String> tm = new TreeMap<>(params);
        tm.put("public_key", publicKey);
        tm.put("version", API_VERSION);
        tm.put("action", "pay");
        tm.put("currency", CurrencyName.UAH.name());
        tm.put("order_id", UUID.randomUUID().toString());
        tm.put("result_url", " https://7cff-91-202-130-93.ngrok-free.app/api/payment/success");
        tm.put("server_url", " https://7cff-91-202-130-93.ngrok-free.app/api/no-auth/liqpay/callback");

        return tm;
    }

    protected TreeMap<String, String> withSandboxParam(TreeMap<String, String> params) {
        if (params.get("sandbox") == null && isCnbSandbox()) {
            TreeMap<String, String> tm = new TreeMap<>(params);
            tm.put("sandbox", "1");
            return tm;
        }
        return params;
    }

    public boolean validateSignature(String data, String signature) throws Exception {
        String expected = generateSignature(data);
        return expected.equals(signature);
    }

    public String generateSignature(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest((privateKey + data + privateKey).getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(digest);
    }

    public Map<String, Object> parseCallbackData(String data) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(data);
        String json = new String(decoded, StandardCharsets.UTF_8);
        return objectMapper.readValue(json, new TypeReference<>(){});
    }

}
