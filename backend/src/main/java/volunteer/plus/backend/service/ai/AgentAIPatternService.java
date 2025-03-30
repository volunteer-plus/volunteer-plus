package volunteer.plus.backend.service.ai;

import volunteer.plus.backend.domain.dto.ai.agent.ChainWorkflowRequestDTO;
import volunteer.plus.backend.domain.dto.ai.agent.RoutingWorkflowRequestDTO;
import volunteer.plus.backend.domain.enums.AIChatClient;

public interface AgentAIPatternService {
    String applyPromptChainWorkflow(AIChatClient aiChatClient, ChainWorkflowRequestDTO chainWorkflowRequestDTO);

    String applyRoutingWorkflow(AIChatClient aiChatClient, RoutingWorkflowRequestDTO routingWorkflowRequestDTO);
}
