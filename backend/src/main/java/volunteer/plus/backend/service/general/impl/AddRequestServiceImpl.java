package volunteer.plus.backend.service.general.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.entity.AddRequest;
import volunteer.plus.backend.domain.dto.AddRequestResponseDTO;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.AddRequestRepository;
import volunteer.plus.backend.service.general.AddRequestService;
import volunteer.plus.backend.service.general.BrigadeCodesService;

import java.util.*;

import static volunteer.plus.backend.util.HashUtil.SHA_256;
import static volunteer.plus.backend.util.HashUtil.hashValue;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddRequestServiceImpl implements AddRequestService {
    private final AddRequestRepository addRequestRepository;
    private final BrigadeCodesService brigadeCodesService;

    @Override
    public List<AddRequestResponseDTO> getRequests() {
        return addRequestRepository.findAll()
                .stream()
                .map(request ->
                        AddRequestResponseDTO.builder()
                                .id(request.getId())
                                .code(request.getRequestId()) // hashed values safe to return
                                .executed(request.isExecuted())
                                .build()
                )
                .toList();
    }

    @SneakyThrows
    @Override
    @Transactional
    public List<AddRequestResponseDTO> generate(final String regimentCode, final Long amount) {
        log.info("Generate add-request codes with amount: {}", amount);

        final var validRegimentCodes = brigadeCodesService.getCodes();

        if (!validRegimentCodes.contains(regimentCode)) {
            throw new ApiException(ErrorCode.NOT_VALID_REGIMENT_CODE);
        }

        final List<AddRequest> addRequests = new ArrayList<>();
        final List<String> keys = new ArrayList<>();

        for (var i = 0; i < amount; i++) {
            // generate absolute unique key
            final String key = UUID.randomUUID().toString() + System.currentTimeMillis();
            keys.add(key);
            final var encodedHash = hashValue(SHA_256, key);

            addRequests.add(
                    AddRequest.builder()
                            .regimentCode(regimentCode)
                            .requestId(encodedHash)
                            .executed(false)
                            .build()
            );
        }

        final List<AddRequest> savedRequests = addRequestRepository.saveAll(addRequests);
        final List<AddRequestResponseDTO> result = new ArrayList<>();

        populateResult(savedRequests, keys, result);

        return result;
    }

    @Override
    public void validateCode(final String code) {
        final var addRequest = addRequestRepository.findByRequestId(hashValue(SHA_256, code));

        if (addRequest.isEmpty()) {
            throw new ApiException(ErrorCode.ADD_REQUEST_NOT_FOUND);
        }

        if (addRequest.get().isExecuted()) {
            throw new ApiException(ErrorCode.ADD_REQUEST_IS_ALREADY_EXECUTED);
        }
    }

    // we need to populate results because we need to return real data without being hashed
    // also we should not call DB to keep high performance
    private void populateResult(List<AddRequest> savedRequests, List<String> keys, List<AddRequestResponseDTO> result) {
        for (final var request : savedRequests) {
            for (final var key : keys) {
                final var hashedKey = hashValue(SHA_256, key);
                if (!Objects.equals(hashedKey, request.getRequestId())) {
                    continue;
                }
                result.add(
                        AddRequestResponseDTO.builder()
                                .id(request.getId())
                                .executed(request.isExecuted())
                                .code(key)
                                .build()
                );
            }
        }
    }


    @Override
    @Transactional
    public void deleteAll(final Set<Long> ids) {
        final List<AddRequest> requests = addRequestRepository.findAllById(ids);

        if (requests.size() != ids.size()) {
            throw new ApiException(ErrorCode.ADD_REQUEST_NOT_FOUND);
        }

        addRequestRepository.deleteAll(requests);
    }
}
