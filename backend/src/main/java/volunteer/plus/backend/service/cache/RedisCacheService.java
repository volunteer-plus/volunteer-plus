package volunteer.plus.backend.service.cache;

import java.util.Set;

public interface RedisCacheService {
    Set<String> getAllKeys(String pattern);

    void evictCache(String name);
}
