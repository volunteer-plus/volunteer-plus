package volunteer.plus.backend.service.general.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.BrigadeCodesDateDTO;
import volunteer.plus.backend.domain.entity.BrigadeCodes;
import volunteer.plus.backend.repository.BrigadeCodesRepository;
import volunteer.plus.backend.service.general.BrigadeCodesService;

import java.util.List;

import static volunteer.plus.backend.config.cache.RedisCacheConfig.REDIS_BROKER_CODES_CACHE_KEY;
import static volunteer.plus.backend.config.cache.RedisCacheConfig.REDIS_BROKER_CODES_CACHE_NAMES;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrigadeCodesServiceImpl implements BrigadeCodesService {
    private final BrigadeCodesRepository brigadeCodesRepository;

    @Override
    @Cacheable(
            key = REDIS_BROKER_CODES_CACHE_KEY,
            cacheNames = REDIS_BROKER_CODES_CACHE_NAMES,
            cacheManager = "redisCacheManager"
    )
    public List<String> getCodes() {
        log.info("Get all validated brigade codes");
        return brigadeCodesRepository.findAll()
                .stream()
                .map(BrigadeCodes::getCode)
                .toList();
    }

    @Override
    public List<BrigadeCodes> getCodesCreatedAt(final BrigadeCodesDateDTO date) {
        log.info("Get all validated brigade codes created at {}", date.getDate());
        return brigadeCodesRepository.findAll()
                .stream()
                .filter(b -> b.getCreateDate().toLocalDate().isEqual(date.getDate()))
                .toList();
    }
}
