package volunteer.plus.backend.service.ai;

import org.springframework.web.multipart.MultipartFile;

public interface DataInjectionService {
    void injectData(MultipartFile multipartFile);
}
