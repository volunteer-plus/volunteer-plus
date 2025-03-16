package volunteer.plus.backend.domain.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import volunteer.plus.backend.domain.enums.AIAgentPatternType;
import volunteer.plus.backend.service.ai.tools.AIAgentPattern;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AIPatterRegistry {
    private final Map<AIAgentPatternType, AIAgentPattern> patternMap = new ConcurrentHashMap<>();

    public AIPatterRegistry(final List<AIAgentPattern> tools) {
        tools.forEach(tool -> patternMap.put(tool.getType(), tool));
    }
}
