package volunteer.plus.backend.config.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import volunteer.plus.backend.domain.dto.BrigadeCreationRequestDTO;
import volunteer.plus.backend.domain.dto.BrigadeDTO;
import volunteer.plus.backend.domain.dto.BrigadeNameDTO;
import volunteer.plus.backend.service.general.BrigadeService;

import java.util.List;
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

    @Bean
    @Description(
            """
            Provides a function to create or update a list of army brigades based on the given
            {@link BrigadeCreationRequestDTO}. Each brigade in the request contains properties such as:
            - regimentCode (mandatory): Unique identifier for the regiment.
            - name: Official name of the brigade.
            - branch: Military branch (e.g., Army, Navy).
            - role: Role or function within the military.
            - partOf: The larger unit or formation the brigade belongs to.
            - websiteLink: URL to the brigade's official website.
            - currentCommander: Name or identifier of the current brigade commander.
            - description: Additional details about the brigade.
            """
    )
    public Function<BrigadeCreationRequestDTO, List<BrigadeDTO>> createOrUpdateArmyBrigade() {
        return brigadeService::createOrUpdate;
    }

}
