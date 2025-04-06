package volunteer.plus.backend.service.ai.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.ai.agent.*;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.service.ai.AgentAIPatternService;
import volunteer.plus.backend.service.ai.tools.impl.*;
import volunteer.plus.backend.util.AIClientProviderUtil;

import java.util.List;

@Slf4j
@Service
public class AgentAIPatternServiceImpl implements AgentAIPatternService {
    private final AIClientProviderUtil aiClientProviderUtil;

    public AgentAIPatternServiceImpl(AIClientProviderUtil aiClientProviderUtil) {
        this.aiClientProviderUtil = aiClientProviderUtil;
    }

    @Override
    public String applyPromptChainWorkflow(final AIChatClient aiChatClient,
                                           final ChainWorkflowRequestDTO chainWorkflowRequestDTO) {
        return PromptChainWorkflow.chain(
                chainWorkflowRequestDTO.getMessage(),
                aiClientProviderUtil.getChatClient(aiChatClient),
                chainWorkflowRequestDTO.getPromptList()
        );
    }

    @Override
    public String applyRoutingWorkflow(final AIChatClient aiChatClient,
                                       final RoutingWorkflowRequestDTO routingWorkflowRequestDTO) {
        return RoutingWorkflow.route(
                routingWorkflowRequestDTO.getMessage(),
                routingWorkflowRequestDTO.getRoutes(),
                aiClientProviderUtil.getChatClient(aiChatClient)
        );
    }

    @Override
    public List<String> applyParallelizationWorkflow(final AIChatClient aiChatClient,
                                                     final ParallelizationWorkflowRequestDTO parallelizationWorkflowRequestDTO) {
        return ParallelizationWorkflow.parallel(
                parallelizationWorkflowRequestDTO.getMessage(),
                parallelizationWorkflowRequestDTO.getInputs(),
                parallelizationWorkflowRequestDTO.getNWorkers(),
                aiClientProviderUtil.getChatClient(aiChatClient)
        );
    }

    @Override
    public FinalResponse applyOrchestratorWorkersWorkflow(final AIChatClient aiChatClient,
                                                          final OrchestratorWorkersRequestDTO orchestratorWorkersRequestDTO) {
        return OrchestratorWorkers.process(
                orchestratorWorkersRequestDTO.getMessage(),
                aiClientProviderUtil.getChatClient(aiChatClient),
                orchestratorWorkersRequestDTO.getOrchestratorPrompt(),
                orchestratorWorkersRequestDTO.getWorkerPrompt()
        );
    }

    @Override
    public RefinedResponse applyEvaluationOptimizerWorkflow(final AIChatClient aiChatClient,
                                                            final EvaluationOptimizerRequestDTO evaluationOptimizerRequestDTO) {
        return EvaluationOptimizer.loop(
                evaluationOptimizerRequestDTO.getMessage(),
                aiClientProviderUtil.getChatClient(aiChatClient),
                evaluationOptimizerRequestDTO.getGeneratorPrompt(),
                evaluationOptimizerRequestDTO.getEvaluatorPrompt()
        );
    }
}
