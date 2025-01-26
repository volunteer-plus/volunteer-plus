package volunteer.plus.backend.config.ai.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class Re2Advisor implements CallAroundAdvisor, StreamAroundAdvisor {

    @Override
    public AdvisedResponse aroundCall(final AdvisedRequest advisedRequest,
                                      final CallAroundAdvisorChain chain) {
        return chain.nextAroundCall(this.enhanceReading(advisedRequest));
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(final AdvisedRequest advisedRequest,
                                              final StreamAroundAdvisorChain chain) {
        return chain.nextAroundStream(this.enhanceReading(advisedRequest));
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private AdvisedRequest enhanceReading(AdvisedRequest advisedRequest) {
        log.info("Starting re-reading advisor");
        final Map<String, Object> advisedUserParams = new HashMap<>(advisedRequest.userParams());
        advisedUserParams.put("prompt", advisedRequest.userText());

        return AdvisedRequest.from(advisedRequest)
                .withUserText("{prompt}\nRead the question again: {prompt} and try to understand it better.\n")
                .withUserParams(advisedUserParams)
                .build();
    }
}
