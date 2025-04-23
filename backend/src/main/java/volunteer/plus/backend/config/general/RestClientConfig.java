package volunteer.plus.backend.config.general;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    private final String warStatsBaseUrl;
    private final String ukraineAlarmUrl;
    private final String ukraineAlarmApiKey;

    public RestClientConfig(@Value("${war.stats.rest.client.base-uri}") final String warStatsBaseUrl,
                            @Value("${api.ukraine.alarm.rest.client.base-uri}") final String ukraineAlarmUrl,
                            @Value("${api.ukraine.alarm.rest.client.api.key}") final String ukraineAlarmApiKey) {
        this.warStatsBaseUrl = warStatsBaseUrl;
        this.ukraineAlarmUrl = ukraineAlarmUrl;
        this.ukraineAlarmApiKey = ukraineAlarmApiKey;
    }

    @Bean("warStatsRestClient")
    public RestClient warStatsRestClient() {
        return RestClient.builder()
                .baseUrl(warStatsBaseUrl)
                .build();
    }

    @Bean("ukraineAlarmRestClient")
    public RestClient ukraineAlarmRestClient() {
        return RestClient.builder()
                .baseUrl(ukraineAlarmUrl)
                .defaultHeader("Authorization", ukraineAlarmApiKey)
                .build();
    }
}
