package volunteer.plus.backend.config.ai;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.entity.AIChatMessage;
import volunteer.plus.backend.repository.AIChatMessageRepository;

import java.util.List;

@Component
public class AIMemory implements ChatMemory {
    private final AIChatMessageRepository repository;

    public AIMemory(final AIChatMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void add(final String conversationId,
                    final List<Message> messages) {
        final List<AIChatMessage> entities = messages.stream()
                .map(msg ->
                        AIChatMessage.builder()
                                .conversationId(conversationId)
                                .message((AbstractMessage) msg)
                                .build()
                )
                .toList();
        repository.saveAll(entities);
    }

    @Override
    @Transactional
    public void clear(final String conversationId) {
        repository.deleteByConversationId(conversationId);
    }

    @Override
    public List<Message> get(final String conversationId,
                             final int lastN) {
        final Pageable pageable = PageRequest.of(0, lastN);
        // return the messages in chronological order (oldest first)
        return repository.findByConversationIdOrderByCreateDateAsc(conversationId, pageable)
                .stream()
                .map(AIChatMessage::getMessage)
                .map(Message.class::cast)
                .toList();
    }
}
