package volunteer.plus.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.LevyCreationRequestDTO;
import volunteer.plus.backend.domain.dto.LevyDTO;
import volunteer.plus.backend.domain.entity.Levy;
import volunteer.plus.backend.repository.LevyRepository;
import volunteer.plus.backend.service.LevyService;

import java.util.List;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class LevyServiceImpl implements LevyService {
    private final LevyRepository levyRepository;

    @Override
    public Page<LevyDTO> getLevies(final Pageable pageable, final Set<Long> requestIds) {
        log.info("Retrieve all levies by page: {}, and size: {}", pageable.getPageNumber(), pageable.getPageSize());

        final Page<Levy> levies;
        if (requestIds == null) {
            levies = levyRepository.findAll(pageable);
        } else {
            levies = levyRepository.findAllByRequestIdIn(requestIds, pageable);
        }

        return levies
                .map(levy ->
                        LevyDTO.builder()
                                .id(levy.getId())
                                .createDate(levy.getCreateDate())
                                .updateDate(levy.getUpdateDate())
                                .accumulated(levy.getAccumulated())
                                .trophyDescription(levy.getTrophyDescription())
                                .build());
    }

    @Override
    @Transactional
    public List<LevyDTO> createOrUpdateLevies(final LevyCreationRequestDTO levyCreationRequestDTO) {

        // TODO implement for

        return List.of();
    }
}
