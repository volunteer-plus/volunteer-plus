package volunteer.plus.backend.service.ai;

import volunteer.plus.backend.domain.dto.ChainWorkflowRequestDTO;
import volunteer.plus.backend.domain.enums.AIChatClient;

public interface AgentAIPatternService {
    String applyPromptChainWorkflow(AIChatClient aiChatClient, ChainWorkflowRequestDTO chainWorkflowRequestDTO);
}
