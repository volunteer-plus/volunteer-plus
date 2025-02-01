package volunteer.plus.backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.ai.chat.messages.AbstractMessage;
import volunteer.plus.backend.domain.entity.converters.MessageJsonConverter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "ai_chat_messages")
public class AIChatMessage extends BaseEntity {

    @Column(name = "conversation_id", nullable = false)
    private String conversationId;

    @Column(name = "message_data")
    @Convert(converter = MessageJsonConverter.class)
    private AbstractMessage message;

}
