package volunteer.plus.backend.config.ai;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("spring.ai.vector-store.redis")
public class RedisVectorStoreProperties {

    private OpenAi openAi;
    private Ollama ollama;
    private boolean initializeSchema;

    @Data
    public static class OpenAi {
        private String index;
        private String prefix;
    }

    @Data
    public static class Ollama {
        private String index;
        private String prefix;
    }
}
