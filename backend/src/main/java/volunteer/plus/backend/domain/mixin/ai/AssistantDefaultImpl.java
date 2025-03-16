package volunteer.plus.backend.domain.mixin.ai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.messages.AssistantMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssistantDefaultImpl extends AssistantMessage {

    @JsonCreator
    public AssistantDefaultImpl(
            @JsonProperty("text") String textContent,
            @JsonProperty("metadata") Map<String, Object> properties,
            @JsonProperty("toolCalls") List<?> toolCalls
    ) {
        super(
                textContent,
                properties,
                toolCalls == null ? new ArrayList<>() :
                        toolCalls.stream()
                                .map(ToolCall.class::cast)
                                .toList()
        );
    }

    public AssistantDefaultImpl(String textContent) {
        super(textContent);
    }

    public AssistantDefaultImpl(String textContent, Map<String, Object> properties) {
        super(textContent, properties);
    }
}
