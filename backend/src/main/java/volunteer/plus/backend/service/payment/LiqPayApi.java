package volunteer.plus.backend.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import volunteer.plus.backend.domain.dto.PaymentCreationDTO;
import volunteer.plus.backend.domain.dto.LiqPayResponseDTO;
import volunteer.plus.backend.domain.entity.User;

public interface LiqPayApi {
    String API_VERSION = "3";

    LiqPayResponseDTO createLiqPayPayload(User user, PaymentCreationDTO paymentCreationDTO) throws JsonProcessingException;
}
