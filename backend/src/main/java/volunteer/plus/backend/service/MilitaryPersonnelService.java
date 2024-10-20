package volunteer.plus.backend.service;

import volunteer.plus.backend.domain.dto.BrigadeDTO;
import volunteer.plus.backend.domain.dto.MilitaryPersonnelCreationRequestDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MilitaryPersonnelService {
    Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>> getMilitaryPersonnel(Set<String> regimentCodes);

    Map<String, List<BrigadeDTO.MilitaryPersonnelDTO>> createMilitaryPersonnel(MilitaryPersonnelCreationRequestDTO militaryPersonnelCreationRequestDTO);

    void deleteMilitaryPersonnel(Set<Long> ids);
}
