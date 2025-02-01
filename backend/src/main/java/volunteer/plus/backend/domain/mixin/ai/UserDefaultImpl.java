package volunteer.plus.backend.domain.mixin.ai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.model.Media;
import org.springframework.core.io.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UserDefaultImpl extends UserMessage {

    @JsonCreator
    public UserDefaultImpl(
            @JsonProperty("content") String textContent,
            @JsonProperty("media") List<Media> media,
            @JsonProperty("metadata") Map<String, Object> metadata
    ) {
        super(textContent, media, metadata);
    }

    public UserDefaultImpl(String textContent) {
        super(textContent);
    }

    public UserDefaultImpl(Resource resource) {
        super(resource);
    }

    public UserDefaultImpl(String textContent, List<Media> media) {
        super(textContent, media);
    }

    public UserDefaultImpl(String textContent, Media... media) {
        super(textContent, media);
    }

    public UserDefaultImpl(String textContent, Collection<Media> mediaList, Map<String, Object> metadata) {
        super(textContent, mediaList, metadata);
    }

    public UserDefaultImpl(MessageType messageType, String textContent, Collection<Media> media, Map<String, Object> metadata) {
        super(messageType, textContent, media, metadata);
    }
}
