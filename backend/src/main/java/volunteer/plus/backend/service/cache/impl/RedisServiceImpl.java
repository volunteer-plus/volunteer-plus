package volunteer.plus.backend.service.cache.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.cache.RedisService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.redis.disabled", havingValue = "false")
public class RedisServiceImpl implements RedisService {
    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public List<String> getAllKeys(final String cacheNamePrefix) {
        final String cachePrefixToUse = cacheNamePrefix != null ? cacheNamePrefix : "";
        final var connection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            return new ArrayList<>(getKeys(cachePrefixToUse, connection));
        } finally {
            RedisConnectionUtils.releaseConnection(connection, redisConnectionFactory);
        }
    }

    private Set<String> getKeys(final String cachePrefixToUse,
                                final RedisConnection connection) {
        final Set<String> keys = new HashSet<>();
        final String pattern = cachePrefixToUse + "*";

        log.info("Scanning redis cache key pattern: {}", pattern);

        final ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .build();

        try (final var c = connection.commands().scan(options)) {
            while (c.hasNext()) {
                keys.add(new String(c.next()));
            }
        }

        return keys;
    }

    @Override
    public void evictCache(final String cacheNamePrefix) {
        final var connection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            clearCache(cacheNamePrefix, connection);
        } finally {
            RedisConnectionUtils.releaseConnection(connection, redisConnectionFactory);
        }
    }

    private void clearCache(final String cacheNamePrefix,
                            final RedisConnection connection) {
        final Set<String> keysToDelete = getKeys(cacheNamePrefix, connection);
        if (!keysToDelete.isEmpty()) {
            final byte[][] dataToDelete = keysToDelete.stream()
                    .map(String::getBytes)
                    .toArray(byte[][]::new);
            connection.commands().del(dataToDelete);
        }
    }
}
