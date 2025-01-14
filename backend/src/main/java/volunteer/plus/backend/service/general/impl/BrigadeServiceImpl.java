package volunteer.plus.backend.service.general.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.entity.Brigade;
import volunteer.plus.backend.domain.dto.BrigadeCreationDTO;
import volunteer.plus.backend.domain.dto.BrigadeCreationRequestDTO;
import volunteer.plus.backend.domain.dto.BrigadeDTO;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.BrigadeRepository;
import volunteer.plus.backend.service.general.BrigadeCodesService;
import volunteer.plus.backend.service.general.BrigadeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static volunteer.plus.backend.domain.dto.BrigadeDTO.MilitaryPersonnelDTO.mapMilitaryPersonnel;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrigadeServiceImpl implements BrigadeService {
    private final BrigadeRepository brigadeRepository;
    private final BrigadeCodesService brigadeCodesService;

    @Override
    public List<BrigadeDTO> getBrigades(Set<Long> ids) {
        log.info("Retrieve brigades data");
        final List<Brigade> brigades;

        if (ids == null) {
            brigades = brigadeRepository.findAll();
        } else {
            brigades = brigadeRepository.findAllById(ids);
        }

        return brigades.stream()
                .map(this::mapBrigade)
                .toList();
    }

    @Override
    public BrigadeDTO getBrigade(final String name) {
        log.info("Retrieve brigade by name");

        final Brigade brigade = brigadeRepository.findByNameIgnoreCase(name).orElseGet(Brigade::new);

        return mapBrigade(brigade);
    }

    @Override
    @Transactional
    public List<BrigadeDTO> createOrUpdate(final BrigadeCreationRequestDTO creationRequestDTO) {
        if (creationRequestDTO == null || creationRequestDTO.getBrigades() == null || creationRequestDTO.getBrigades().isEmpty() ||
                creationRequestDTO.getBrigades().stream().anyMatch(b -> b.getRegimentCode() == null)) {
            throw new ApiException(ErrorCode.EMPTY_BRIGADE_DATA);
        }

        final var validRegimentCodes = brigadeCodesService.getCodes();
        final var brigades = creationRequestDTO.getBrigades();
        final var regimentCodes = brigades.stream().map(BrigadeCreationDTO::getRegimentCode).collect(Collectors.toSet());

        if (regimentCodes.stream().anyMatch(code -> !validRegimentCodes.contains(code))) {
            throw new ApiException(ErrorCode.NOT_VALID_REGIMENT_CODE);
        }

        final Map<String, Brigade> mapOfBrigades = brigadeRepository.findAllByRegimentCodeIn(regimentCodes).stream()
                .collect(Collectors.toMap(Brigade::getRegimentCode, Function.identity()));

        final List<Brigade> brigadesToPersist = new ArrayList<>();

        brigades.forEach(brigade -> {
           final var brigadeFromDB = mapOfBrigades.get(brigade.getRegimentCode());

           // update brigade
           if (brigadeFromDB != null) {
               brigadeFromDB.setName(brigade.getName());
               brigadeFromDB.setBranch(brigade.getBranch());
               brigadeFromDB.setRole(brigade.getRole());
               brigadeFromDB.setPartOf(brigade.getPartOf());
               brigadeFromDB.setWebsiteLink(brigade.getWebsiteLink());
               brigadeFromDB.setCurrentCommander(brigade.getCurrentCommander());
               brigadeFromDB.setDescription(brigade.getDescription());
               brigadesToPersist.add(brigadeFromDB);
           } else {
               // create a brigade
               final var newBrigade = Brigade.builder()
                       .regimentCode(brigade.getRegimentCode())
                       .name(brigade.getName())
                       .branch(brigade.getBranch())
                       .role(brigade.getRole())
                       .partOf(brigade.getPartOf())
                       .websiteLink(brigade.getWebsiteLink())
                       .currentCommander(brigade.getCurrentCommander())
                       .description(brigade.getDescription())
                       .militaryPersonnel(new ArrayList<>())
                       .build();
               brigadesToPersist.add(newBrigade);
           }
        });

        final List<Brigade> savedBrigades = brigadeRepository.saveAll(brigadesToPersist);

        return savedBrigades.stream()
                .map(this::mapBrigade)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAll(final Set<Long> ids) {
        final List<Brigade> brigades = brigadeRepository.findAllById(ids);

        if (brigades.size() != ids.size()) {
            throw new ApiException(ErrorCode.BRIGADE_NOT_FOUND);
        }

        brigadeRepository.deleteAll(brigades);
    }

    private BrigadeDTO mapBrigade(final Brigade brigade) {
        return BrigadeDTO.builder()
                .id(brigade.getId())
                .createDate(brigade.getCreateDate())
                .updateDate(brigade.getUpdateDate())
                .regimentCode(brigade.getRegimentCode())
                .name(brigade.getName())
                .branch(brigade.getBranch())
                .role(brigade.getRole())
                .partOf(brigade.getPartOf())
                .websiteLink(brigade.getWebsiteLink())
                .currentCommander(brigade.getCurrentCommander())
                .description(brigade.getDescription())
                .militaryPersonnel(brigade.getMilitaryPersonnel() == null ? new ArrayList<>() : mapMilitaryPersonnel(brigade.getMilitaryPersonnel()))
                .build();
    }
}
