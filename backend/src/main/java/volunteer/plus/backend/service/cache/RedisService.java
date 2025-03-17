package volunteer.plus.backend.service.cache;

import java.util.List;

public interface RedisService {
    List<String> getAllKeys(String cacheNamePrefix);

    void evictCache(String cacheNamePrefix);
}
