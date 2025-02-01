package volunteer.plus.backend.domain.mixin.ai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.messages.ToolResponseMessage;

import java.util.List;
import java.util.Map;

public class ToolResponseDefaultImpl extends ToolResponseMessage {

    @JsonCreator
    public ToolResponseDefaultImpl(@JsonProperty("responses") List<ToolResponse> responses,
                                   @JsonProperty("metadata") Map<String, Object> metadata) {
        super(responses, metadata);
    }

    @JsonCreator
    public ToolResponseDefaultImpl(@JsonProperty("responses") List<ToolResponse> responses) {
        super(responses);
    }
}
