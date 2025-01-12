package volunteer.plus.backend.service.ai.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.service.ai.OpenAIService;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {
    private final ChatClient chatClient;

    @Override
    public String chat(final String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
