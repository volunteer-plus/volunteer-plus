package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.service.cache.RedisService;

import java.util.List;

@RestController
@RequestMapping("/api/cache-redis")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.redis.disabled", havingValue = "false")
public class RedisCacheController {
    private final RedisService redisService;

    @GetMapping("/keys")
    public List<String> getAllKeys(@RequestParam(required = false) final String cacheNamePrefix) {
        return redisService.getAllKeys(cacheNamePrefix);
    }

    @PostMapping("/evict")
    public void evictCache(@RequestParam(required = false, defaultValue = "") final String cacheNamePrefix) {
        redisService.evictCache(cacheNamePrefix);
    }
}
