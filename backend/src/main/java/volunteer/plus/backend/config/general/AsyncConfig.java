package volunteer.plus.backend.config.general;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {
    @Value("${system.executor.core-size}")
    private Integer corePoolSize;

    @Value("${system.executor.pool-size}")
    private Integer mapPoolSize;

    @Value("${system.executor.queue-capacity}")
    private Integer queueCapacity;

    @Bean(name = "asyncExecutor")
    public Executor customAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(mapPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("async-thread-");
        executor.initialize();
        return executor;
    }

}
