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

    @Value("${spring.ai.vectorstore.redis.index}")
    private String index;

    @Value("${spring.ai.vectorstore.redis.prefix}")
    private String prefix;

    @Value("${spring.ai.vectorstore.redis.initialize-schema}")
    private boolean initializeSchema;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisVectorStore openAiVectorStore(final @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        return getRedisVectorStore(embeddingModel);
    }

    @Bean
    public RedisVectorStore ollamaVectorStore(final @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        return getRedisVectorStore(embeddingModel);
    }

    private RedisVectorStore getRedisVectorStore(final EmbeddingModel embeddingModel) {
        // Create RedisVectorStoreConfig
        RedisVectorStore.RedisVectorStoreConfig config = RedisVectorStore.RedisVectorStoreConfig.builder()
                .withIndexName(index)
                .withPrefix(prefix)
                .build();

        // Create JedisPooled instance
        JedisPooled jedis = new JedisPooled(redisHost, redisPort);

        // Create RedisVectorStore with required parameters
        return new RedisVectorStore(config, embeddingModel, jedis, initializeSchema);
    }
}
