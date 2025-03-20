package volunteer.plus.backend.domain.mixin.ai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.core.io.Resource;

public class SystemDefaultImpl extends SystemMessage {
    @JsonCreator
    public SystemDefaultImpl(@JsonProperty("text") String textContent) {
        super(textContent);
    }

    @JsonCreator
    public SystemDefaultImpl(@JsonProperty("resource") Resource resource) {
        super(resource);
    }
}
