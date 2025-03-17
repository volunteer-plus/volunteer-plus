package volunteer.plus.backend.config.ai;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;
import volunteer.plus.backend.domain.enums.AIProvider;

import java.util.Map;

@Configuration
@ConditionalOnProperty(value = "spring.redis.disabled", havingValue = "false")
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
    public JedisPooled jedisPooled() {
        return new JedisPooled(redisHost, redisPort);
    }

    @Bean
    public RedisVectorStore openAiVectorStore(final @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel,
                                              final JedisPooled jedisPooled) {
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName(redisVectorStoreProperties.getOpenAi().getIndex())
                .prefix(redisVectorStoreProperties.getOpenAi().getPrefix())
                .build();
    }

    @Bean
    public RedisVectorStore ollamaVectorStore(final @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel,
                                              final JedisPooled jedisPooled) {
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName(redisVectorStoreProperties.getOllama().getIndex())
                .prefix(redisVectorStoreProperties.getOllama().getPrefix())
                .build();
    }

    @Bean("redisVectorStoreMap")
    public Map<AIProvider, RedisVectorStore> redisVectorStoreMap(final RedisVectorStore openAiVectorStore,
                                                                 final RedisVectorStore ollamaVectorStore) {
        return Map.of(
                AIProvider.OPENAI, openAiVectorStore,
                AIProvider.OLLAMA, ollamaVectorStore
        );
    }
}
