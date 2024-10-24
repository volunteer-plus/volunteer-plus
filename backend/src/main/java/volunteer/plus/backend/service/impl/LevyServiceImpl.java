package volunteer.plus.backend.service.impl;

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
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.LevyRepository;
import volunteer.plus.backend.repository.VolunteerRepository;
import volunteer.plus.backend.service.LevyService;

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
                .map(levy ->
                        LevyDTO.builder()
                                .id(levy.getId())
                                .createDate(levy.getCreateDate())
                                .updateDate(levy.getUpdateDate())
                                .accumulated(levy.getAccumulated())
                                .trophyDescription(levy.getTrophyDescription())
                                .build())
                .toList();
    }

    private void handleLevyDTOS(LevyCreationRequestDTO levyCreationRequestDTO, List<Volunteer> volunteers, Map<Long, Levy> leviesMapFromDB, List<Levy> levies) {
        levyCreationRequestDTO.getLevies().forEach(levyDTO -> {
            final Levy levy;
            if (levyDTO.getId() == null) {
                levy = Levy.builder()
                        .accumulated(levyDTO.getAccumulated())
                        .trophyDescription(levyDTO.getTrophyDescription())
                        .volunteers(new ArrayList<>())
                        .build();

                final var vols = volunteers.stream()
                        .filter(v -> levyDTO.getVolunteerIds() != null && levyDTO.getVolunteerIds().contains(v.getId()))
                        .collect(Collectors.toSet());

                vols.forEach(levy::addVolunteer);
            } else {
                levy = leviesMapFromDB.get(levyDTO.getId());
                levy.setTrophyDescription(levyDTO.getTrophyDescription());
                levy.setAccumulated(levyDTO.getAccumulated());

                final var vols = volunteers.stream()
                        .filter(v -> levyDTO.getVolunteerIds() != null && levy.getVolunteers().stream().noneMatch(el -> Objects.equals(el.getId(), v.getId())))
                        .collect(Collectors.toSet());

                vols.forEach(levy::addVolunteer);
            }
            levies.add(levy);
        });
    }
}
