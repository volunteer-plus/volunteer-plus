package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"users", "messages"})
@Entity
@Table(name = "conversation_room")
public class ConversationRoom extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private boolean deleted;

    @ManyToMany
    @JoinTable(
            name = "conversation_room_user",
            joinColumns = @JoinColumn(name = "conversation_room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "conversationRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WSMessage> messages = new ArrayList<>();

    public void addUser(User user) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.add(user);
        user.getConversationRooms().add(this);
    }

    public void removeUser(User user) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.remove(user);
        user.getConversationRooms().remove(this);
    }

    public void addMessage(WSMessage message) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        this.messages.add(message);
        message.setConversationRoom(this);
    }

    public void removeMessage(WSMessage message) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        this.messages.remove(message);
        message.setConversationRoom(null);
    }
}
