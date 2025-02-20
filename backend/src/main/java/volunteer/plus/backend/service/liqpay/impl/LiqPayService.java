package volunteer.plus.backend.service.liqpay.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.LiqPayCreationDTO;
import volunteer.plus.backend.domain.dto.LiqPayResponseDTO;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.enums.CurrencyName;
import volunteer.plus.backend.service.email.EmailNotificationBuilderService;
import volunteer.plus.backend.service.liqpay.LiqPayApi;

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

    public LiqPayService(final EmailNotificationBuilderService emailNotificationBuilderService) {
        this.emailNotificationBuilderService = emailNotificationBuilderService;
    }


    @Override
    public LiqPayResponseDTO createLiqPayPayload(final User user,
                                                 final LiqPayCreationDTO liqPayCreationDTO) throws JsonProcessingException {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("amount", liqPayCreationDTO.getAmount().toString());

        checkCnbParams(params);
        var liqPayParamPayload = withSandboxParam(withBasicApiParams(params));
        String data = base64_encode(objectMapper.writeValueAsString(liqPayParamPayload));
        String signature = createSignature(data);

        if (user != null) {
            emailNotificationBuilderService.createUserPaymentEmail(liqPayCreationDTO, user);
        }

        return LiqPayResponseDTO.builder()
                .data(data)
                .signature(signature)
                .build();
    }

    protected void checkCnbParams(Map<String, String> params) {
        if (params.get("amount") == null)
            throw new NullPointerException("amount can't be null");
    }

    protected String createSignature(String base64EncodedData) {
        return str_to_sign(privateKey + base64EncodedData + privateKey);
    }

    protected String str_to_sign(String str) {
        return base64_encode(sha1(str));
    }

    protected TreeMap<String, String> withBasicApiParams(Map<String, String> params) {
        TreeMap<String, String> tm = new TreeMap<>(params);
        tm.put("public_key", publicKey);
        tm.put("version", API_VERSION);
        tm.put("action", "pay");
        tm.put("currency", CurrencyName.UAH.name());
        tm.put("order_id", UUID.randomUUID().toString());

        return tm;
    }

    private void checkRequired() {
        if (this.publicKey == null || this.publicKey.isEmpty()) {
            throw new IllegalArgumentException("publicKey is empty");
        }
        if (this.privateKey == null || this.privateKey.isEmpty()) {
            throw new IllegalArgumentException("privateKey is empty");
        }
    }

    protected TreeMap<String, String> withSandboxParam(TreeMap<String, String> params) {
        if (params.get("sandbox") == null && isCnbSandbox()) {
            TreeMap<String, String> tm = new TreeMap<>(params);
            tm.put("sandbox", "1");
            return tm;
        }
        return params;
    }

    protected String getBtnTxt(String lang) {
        return switch (lang) {
            case "uk" -> "Сплатити";
            case "en" -> "Pay";
            default -> "Сплатити";
        };
    }
}
