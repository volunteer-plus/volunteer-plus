package volunteer.plus.backend.service.ai.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.ai.agent.*;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.service.ai.AgentAIPatternService;
import volunteer.plus.backend.service.ai.tools.impl.OrchestratorWorkers;
import volunteer.plus.backend.service.ai.tools.impl.ParallelizationWorkflow;
import volunteer.plus.backend.service.ai.tools.impl.PromptChainWorkflow;
import volunteer.plus.backend.service.ai.tools.impl.RoutingWorkflow;

import java.util.List;
import java.util.Map;

import static volunteer.plus.backend.domain.enums.AIChatClient.*;

@Slf4j
@Service
public class AgentAIPatternServiceImpl implements AgentAIPatternService {
    private final Map<AIChatClient, ChatClient> openAIChatClientMap;
    private final Map<AIChatClient, ChatClient> ollamaChatClientMap;

    public AgentAIPatternServiceImpl(final @Qualifier("openAIChatClientMap") Map<AIChatClient, ChatClient> openAIChatClientMap,
                                     final @Qualifier("ollamaChatClientMap") Map<AIChatClient, ChatClient> ollamaChatClientMap) {
        this.openAIChatClientMap = openAIChatClientMap;
        this.ollamaChatClientMap = ollamaChatClientMap;
    }

    @Override
    public String applyPromptChainWorkflow(final AIChatClient aiChatClient,
                                           final ChainWorkflowRequestDTO chainWorkflowRequestDTO) {
        return PromptChainWorkflow.chain(
                chainWorkflowRequestDTO.getMessage(),
                getChatClient(aiChatClient),
                chainWorkflowRequestDTO.getPromptList()
        );
    }

    @Override
    public String applyRoutingWorkflow(final AIChatClient aiChatClient,
                                       final RoutingWorkflowRequestDTO routingWorkflowRequestDTO) {
        return RoutingWorkflow.route(
                routingWorkflowRequestDTO.getMessage(),
                routingWorkflowRequestDTO.getRoutes(),
                getChatClient(aiChatClient)
        );
    }

    @Override
    public List<String> applyParallelizationWorkflow(final AIChatClient aiChatClient,
                                                     final ParallelizationWorkflowDTO parallelizationWorkflowDTO) {
        return ParallelizationWorkflow.parallel(
                parallelizationWorkflowDTO.getMessage(),
                parallelizationWorkflowDTO.getInputs(),
                parallelizationWorkflowDTO.getNWorkers(),
                getChatClient(aiChatClient)
        );
    }

    @Override
    public FinalResponse applyOrchestratorWorkersWorkflow(final AIChatClient aiChatClient,
                                                          final OrchestratorWorkersDTO orchestratorWorkersDTO) {
        return OrchestratorWorkers.process(
                orchestratorWorkersDTO.getMessage(),
                getChatClient(aiChatClient),
                orchestratorWorkersDTO.getOrchestratorPrompt(),
                orchestratorWorkersDTO.getWorkerPrompt()
        );
    }

    private ChatClient getChatClient(final AIChatClient aiChatClient) {
        return switch (aiChatClient) {
            case OPENAI_DEFAULT -> openAIChatClientMap.get(OPENAI_DEFAULT);
            case OLLAMA_DEFAULT -> ollamaChatClientMap.get(OLLAMA_DEFAULT);

            case OPENAI_IN_MEMORY -> openAIChatClientMap.get(OPENAI_IN_MEMORY);
            case OLLAMA_IN_MEMORY -> ollamaChatClientMap.get(OLLAMA_IN_MEMORY);

            case OPENAI_MILITARY -> openAIChatClientMap.get(OPENAI_MILITARY);
            case OLLAMA_MILITARY -> ollamaChatClientMap.get(OLLAMA_MILITARY);
        };
    }
}
