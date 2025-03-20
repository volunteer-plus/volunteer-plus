package volunteer.plus.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.service.cache.CacheService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
public class CacheController {
    private final CacheService cacheService;

    @GetMapping("/names")
    public ResponseEntity<Collection<String>> getCacheNames() {
        return ResponseEntity.ok(cacheService.getCacheNames());
    }

    @PostMapping("/clear-all")
    public ResponseEntity<Void> clearCache() {
        cacheService.clearCache();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/keys")
    public ResponseEntity<List<String>> getAllKeyNames(@RequestParam final String cacheName) {
        return ResponseEntity.ok(cacheService.getAllKeyNames(cacheName));
    }
}
