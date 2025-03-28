package volunteer.plus.backend.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OllamaAIModel {
    TINY_LLAMA("tinyllama:latest"),
    LLAVA("llava:latest"),
    LLAMA("llama3.1:8b");

    private final String modelName;
}
