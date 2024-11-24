package volunteer.plus.backend.service.liqpay;

import com.fasterxml.jackson.core.JsonProcessingException;
import volunteer.plus.backend.domain.dto.LiqPayCreationDTO;
import volunteer.plus.backend.domain.dto.LiqPayResponseDTO;

import java.util.Map;

public interface LiqPayApi {
    String API_VERSION = "3";

    LiqPayResponseDTO createLiqPayPayload(LiqPayCreationDTO liqPayCreationDTO) throws JsonProcessingException;
}
