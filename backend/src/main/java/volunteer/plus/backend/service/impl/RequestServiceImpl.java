package volunteer.plus.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.RequestCreationRequestDTO;
import volunteer.plus.backend.domain.dto.RequestDTO;
import volunteer.plus.backend.domain.entity.Request;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.MilitaryPersonnelRepository;
import volunteer.plus.backend.repository.RequestRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.RequestService;

import java.util.ArrayList;
import java.util.List;

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
                .map(request ->
                        RequestDTO.builder()
                                .id(request.getId())
                                .createDate(request.getCreateDate())
                                .updateDate(request.getUpdateDate())
                                .description(request.getDescription())
                                .deadline(request.getDeadline())
                                .amount(request.getAmount())
                                .build());
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
    public List<RequestDTO> createOrUpdateRequests(final String userEmail,
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
                .map(request ->
                        RequestDTO.builder()
                                .id(request.getId())
                                .createDate(request.getCreateDate())
                                .updateDate(request.getUpdateDate())
                                .description(request.getDescription())
                                .deadline(request.getDeadline())
                                .amount(request.getAmount())
                                .build())
                .toList();
    }
}
