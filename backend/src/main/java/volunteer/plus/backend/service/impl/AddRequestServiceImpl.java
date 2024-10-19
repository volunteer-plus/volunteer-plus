package volunteer.plus.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.AddRequest;
import volunteer.plus.backend.dto.AddRequestResponseDTO;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.AddRequestRepository;
import volunteer.plus.backend.service.AddRequestService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddRequestServiceImpl implements AddRequestService {
    private final AddRequestRepository addRequestRepository;

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
    public List<AddRequestResponseDTO> generate(final Long amount) {
        log.info("Generate add-request codes with amount: {}", amount);

        final List<AddRequest> addRequests = new ArrayList<>();
        final List<String> keys = new ArrayList<>();

        // Create SHA-256 hash of the UUID
        final var digest = MessageDigest.getInstance("SHA-256");

        for (var i = 0; i < amount; i++) {
            // generate absolute unique key
            final String key = UUID.randomUUID().toString() + System.currentTimeMillis();
            keys.add(key);
            final var encodedHash = hashKey(digest, key);

            addRequests.add(
                    AddRequest.builder()
                            .requestId(encodedHash)
                            .executed(false)
                            .build()
            );
        }

        final List<AddRequest> savedRequests = addRequestRepository.saveAll(addRequests);
        final List<AddRequestResponseDTO> result = new ArrayList<>();

        populateResult(savedRequests, keys, digest, result);

        return result;
    }

    // we need to populate results because we need to return real data without being hashed
    // also we should not call DB to keep high performance
    private void populateResult(List<AddRequest> savedRequests, List<String> keys, MessageDigest digest, List<AddRequestResponseDTO> result) {
        for (final var request : savedRequests) {
            for (final var key : keys) {
                final var hashedKey = hashKey(digest, key);
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

    private String hashKey(MessageDigest digest, String key) {
        byte[] hash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
        // Encode the hash to Base64 to store in DB as a readable string
        return Base64.getEncoder().encodeToString(hash);
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
