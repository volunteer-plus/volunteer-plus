package volunteer.plus.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.MilitaryPersonnelCreationDTO;
import volunteer.plus.backend.domain.dto.MilitaryPersonnelCreationRequestDTO;
import volunteer.plus.backend.domain.entity.AddRequest;
import volunteer.plus.backend.domain.entity.Brigade;
import volunteer.plus.backend.domain.dto.BrigadeDTO;
import volunteer.plus.backend.domain.entity.MilitaryPersonnel;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.AddRequestRepository;
import volunteer.plus.backend.repository.BrigadeRepository;
import volunteer.plus.backend.repository.MilitaryPersonnelRepository;
import volunteer.plus.backend.service.MilitaryPersonnelService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static volunteer.plus.backend.domain.dto.BrigadeDTO.MilitaryPersonnelDTO.mapMilitaryPersonnel;
import static volunteer.plus.backend.util.HashUtil.SHA_256;
import static volunteer.plus.backend.util.HashUtil.hashValue;

@Slf4j
@Service
@RequiredArgsConstructor
public class MilitaryPersonnelServiceImpl implements MilitaryPersonnelService {
    private final BrigadeRepository brigadeRepository;
    private final AddRequestRepository addRequestRepository;
    private final MilitaryPersonnelRepository militaryPersonnelRepository;

    @Override
    public Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>> getMilitaryPersonnel(final Set<String> regimentCodes) {
        log.info("Retrieve military personnel of brigades");

        final List<Brigade> brigades;

        if (regimentCodes == null) {
            brigades = brigadeRepository.findAll();
        } else {
            brigades = brigadeRepository.findAllByRegimentCodeIn(regimentCodes);
        }

        return getMilitaryPersonnelResultDTO(brigades);
    }

    @Override
    @Transactional
    public Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>> createMilitaryPersonnel(final MilitaryPersonnelCreationRequestDTO militaryPersonnelCreationRequestDTO) {
        if (militaryPersonnelCreationRequestDTO == null || militaryPersonnelCreationRequestDTO.getMilitaryPersonnel() == null ||
                militaryPersonnelCreationRequestDTO.getMilitaryPersonnel().isEmpty()) {
            throw new ApiException(ErrorCode.EMPTY_MILITARY_PERSONNEL_DATA);
        }

        final var militaryPersonnelCreationDTOs = militaryPersonnelCreationRequestDTO.getMilitaryPersonnel();
        final var regimentCodes = militaryPersonnelCreationDTOs.stream().map(MilitaryPersonnelCreationDTO::getRegimentCode).collect(Collectors.toSet());

        final Map<String, Brigade> mapOfBrigades = brigadeRepository.findAllByRegimentCodeIn(regimentCodes).stream()
                .collect(Collectors.toMap(Brigade::getRegimentCode, Function.identity()));

        if (mapOfBrigades.size() != militaryPersonnelCreationDTOs.size()) {
            throw new ApiException(ErrorCode.BRIGADE_NOT_FOUND);
        }

        final List<AddRequest> requestsToUpdate = new ArrayList<>();
        final List<Brigade> brigadesToUpdate = new ArrayList<>();

        for (final var creationDTO : militaryPersonnelCreationDTOs) {
            final var brigade = mapOfBrigades.get(creationDTO.getRegimentCode());
            final var addRequest = addRequestRepository.findByRequestId(hashValue(SHA_256, creationDTO.getAddRequestId()));
            final var militaryPersonnel = getMilitaryPersonnelFromCreationDTO(creationDTO);

            if (addRequest.isEmpty()) {
                throw new ApiException(ErrorCode.ADD_REQUEST_NOT_FOUND);
            }

            if (addRequest.get().isExecuted()) {
                throw new ApiException(ErrorCode.ADD_REQUEST_IS_ALREADY_EXECUTED);
            }

            addRequest.get().setExecuted(true);
            requestsToUpdate.add(addRequest.get());

            brigade.setupMilitaryPersonnel(militaryPersonnel);
        }

        addRequestRepository.saveAll(requestsToUpdate);

        final var brigades = brigadeRepository.saveAll(brigadesToUpdate);

        return getMilitaryPersonnelResultDTO(brigades);
    }

    @Override
    @Transactional
    public void deleteMilitaryPersonnel(final Set<Long> ids) {
        final var mps = militaryPersonnelRepository.findAllById(ids);

        if (mps.size() != ids.size()) {
            throw new ApiException(ErrorCode.MILITARY_PERSONNEL_NOT_FOUND);
        }

        final List<Brigade> brigades = mps.stream().map(MilitaryPersonnel::getBrigade).toList();
        for (final var brigade : brigades) {
            final var mpsToDelete = brigade.getMilitaryPersonnel()
                    .stream()
                    .filter(mp -> ids.contains(mp.getId()))
                    .toList();
            mpsToDelete.forEach(brigade::removeMilitaryPersonnel);
        }

        brigadeRepository.saveAllAndFlush(brigades);
        militaryPersonnelRepository.deleteAll(mps);
    }

    private Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>> getMilitaryPersonnelResultDTO(List<Brigade> brigades) {
        final Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>> result = new HashMap<>();

        brigades.forEach(brigade -> {
            final String regimentCode = brigade.getRegimentCode();
            final List<BrigadeDTO.MilitaryPersonnelDTO> militaryPersonnel = brigade.getMilitaryPersonnel() == null ? new ArrayList<>() : mapMilitaryPersonnel(brigade.getMilitaryPersonnel());
            result.put(regimentCode, militaryPersonnel);
        });

        return result;
    }

    private List<MilitaryPersonnel> getMilitaryPersonnelFromCreationDTO(MilitaryPersonnelCreationDTO creationDTO) {
        return creationDTO.getMilitaryPersonnel()
                .stream()
                .map(mp ->
                        MilitaryPersonnel.builder()
                                .firstName(mp.getFirstName())
                                .lastName(mp.getLastName())
                                .dateOfBirth(mp.getDateOfBirth())
                                .placeOfBirth(mp.getPlaceOfBirth())
                                .rank(mp.getRank())
                                .status(mp.getStatus())
                                .requests(new ArrayList<>())
                                .build()
                )
                .toList();
    }
}
