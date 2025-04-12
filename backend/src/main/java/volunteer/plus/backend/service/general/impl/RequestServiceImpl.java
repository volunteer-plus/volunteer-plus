package volunteer.plus.backend.service.general.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.RequestCreationRequestDTO;
import volunteer.plus.backend.domain.dto.RequestDTO;
import volunteer.plus.backend.domain.entity.Levy;
import volunteer.plus.backend.domain.entity.Request;
import volunteer.plus.backend.domain.enums.RequestStatus;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.MilitaryPersonnelRepository;
import volunteer.plus.backend.repository.RequestRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.RequestService;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final MilitaryPersonnelRepository militaryPersonnelRepository;

    @Override
    public Page<RequestDTO> getRequests(final Pageable pageable) {
        log.info("Retrieve all requests by page: {}, and size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return requestRepository.findAll(pageable)
                .map(this::getRequestDTO);
    }

    /**
     * When creating a requests we need to specify a creator, either user or military personnel
     * @param userEmail                 user email of user creator
     * @param militaryPersonnelId       military personnel id
     * @param requestCreationRequestDTO payload with requests data to create
     * @return                          list of saved requests DTO
     */
    @Override
    @Transactional
    public List<RequestDTO> createRequests(final String userEmail,
                                           final Long militaryPersonnelId,
                                           final RequestCreationRequestDTO requestCreationRequestDTO) {
        if ((userEmail == null && militaryPersonnelId == null) || (userEmail != null && militaryPersonnelId != null)) {
            throw new ApiException(ErrorCode.ONLY_ONE_CREATOR_FOR_REQUESTS);
        }

        final var requestsToSave = requestCreationRequestDTO.getRequests()
                .stream()
                .map(r -> Request.builder()
                        .description(r.getDescription())
                        .deadline(r.getDeadline())
                        .amount(r.getAmount())
                        .category(r.getCategory())
                        .status(RequestStatus.IN_PROGRESS)
                        .levies(new ArrayList<>())
                        .build())
                .toList();

        final var savedRequests = requestRepository.saveAllAndFlush(requestsToSave);

        if (userEmail != null) {
            final var user =  userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
            savedRequests.forEach(user::addRequest);
            userRepository.saveAndFlush(user);
        }

        if (militaryPersonnelId != null) {
            final var militaryPersonnel = militaryPersonnelRepository.findById(militaryPersonnelId)
                    .orElseThrow(() -> new ApiException(ErrorCode.MILITARY_PERSONNEL_NOT_FOUND));
            savedRequests.forEach(militaryPersonnel::addRequest);
            militaryPersonnelRepository.saveAndFlush(militaryPersonnel);
        }

        return savedRequests.stream()
                .map(this::getRequestDTO)
                .toList();
    }

    @Override
    @Transactional
    public List<RequestDTO> updateRequests(final RequestCreationRequestDTO requestCreationRequestDTO) {

        final Set<Long> requestIds = requestCreationRequestDTO.getRequests()
                .stream()
                .map(RequestDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        final Map<Long, Request> requestMap = requestRepository.findAllById(requestIds)
                .stream()
                .collect(Collectors.toMap(Request::getId, Function.identity()));

        final List<Request> requestsToSave = new ArrayList<>();

        for (final RequestDTO requestDTO : requestCreationRequestDTO.getRequests()) {
            final Request requestFromDB = requestMap.get(requestDTO.getId());

            if (requestFromDB == null) {
                continue;
            }

            requestFromDB.setDescription(requestDTO.getDescription());
            requestFromDB.setDeadline(requestDTO.getDeadline());
            requestFromDB.setAmount(requestDTO.getAmount());
            requestFromDB.setCategory(requestDTO.getCategory());

            requestsToSave.add(requestFromDB);
        }

        return requestRepository.saveAllAndFlush(requestsToSave)
                .stream()
                .map(this::getRequestDTO)
                .toList();
    }

    private RequestDTO getRequestDTO(final Request request) {
        return RequestDTO.builder()
                .id(request.getId())
                .createDate(request.getCreateDate())
                .updateDate(request.getUpdateDate())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .amount(request.getAmount())
                .category(request.getCategory())
                .accumulated(getAccumulated(request))
                .brigade(getBrigade(request))
                .status(request.getStatus())
                .build();
    }

    private String getBrigade(final Request request) {
        if (request.getMilitaryPersonnel() == null || request.getMilitaryPersonnel().getBrigade() == null) {
            return null;
        }
        return request.getMilitaryPersonnel().getBrigade().getName();
    }

    private BigDecimal getAccumulated(final Request request) {
        return request.getLevies() == null ?
                BigDecimal.ZERO :
                request.getLevies()
                        .stream()
                        .map(Levy::getAccumulated)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
