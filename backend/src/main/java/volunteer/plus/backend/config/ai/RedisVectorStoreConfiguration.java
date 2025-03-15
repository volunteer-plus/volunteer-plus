package volunteer.plus.backend.config.ai;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;
import volunteer.plus.backend.domain.enums.AIProvider;

import java.util.Map;

@Configuration
public class RedisVectorStoreConfiguration {
    private final String redisHost;
    private final int redisPort;
    private final RedisVectorStoreProperties redisVectorStoreProperties;

    public RedisVectorStoreConfiguration(@Value("${spring.data.redis.host}") final String redisHost,
                                         @Value("${spring.data.redis.port}") final int redisPort,
                                         final RedisVectorStoreProperties redisVectorStoreProperties) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisVectorStoreProperties = redisVectorStoreProperties;
    }

    @Bean
    public RedisVectorStore openAiVectorStore(final @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        final RedisVectorStore.RedisVectorStoreConfig config = RedisVectorStore.RedisVectorStoreConfig.builder()
                .withIndexName(redisVectorStoreProperties.getOpenAi().getIndex())
                .withPrefix(redisVectorStoreProperties.getOpenAi().getPrefix())
                .build();

        return getRedisVectorStore(embeddingModel, config);
    }

    @Bean
    public RedisVectorStore ollamaVectorStore(final @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        // Create RedisVectorStoreConfig
        final RedisVectorStore.RedisVectorStoreConfig config = RedisVectorStore.RedisVectorStoreConfig.builder()
                .withIndexName(redisVectorStoreProperties.getOllama().getIndex())
                .withPrefix(redisVectorStoreProperties.getOllama().getPrefix())
                .build();

        return getRedisVectorStore(embeddingModel, config);
    }

    @Bean("redisVectorStoreMap")
    public Map<AIProvider, RedisVectorStore> redisVectorStoreMap(final RedisVectorStore openAiVectorStore,
                                                                 final RedisVectorStore ollamaVectorStore) {
        return Map.of(
                AIProvider.OPENAI, openAiVectorStore,
                AIProvider.OLLAMA, ollamaVectorStore
        );
    }

    private RedisVectorStore getRedisVectorStore(final EmbeddingModel embeddingModel,
                                                 final RedisVectorStore.RedisVectorStoreConfig redisConfig) {
        // Create JedisPooled instance
        final JedisPooled jedis = new JedisPooled(redisHost, redisPort);

        // Create RedisVectorStore with required parameters
        return new RedisVectorStore(redisConfig, embeddingModel, jedis, redisVectorStoreProperties.isInitializeSchema());
    }
}
