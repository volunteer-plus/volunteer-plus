package volunteer.plus.backend.config.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import volunteer.plus.backend.domain.dto.BrigadeDTO;
import volunteer.plus.backend.domain.dto.BrigadeNameDTO;
import volunteer.plus.backend.service.general.BrigadeService;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class FunctionalAIConfiguration {
    private final BrigadeService brigadeService;

    @Bean
    @Description("Get Army brigade details by name")
    public Function<BrigadeNameDTO, BrigadeDTO> getArmyBrigadeDetailsByName() {
        return brigadeName -> {
            final BrigadeDTO brigade = brigadeService.getBrigade(brigadeName.getName());
            return brigade == null ? new BrigadeDTO() : brigade;
        };
    }
}
