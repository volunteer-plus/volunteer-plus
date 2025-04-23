package volunteer.plus.backend.service.alerts;

import volunteer.plus.backend.domain.dto.AlertRegionModelDTO;

import java.util.List;

public interface AlertService {
    List<AlertRegionModelDTO> getAlerts();
}
