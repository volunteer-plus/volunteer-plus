package volunteer.plus.backend.service;

import volunteer.plus.backend.domain.dto.BrigadeCreationRequestDTO;
import volunteer.plus.backend.domain.dto.BrigadeDTO;

import java.util.List;
import java.util.Set;

public interface BrigadeService {
    List<BrigadeDTO> getBrigades(Set<Long> ids);

    List<BrigadeDTO> createOrUpdate(BrigadeCreationRequestDTO creationRequestDTO);

    void deleteAll(Set<Long> ids);
}
