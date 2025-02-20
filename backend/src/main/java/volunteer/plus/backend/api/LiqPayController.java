package volunteer.plus.backend.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.LiqPayCreationDTO;
import volunteer.plus.backend.domain.dto.LiqPayResponseDTO;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.service.liqpay.impl.LiqPayService;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LiqPayController {
    private final LiqPayService liqPayService;

    @PostMapping("/liq-pay/create")
    @Operation(description = "Create liq pay payload")
    public ResponseEntity<LiqPayResponseDTO> createLiqPayPayload(@AuthenticationPrincipal final User user,
                                                                 @RequestBody @Valid final LiqPayCreationDTO liqPayCreationDTO) throws JsonProcessingException {
        return ResponseEntity.ok(liqPayService.createLiqPayPayload(user, liqPayCreationDTO));
    }
}
