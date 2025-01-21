package volunteer.plus.backend.config.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import volunteer.plus.backend.domain.dto.BrigadeCodesDateDTO;
import volunteer.plus.backend.domain.dto.BrigadeCreationRequestDTO;
import volunteer.plus.backend.domain.dto.BrigadeDTO;
import volunteer.plus.backend.domain.dto.BrigadeNameDTO;
import volunteer.plus.backend.domain.dto.MilitaryPersonnelCreationRequestDTO;
import volunteer.plus.backend.domain.entity.BrigadeCodes;
import volunteer.plus.backend.service.general.BrigadeCodesService;
import volunteer.plus.backend.service.general.BrigadeService;
import volunteer.plus.backend.service.general.MilitaryPersonnelService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class FunctionalAIConfiguration {
    private final BrigadeCodesService brigadeCodesService;
    private final BrigadeService brigadeService;
    private final MilitaryPersonnelService militaryPersonnelService;

    @Bean
    @Description("Get Army all valid brigade regiment codes in the system based on given date criteria.")
    public Function<BrigadeCodesDateDTO, List<BrigadeCodes>> getArmyBrigadeValidCodes() {
        return brigadeCodesService::getCodesCreatedAt;
    }

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


    @Bean
    @Description(
            """
            Create military personnel records for specified brigades.
            Accepts a MilitaryPersonnelCreationRequestDTO containing personnel data.
            Returns a map grouping the newly created MilitaryPersonnelDTO objects,
            typically organized by brigade or regiment code.
            """
    )
    public Function<MilitaryPersonnelCreationRequestDTO, Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>>> createMilitaryPersonnel() {
        return militaryPersonnelService::createMilitaryPersonnel;
    }

    @Bean
    @Description(
            """
            Update existing military personnel records for specified brigades.
            Accepts a MilitaryPersonnelCreationRequestDTO with updated personnel information.
            Returns a list of updated MilitaryPersonnelDTO objects reflecting the changes.
            """
    )
    public Function<MilitaryPersonnelCreationRequestDTO, List<BrigadeDTO.MilitaryPersonnelDTO>> updateMilitaryPersonnel() {
        return militaryPersonnelService::updateMilitaryPersonnel;
    }
}
