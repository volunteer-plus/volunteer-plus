package volunteer.plus.backend.api;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID;

@Validated
@RestController
@RequestMapping("/api/ai")
public class AIMemoryController {
    private final ChatMemory chatMemory;

    public AIMemoryController(final ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }

    @GetMapping("/memories")
    public ResponseEntity<List<Message>> getMessages(@RequestParam final int size,
                                                     @RequestParam(defaultValue = DEFAULT_CHAT_MEMORY_CONVERSATION_ID) final String conversationId) {
        return ResponseEntity.ok(chatMemory.get(conversationId, size));
    }

    @DeleteMapping("/memory/reboot")
    public ResponseEntity<Void> clear(@RequestParam(defaultValue = DEFAULT_CHAT_MEMORY_CONVERSATION_ID) final String conversationId) {
        chatMemory.clear(conversationId);
        return ResponseEntity.ok().build();
    }
}
