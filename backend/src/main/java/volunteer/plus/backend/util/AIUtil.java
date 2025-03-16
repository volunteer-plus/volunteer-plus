package volunteer.plus.backend.util;

import org.springframework.ai.model.Media;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class AIUtil {
    private AIUtil() {
    }

    public static List<Media> getAIMediaList(final List<MultipartFile> multipartFiles) {
        return multipartFiles == null ? new ArrayList<>() :
                multipartFiles.stream()
                        .filter(el -> el.getContentType() != null && el.getOriginalFilename() != null)
                        .map(el -> new Media(MimeTypeUtils.parseMimeType(el.getContentType()), el.getResource()))
                        .toList();
    }
}
