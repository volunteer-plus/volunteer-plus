package volunteer.plus.backend.service.general;

import volunteer.plus.backend.domain.dto.AddRequestResponseDTO;

import java.util.List;
import java.util.Set;

public interface AddRequestService {
    List<AddRequestResponseDTO> getRequests();

    List<AddRequestResponseDTO> generate(String regimentCode, Long amount);

    void validateCode(String code);

    void deleteAll(Set<Long> ids);
}
