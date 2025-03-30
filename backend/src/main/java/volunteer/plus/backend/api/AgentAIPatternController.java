package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.ai.agent.ChainWorkflowRequestDTO;
import volunteer.plus.backend.domain.dto.ai.agent.RoutingWorkflowRequestDTO;
import volunteer.plus.backend.domain.enums.AIChatClient;
import volunteer.plus.backend.service.ai.AgentAIPatternService;


@Validated
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AgentAIPatternController {
    private final AgentAIPatternService agentAIPatternService;

    @PostMapping("/agent/prompt-chain-workflow")
    @Operation(description = "Prompt chain workflow")
    public ResponseEntity<String> applyPromptChainWorkflow(@RequestParam final AIChatClient aiChatClient,
                                                           @RequestBody @Valid final ChainWorkflowRequestDTO chainWorkflowRequestDTO) {
        return ResponseEntity.ok(agentAIPatternService.applyPromptChainWorkflow(aiChatClient, chainWorkflowRequestDTO));
    }

    @PostMapping("/agent/routing-workflow")
    @Operation(description = "Routing workflow")
    public ResponseEntity<String> applyRoutingWorkflow(@RequestParam final AIChatClient aiChatClient,
                                                       @RequestBody @Valid final RoutingWorkflowRequestDTO routingWorkflowRequestDTO) {
        return ResponseEntity.ok(agentAIPatternService.applyRoutingWorkflow(aiChatClient, routingWorkflowRequestDTO));
    }
}
