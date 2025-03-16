package volunteer.plus.backend.service.cache.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.cache.RedisCacheService;

import java.util.Set;

@Slf4j
@Service
public class RedisCacheServiceImpl implements RedisCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheServiceImpl(@Autowired final RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Set<String> getAllKeys(String pattern) {
        final String path = pattern == null ? "*" : pattern + ":*";
        return redisTemplate.keys(path);
    }

    @Override
    public void evictCache(final String name) {
        redisTemplate.delete(name);
        log.info("Cleared all keys: {}", name);
    }
}
