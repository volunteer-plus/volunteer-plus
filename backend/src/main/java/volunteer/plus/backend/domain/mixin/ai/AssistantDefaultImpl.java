package volunteer.plus.backend.domain.mixin.ai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.messages.AssistantMessage;
import java.util.List;
import java.util.Map;

public class AssistantDefaultImpl extends AssistantMessage {

    @JsonCreator
    public AssistantDefaultImpl(
            @JsonProperty("content") String content,
            @JsonProperty("metadata") Map<String, Object> properties,
            @JsonProperty("toolCalls") List<?> toolCalls
    ) {
        super(content, properties, toolCalls == null ? null :
                toolCalls.stream().map(ToolCall.class::cast).toList());
    }

    public AssistantDefaultImpl(String content) {
        super(content);
    }

    public AssistantDefaultImpl(String content, Map<String, Object> properties) {
        super(content, properties);
    }
}
