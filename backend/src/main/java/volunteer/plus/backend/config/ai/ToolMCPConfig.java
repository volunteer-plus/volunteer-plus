package volunteer.plus.backend.config.ai;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import volunteer.plus.backend.service.ai.tools.AIAgentPattern;
import volunteer.plus.backend.service.ai.tools.AIMilitaryTools;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Configuration
public class ToolMCPConfig {

    @Bean
    public ToolCallbackProvider volunteerMCPTools(final List<ToolCallback> tools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(tools)
                .build();
    }

    @Bean
    public List<ToolCallback> tools(final AIMilitaryTools aiMilitaryTools,
                                    final List<AIAgentPattern> aiAgentPatterns) {
        return Stream.concat(Stream.of(aiMilitaryTools), aiAgentPatterns.stream())
                .map(ToolCallbacks::from)
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .toList();
    }
}
