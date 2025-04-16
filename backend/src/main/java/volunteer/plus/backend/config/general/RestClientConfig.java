package volunteer.plus.backend.config.general;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    private final String warStatsBaseUrl;

    public RestClientConfig(@Value("${war.stats.rest.client.base-uri}") final String warStatsBaseUrl) {
        this.warStatsBaseUrl = warStatsBaseUrl;
    }

    @Bean("warStatsRestClient")
    public RestClient warStatsRestClient() {
        return RestClient.builder()
                .baseUrl(warStatsBaseUrl)
                .build();
    }
}
