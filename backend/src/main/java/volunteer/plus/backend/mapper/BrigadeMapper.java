package volunteer.plus.backend.mapper;

import org.mapstruct.Mapper;
import volunteer.plus.backend.domain.Brigade;
import volunteer.plus.backend.dto.BrigadeDTO;

@Mapper
public interface BrigadeMapper {
    BrigadeDTO toDTO(Brigade brigade);
}
