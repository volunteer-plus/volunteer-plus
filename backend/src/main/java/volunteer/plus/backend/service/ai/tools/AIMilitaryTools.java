package volunteer.plus.backend.service.ai.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
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

@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
public class AIMilitaryTools {
    private final BrigadeCodesService brigadeCodesService;
    private final BrigadeService brigadeService;
    private final MilitaryPersonnelService militaryPersonnelService;

    @Tool(description = "Get Army all valid brigade regiment codes in the system based on given date criteria.")
    public List<BrigadeCodes> getArmyBrigadeValidCodes(@ToolParam final BrigadeCodesDateDTO codeDTO) {
        return brigadeCodesService.getCodesCreatedAt(codeDTO);
    }

    @Tool(description = "Get Army brigade details by name")
    public BrigadeDTO getArmyBrigadeDetailsByName(@ToolParam final BrigadeNameDTO nameDTO) {
        final BrigadeDTO brigade = brigadeService.getBrigade(nameDTO.getName());
        return brigade == null ? new BrigadeDTO() : brigade;
    }

    @Tool(description =
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
    public List<BrigadeDTO> createOrUpdateArmyBrigade(@ToolParam final BrigadeCreationRequestDTO creationRequestDTO) {
        return brigadeService.createOrUpdate(creationRequestDTO);
    }

    @Tool(description =
            """
            Create military personnel records for specified brigades.
            Accepts a MilitaryPersonnelCreationRequestDTO containing personnel data.
            Returns a map grouping the newly created MilitaryPersonnelDTO objects,
            typically organized by brigade or regiment code.
            """
    )
    public Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>> createMilitaryPersonnel(@ToolParam final MilitaryPersonnelCreationRequestDTO creationRequestDTO) {
        return militaryPersonnelService.createMilitaryPersonnel(creationRequestDTO);
    }

    @Tool(description =
            """
            Update existing military personnel records for specified brigades.
            Accepts a MilitaryPersonnelCreationRequestDTO with updated personnel information.
            Returns a list of updated MilitaryPersonnelDTO objects reflecting the changes.
            """
    )
    public List<BrigadeDTO.MilitaryPersonnelDTO> updateMilitaryPersonnel(@ToolParam final MilitaryPersonnelCreationRequestDTO creationRequestDTO) {
        return militaryPersonnelService.updateMilitaryPersonnel(creationRequestDTO);
    }
}
