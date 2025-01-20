package volunteer.plus.backend.service.general;

import volunteer.plus.backend.domain.dto.BrigadeCodesDateDTO;
import volunteer.plus.backend.domain.entity.BrigadeCodes;

import java.util.List;

public interface BrigadeCodesService {
    List<String> getCodes();
    List<BrigadeCodes> getCodesCreatedAt(BrigadeCodesDateDTO date);
}
