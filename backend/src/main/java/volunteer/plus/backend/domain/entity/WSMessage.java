package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = "conversationRoom")
@Entity
@Table(name = "ws_message")
public class WSMessage extends BaseEntity {
    @Column(name = "content")
    private String content;

    @Column(name = "from_user")
    private Long fromUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conversation_room_id")
    private ConversationRoom conversationRoom;
}
