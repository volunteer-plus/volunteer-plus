package volunteer.plus.backend.service.cache.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.cache.CacheService;
import volunteer.plus.backend.service.cache.RedisService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static volunteer.plus.backend.util.CacheUtil.BRIGADES_CACHE;
import static volunteer.plus.backend.util.CacheUtil.BRIGADE_CODES_CACHE;

@Slf4j
@Service
public class CacheServiceImpl implements CacheService {
    private final CacheManager cacheManager;
    private final RedisService redisService;

    public CacheServiceImpl(final CacheManager cacheManager,
                            @Autowired(required = false) final RedisService redisService) {
        this.cacheManager = cacheManager;
        this.redisService = redisService;
    }


    @Override
    public Collection<String> getCacheNames() {
        log.info("Getting cache names from app..");

        final List<String> result;

        if (redisService != null) {
            result = redisService.getAllKeys(null);
        } else {
            result = new ArrayList<>();
        }

        result.addAll(cacheManager.getCacheNames());

        return result;
    }

    @Override
    @CacheEvict(cacheNames = {BRIGADE_CODES_CACHE, BRIGADES_CACHE}, allEntries = true)
    public void clearCache() {
        log.info("Clearing all cache");
        if (redisService != null) {
            redisService.evictCache("");
        }
    }

    @Override
    public List<String> getAllKeyNames(final String cacheName) {
        final Cache cache = cacheManager.getCache(cacheName);

        if (cache instanceof CaffeineCache caffeineCache) {
            return caffeineCache.getNativeCache()
                    .asMap()
                    .keySet()
                    .stream()
                    .map(Object::toString)
                    .toList();
        }

        return List.of();
    }
}
