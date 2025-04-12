package volunteer.plus.backend.service.liqpay.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.LiqPayCreationDTO;
import volunteer.plus.backend.domain.dto.LiqPayResponseDTO;
import volunteer.plus.backend.domain.entity.Levy;
import volunteer.plus.backend.domain.entity.Request;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.enums.CurrencyName;
import volunteer.plus.backend.domain.enums.LevyStatus;
import volunteer.plus.backend.domain.enums.RequestStatus;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.LevyRepository;
import volunteer.plus.backend.repository.RequestRepository;
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
    private final LevyRepository levyRepository;
    private final RequestRepository requestRepository;

    public LiqPayService(final EmailNotificationBuilderService emailNotificationBuilderService,
                         final LevyRepository levyRepository,
                         final RequestRepository requestRepository) {
        this.emailNotificationBuilderService = emailNotificationBuilderService;
        this.levyRepository = levyRepository;
        this.requestRepository = requestRepository;
    }


    @Override
    @Transactional
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

        if (liqPayCreationDTO.getLevyId() != null) {
            handleLevy(liqPayCreationDTO);
        }

        return LiqPayResponseDTO.builder()
                .data(data)
                .signature(signature)
                .build();
    }

    private void handleLevy(final LiqPayCreationDTO liqPayCreationDTO) {
        final Levy levy = levyRepository.findById(liqPayCreationDTO.getLevyId())
                .orElseThrow(() -> new ApiException(ErrorCode.LEVY_NOT_FOUND));

        levy.setAccumulated(
                levy.getAccumulated() == null ?
                        liqPayCreationDTO.getAmount() :
                        levy.getAccumulated().add(liqPayCreationDTO.getAmount())
        );

        if (levy.getAccumulated().compareTo(levy.getGoalAmount()) >= 0) {

            levy.setStatus(LevyStatus.FINISHED);
            final Request request = levy.getRequest();

            // check levies statuses in request
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

    protected String getBtnTxt(String lang) {
        return switch (lang) {
            case "uk" -> "Сплатити";
            case "en" -> "Pay";
            default -> "Сплатити";
        };
    }
}
