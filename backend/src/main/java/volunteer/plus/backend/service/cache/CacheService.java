package volunteer.plus.backend.service.cache;

import java.util.Collection;
import java.util.List;

public interface CacheService {
    Collection<String> getCacheNames();

    void clearCache();

    List<String> getAllKeyNames(String cacheName);
}
