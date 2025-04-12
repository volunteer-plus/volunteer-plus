package volunteer.plus.backend.service.general.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.LevyCreationDTO;
import volunteer.plus.backend.domain.dto.LevyCreationRequestDTO;
import volunteer.plus.backend.domain.dto.LevyDTO;
import volunteer.plus.backend.domain.entity.Levy;
import volunteer.plus.backend.domain.entity.Volunteer;
import volunteer.plus.backend.domain.enums.LevyStatus;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.LevyRepository;
import volunteer.plus.backend.repository.VolunteerRepository;
import volunteer.plus.backend.service.general.LevyService;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class LevyServiceImpl implements LevyService {
    private final LevyRepository levyRepository;
    private final VolunteerRepository volunteerRepository;

    @Override
    public Page<LevyDTO> getLevies(final Pageable pageable,
                                   final Set<Long> requestIds) {
        log.info("Retrieve all levies by page: {}, and size: {}", pageable.getPageNumber(), pageable.getPageSize());

        final Page<Levy> levies;
        if (requestIds == null) {
            levies = levyRepository.findAll(pageable);
        } else {
            levies = levyRepository.findAllByRequestIdIn(requestIds, pageable);
        }

        return levies.map(this::getLevyDTO);
    }

    @Override
    @Transactional
    public List<LevyDTO> createOrUpdateLevies(final LevyCreationRequestDTO levyCreationRequestDTO) {
        if (levyCreationRequestDTO.getLevies() == null || levyCreationRequestDTO.getLevies().isEmpty()) {
            throw new ApiException(ErrorCode.LEVY_NOT_FOUND);
        }

        final var volunteerIds = levyCreationRequestDTO.getLevies()
                .stream()
                .map(LevyCreationDTO::getVolunteerIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        final var volunteers = volunteerRepository.findAllById(volunteerIds);

        if (volunteerIds.size() != volunteers.size()) {
            throw new ApiException(ErrorCode.VOLUNTEER_NOT_FOUND);
        }

        final var levyIds = levyCreationRequestDTO.getLevies()
                .stream()
                .map(LevyCreationDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        final var leviesMapFromDB = levyRepository.findAllById(levyIds)
                .stream()
                .collect(Collectors.toMap(Levy::getId, Function.identity()));

        if (leviesMapFromDB.size() != levyIds.size()) {
            throw new ApiException(ErrorCode.LEVY_NOT_FOUND);
        }

        final List<Levy> levies = new ArrayList<>();

        handleLevyDTOS(levyCreationRequestDTO, volunteers, leviesMapFromDB, levies);

        return levyRepository.saveAll(levies)
                .stream()
                .map(this::getLevyDTO)
                .toList();
    }

    private LevyDTO getLevyDTO(final Levy levy) {
        return LevyDTO.builder()
                .id(levy.getId())
                .createDate(levy.getCreateDate())
                .updateDate(levy.getUpdateDate())
                .accumulated(levy.getAccumulated())
                .trophyDescription(levy.getTrophyDescription())
                .description(levy.getDescription())
                .goalAmount(levy.getGoalAmount())
                .category(levy.getCategory())
                .status(levy.getStatus())
                .build();
    }

    private void handleLevyDTOS(final LevyCreationRequestDTO levyCreationRequestDTO,
                                final List<Volunteer> volunteers,
                                final Map<Long, Levy> leviesMapFromDB,
                                final List<Levy> levies) {
        levyCreationRequestDTO.getLevies().forEach(levyDTO -> {
            final Levy levy;
            if (levyDTO.getId() == null) {
                levy = Levy.builder()
                        .accumulated(BigDecimal.ZERO)
                        .goalAmount(levyDTO.getGoalAmount())
                        .trophyDescription(levyDTO.getTrophyDescription())
                        .description(levyDTO.getDescription())
                        .category(levyDTO.getCategory())
                        .status(LevyStatus.IN_PROGRESS)
                        .volunteers(new ArrayList<>())
                        .build();

                final var vols = volunteers.stream()
                        .filter(v -> levyDTO.getVolunteerIds() != null && levyDTO.getVolunteerIds().contains(v.getId()))
                        .collect(Collectors.toSet());

                vols.forEach(levy::addVolunteer);
            } else {
                // we don't need to update status
                levy = leviesMapFromDB.get(levyDTO.getId());
                levy.setTrophyDescription(levyDTO.getTrophyDescription());
                levy.setAccumulated(levyDTO.getAccumulated());
                levy.setGoalAmount(levyDTO.getGoalAmount());
                levy.setDescription(levyDTO.getDescription());
                levy.setCategory(levyDTO.getCategory());

                final var vols = volunteers.stream()
                        .filter(v -> levyDTO.getVolunteerIds() != null && levy.getVolunteers().stream().noneMatch(el -> Objects.equals(el.getId(), v.getId())))
                        .collect(Collectors.toSet());

                vols.forEach(levy::addVolunteer);
            }
            levies.add(levy);
        });
    }
}
