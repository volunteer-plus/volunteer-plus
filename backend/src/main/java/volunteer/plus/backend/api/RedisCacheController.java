package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.service.cache.RedisCacheService;

import java.util.Set;

@RestController
@RequestMapping("/api/cache-redis")
@RequiredArgsConstructor
public class RedisCacheController {
    private final RedisCacheService redisCacheService;

    @GetMapping("/keys")
    public Set<String> getAllKeys(@RequestParam(required = false) final String pattern) {
        return redisCacheService.getAllKeys(pattern);
    }

    @PostMapping("/evict")
    public void evictCache(@RequestParam final String name) {
        redisCacheService.evictCache(name);
    }
}
