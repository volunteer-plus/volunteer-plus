package volunteer.plus.backend.domain.mixin.ai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

public class DefaultMessage extends AbstractMessage {

    public DefaultMessage() {
        super(MessageType.USER, "", new HashMap<>());
    }

    @JsonCreator
    public DefaultMessage(
            @JsonProperty("messageType") MessageType messageType,
            @JsonProperty("text") String textContent,
            @JsonProperty("metadata") Map<String, Object> metadata
    ) {
        super(messageType, textContent, metadata);
    }

    @JsonCreator
    public DefaultMessage(
            @JsonProperty("messageType") MessageType messageType,
            @JsonProperty("resource") Resource resource,
            @JsonProperty("metadata") Map<String, Object> metadata
    ) {
        super(messageType, resource, metadata);
    }
}

