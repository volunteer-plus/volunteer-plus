package volunteer.plus.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.dto.BrigadeDTO;
import volunteer.plus.backend.service.BrigadeService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrigadeServiceImpl implements BrigadeService {
    @Override
    public List<BrigadeDTO> getAll() {
        return List.of();
    }
}
