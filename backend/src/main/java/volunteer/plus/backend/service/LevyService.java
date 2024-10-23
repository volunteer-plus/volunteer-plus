package volunteer.plus.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import volunteer.plus.backend.domain.dto.LevyCreationRequestDTO;
import volunteer.plus.backend.domain.dto.LevyDTO;

import java.util.List;
import java.util.Set;


public interface LevyService {
    Page<LevyDTO> getLevies(Pageable of, Set<Long> requestIds);

    List<LevyDTO> createOrUpdateLevies(LevyCreationRequestDTO levyCreationRequestDTO);
}
