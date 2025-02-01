package volunteer.plus.backend.domain.mixin.ai;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "messageType",
        defaultImpl = DefaultMessage.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserDefaultImpl.class, name = "USER"),
        @JsonSubTypes.Type(value = AssistantDefaultImpl.class, name = "ASSISTANT"),
        @JsonSubTypes.Type(value = SystemDefaultImpl.class, name = "SYSTEM"),
        @JsonSubTypes.Type(value = ToolResponseDefaultImpl.class, name = "TOOL")
})
public interface AIMessageMixin {
}
