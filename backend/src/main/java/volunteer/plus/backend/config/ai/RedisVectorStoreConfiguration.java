package volunteer.plus.backend.config.ai;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisVectorStoreConfiguration {

    @Value("${spring.ai.vectorstore.redis-open-ai.index}")
    private String indexOpenAi;

    @Value("${spring.ai.vectorstore.redis-open-ai.prefix}")
    private String prefixOpenAi;

    @Value("${spring.ai.vectorstore.redis-ollama.index}")
    private String indexOllama;

    @Value("${spring.ai.vectorstore.redis-ollama.prefix}")
    private String prefixOllama;

    @Value("${spring.ai.vectorstore.redis.initialize-schema}")
    private boolean initializeSchema;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisVectorStore openAiVectorStore(final @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        final RedisVectorStore.RedisVectorStoreConfig config = RedisVectorStore.RedisVectorStoreConfig.builder()
                .withIndexName(indexOpenAi)
                .withPrefix(prefixOpenAi)
                .build();

        return getRedisVectorStore(embeddingModel, config);
    }

    @Bean
    public RedisVectorStore ollamaVectorStore(final @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        // Create RedisVectorStoreConfig
        final RedisVectorStore.RedisVectorStoreConfig config = RedisVectorStore.RedisVectorStoreConfig.builder()
                .withIndexName(indexOllama)
                .withPrefix(prefixOllama)
                .build();

        return getRedisVectorStore(embeddingModel, config);
    }

    private RedisVectorStore getRedisVectorStore(final EmbeddingModel embeddingModel,
                                                 final RedisVectorStore.RedisVectorStoreConfig redisConfig) {
        // Create JedisPooled instance
        final JedisPooled jedis = new JedisPooled(redisHost, redisPort);

        // Create RedisVectorStore with required parameters
        return new RedisVectorStore(redisConfig, embeddingModel, jedis, initializeSchema);
    }
}
