package volunteer.plus.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import volunteer.plus.backend.domain.dto.RequestCreationRequestDTO;
import volunteer.plus.backend.domain.dto.RequestDTO;

import java.util.List;

public interface RequestService {
    Page<RequestDTO> getRequests(Pageable pageable);

    List<RequestDTO> createOrUpdateRequests(String userEmail, Long militaryPersonnelId, RequestCreationRequestDTO requestCreationRequestDTO);
}
