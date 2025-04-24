package volunteer.plus.backend.service.alerts;

import volunteer.plus.backend.domain.dto.AlertDurationResponseDTO;
import volunteer.plus.backend.domain.dto.AlertRegionModelDTO;
import volunteer.plus.backend.domain.dto.RegionAlarmsHistoryDTO;
import volunteer.plus.backend.domain.dto.RegionsViewModelDTO;

import java.time.LocalDate;
import java.util.List;

public interface AlarmService {
    List<AlertRegionModelDTO> getAlerts(String regionId);

    RegionsViewModelDTO getRegions();

    List<AlertDurationResponseDTO> getDateHistoryAlerts(LocalDate date);

    List<RegionAlarmsHistoryDTO> getRegionHistoryAlerts(String regionId);

}
