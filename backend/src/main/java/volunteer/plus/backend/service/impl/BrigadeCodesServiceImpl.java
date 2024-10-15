package volunteer.plus.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.BrigadeCodes;
import volunteer.plus.backend.repository.BrigadeCodesRepository;
import volunteer.plus.backend.service.BrigadeCodesService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrigadeCodesServiceImpl implements BrigadeCodesService {
    private final BrigadeCodesRepository brigadeCodesRepository;

    @Override
    public List<String> getCodes() {
        log.info("Get all validated brigade codes");
        return brigadeCodesRepository.findAll()
                .stream()
                .map(BrigadeCodes::getCode)
                .toList();
    }
}
