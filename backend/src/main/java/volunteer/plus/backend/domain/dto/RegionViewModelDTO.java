package volunteer.plus.backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import volunteer.plus.backend.domain.enums.RegionType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionViewModelDTO {
    private String regionId;
    private String regionName;
    private RegionType regionType;
    private List<RegionViewModelDTO> regionChildIds;
}
