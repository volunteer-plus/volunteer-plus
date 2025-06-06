package volunteer.plus.backend.service.general.impl;

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
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.AddRequestRepository;
import volunteer.plus.backend.repository.BrigadeRepository;
import volunteer.plus.backend.repository.MilitaryPersonnelRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.MilitaryPersonnelService;

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
    private final UserRepository userRepository;
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
        validatePayload(militaryPersonnelCreationRequestDTO);

        final var militaryPersonnelCreationDTOs = militaryPersonnelCreationRequestDTO.getMilitaryPersonnel();

        final var addRequestIds = militaryPersonnelCreationDTOs.stream()
                .map(MilitaryPersonnelCreationDTO::getAddRequestId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        final var hashedRequests = addRequestIds.stream()
                .map(val -> hashValue(SHA_256, val))
                .collect(Collectors.toSet());

        final var addRequestsMap = addRequestRepository.findByRequestIdIn(hashedRequests)
                .stream()
                .collect(Collectors.toMap(AddRequest::getRequestId, Function.identity()));

        final var userMap = userRepository.findAll()
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        final var regimentCodes = addRequestsMap.values()
                .stream()
                .map(AddRequest::getRegimentCode)
                .collect(Collectors.toSet());

        final Map<String, Brigade> mapOfBrigades = brigadeRepository.findAllByRegimentCodeIn(regimentCodes)
                .stream()
                .collect(Collectors.toMap(Brigade::getRegimentCode, Function.identity()));

        if (mapOfBrigades.size() != militaryPersonnelCreationDTOs.size()) {
            throw new ApiException(ErrorCode.BRIGADE_NOT_FOUND);
        }

        final List<AddRequest> requestsToUpdate = new ArrayList<>();
        final List<Brigade> brigadesToUpdate = new ArrayList<>();

        for (final var creationDTO : militaryPersonnelCreationDTOs) {
            final var addRequest = addRequestsMap.get(hashValue(SHA_256, creationDTO.getAddRequestId()));

            if (addRequest == null) {
                throw new ApiException(ErrorCode.ADD_REQUEST_NOT_FOUND);
            }

            if (addRequest.isExecuted()) {
                throw new ApiException(ErrorCode.ADD_REQUEST_IS_ALREADY_EXECUTED);
            }

            final var brigade = mapOfBrigades.get(addRequest.getRegimentCode());
            final var militaryPersonnel = getMilitaryPersonnelFromCreationDTO(creationDTO, userMap);

            addRequest.setExecuted(true);
            requestsToUpdate.add(addRequest);

            brigade.setupMilitaryPersonnel(militaryPersonnel);
            brigadesToUpdate.add(brigade);
        }

        addRequestRepository.saveAll(requestsToUpdate);

        final var brigades = brigadeRepository.saveAll(brigadesToUpdate);

        return getMilitaryPersonnelResultDTO(brigades);
    }

    @Override
    @Transactional
    public List<BrigadeDTO.MilitaryPersonnelDTO> updateMilitaryPersonnel(final MilitaryPersonnelCreationRequestDTO militaryPersonnelCreationRequestDTO) {
        validatePayload(militaryPersonnelCreationRequestDTO);

        final List<MilitaryPersonnelCreationDTO> militaryPersonnelCreationDTOList = militaryPersonnelCreationRequestDTO.getMilitaryPersonnel();
        final Set<MilitaryPersonnelCreationDTO.MilitaryPersonnelDTO> mps = militaryPersonnelCreationDTOList.stream()
                .flatMap(mpc -> mpc.getMilitaryPersonnel().stream())
                .filter(mp -> mp.getId() != null)
                .collect(Collectors.toSet());

        final var ids = mps.stream().map(MilitaryPersonnelCreationDTO.MilitaryPersonnelDTO::getId).collect(Collectors.toSet());
        final Map<Long, MilitaryPersonnel> mpsFromDB = militaryPersonnelRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(MilitaryPersonnel::getId, Function.identity()));

        if (mps.size() != mpsFromDB.size()) {
            throw new ApiException(ErrorCode.MILITARY_PERSONNEL_NOT_FOUND);
        }

        final List<MilitaryPersonnel> mpToUpdate = new ArrayList<>();

        mps.forEach(mp -> {
            final var mpFromDB = mpsFromDB.get(mp.getId());
            mpFromDB.setFirstName(mp.getFirstName());
            mpFromDB.setLastName(mp.getLastName());
            mpFromDB.setDateOfBirth(mp.getDateOfBirth());
            mpFromDB.setPlaceOfBirth(mp.getPlaceOfBirth());
            mpFromDB.setRank(mp.getRank());
            mpFromDB.setStatus(mp.getStatus());
            mpToUpdate.add(mpFromDB);
        });

        final var savedMps = militaryPersonnelRepository.saveAll(mpToUpdate);

        return mapMilitaryPersonnel(savedMps);
    }

    private void validatePayload(MilitaryPersonnelCreationRequestDTO militaryPersonnelCreationRequestDTO) {
        if (militaryPersonnelCreationRequestDTO == null || militaryPersonnelCreationRequestDTO.getMilitaryPersonnel() == null ||
                militaryPersonnelCreationRequestDTO.getMilitaryPersonnel().isEmpty()) {
            throw new ApiException(ErrorCode.EMPTY_MILITARY_PERSONNEL_DATA);
        }
    }

    @Override
    @Transactional
    public void deleteMilitaryPersonnel(final Set<Long> ids) {
        final var mps = militaryPersonnelRepository.findAllById(ids);

        if (mps.size() != ids.size()) {
            throw new ApiException(ErrorCode.MILITARY_PERSONNEL_NOT_FOUND);
        }

        militaryPersonnelRepository.deleteAll(mps);
    }

    private Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>> getMilitaryPersonnelResultDTO(List<Brigade> brigades) {
        final Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>> result = new HashMap<>();

        brigades.forEach(brigade -> {
            final String regimentCode = brigade.getRegimentCode();
            final List<BrigadeDTO.MilitaryPersonnelDTO> militaryPersonnel = brigade.getMilitaryPersonnel() == null ?
                    new ArrayList<>() :
                    mapMilitaryPersonnel(brigade.getMilitaryPersonnel());
            result.put(regimentCode, militaryPersonnel);
        });

        return result;
    }

    private List<MilitaryPersonnel> getMilitaryPersonnelFromCreationDTO(final MilitaryPersonnelCreationDTO creationDTO,
                                                                        final Map<Long, User> userMap) {
        return creationDTO.getMilitaryPersonnel()
                .stream()
                .map(mp -> {
                    User user = null;

                    if (mp.getUserId() != null && userMap.containsKey(mp.getUserId())) {
                        user = userMap.get(mp.getUserId());
                    }

                    return MilitaryPersonnel.builder()
                            .firstName(mp.getFirstName())
                            .lastName(mp.getLastName())
                            .dateOfBirth(mp.getDateOfBirth())
                            .placeOfBirth(mp.getPlaceOfBirth())
                            .rank(mp.getRank())
                            .status(mp.getStatus())
                            .requests(new ArrayList<>())
                            .user(user)
                            .build();
                })
                .toList();
    }
}
