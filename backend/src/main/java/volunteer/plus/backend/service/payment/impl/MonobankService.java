package volunteer.plus.backend.service.payment.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import volunteer.plus.backend.domain.dto.MonobankCreateInvoiceDTO;
import volunteer.plus.backend.domain.dto.MonobankInvoiceResponseDTO;
import volunteer.plus.backend.domain.dto.PaymentCreationDTO;
import volunteer.plus.backend.domain.entity.Levy;
import volunteer.plus.backend.domain.entity.PaymentOrder;
import volunteer.plus.backend.domain.entity.Request;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.enums.LevyStatus;
import volunteer.plus.backend.domain.enums.PaymentProvider;
import volunteer.plus.backend.domain.enums.PaymentStatus;
import volunteer.plus.backend.domain.enums.RequestStatus;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.LevyRepository;
import volunteer.plus.backend.repository.PaymentOrderRepository;
import volunteer.plus.backend.repository.RequestRepository;

@Service
@RequiredArgsConstructor
public class MonobankService {
    private final RestTemplate rest = new RestTemplate();

    @Value("${monobank.token}")
    private String token;
    @Value("${monobank.api.url}")
    private String apiUrl;

    private final PaymentOrderRepository orderRepository;
    private final LevyRepository levyRepository;
    private final RequestRepository requestRepository;

    public MonobankInvoiceResponseDTO createInvoice(PaymentCreationDTO dto, User user) {
        String url = apiUrl + "/api/merchant/invoice/create";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        MonobankCreateInvoiceDTO monobankCreateInvoiceDTO = new MonobankCreateInvoiceDTO();
        monobankCreateInvoiceDTO.setAmount(dto.getAmount());
        monobankCreateInvoiceDTO.setCcy(dto.getCurrency().getNumericCode());
        monobankCreateInvoiceDTO.setWebHookUrl("https://7cff-91-202-130-93.ngrok-free.app/api/no-auth/monobank/success");

        HttpEntity<MonobankCreateInvoiceDTO> request = new HttpEntity<>(monobankCreateInvoiceDTO, headers);

        if (dto.getLevyId() != null) {
            handleLevy(dto);
        }

        MonobankInvoiceResponseDTO monobankInvoiceResponseDTO = rest.postForObject(url, request, MonobankInvoiceResponseDTO.class);

        assert monobankInvoiceResponseDTO != null;
        PaymentOrder order = PaymentOrder.builder()
                .currencyName(dto.getCurrency())
                .amount(dto.getAmount())
                .orderId(monobankInvoiceResponseDTO.getInvoiceId())
                .user(user)
                .provider(PaymentProvider.MONOBANK)
                .status(PaymentStatus.PENDING)
                .build();

        orderRepository.save(order);

        return monobankInvoiceResponseDTO;
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


}
