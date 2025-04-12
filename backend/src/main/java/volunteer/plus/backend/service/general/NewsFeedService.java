package volunteer.plus.backend.service.general;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.NewsFeedCommentDTO;
import volunteer.plus.backend.domain.dto.NewsFeedDTO;
import volunteer.plus.backend.domain.enums.AIChatClient;

public interface NewsFeedService {
    Page<NewsFeedDTO> getNewsFeeds(Pageable pageable);

    NewsFeedDTO getNewsFeed(Long newsFeedId);

    NewsFeedDTO createOrUpdateNewsFeed(Long userId, NewsFeedDTO newsFeedDTO);

    NewsFeedDTO selectNewsFeedLogo(Long attachmentId, boolean isLogo);

    NewsFeedDTO createOrUpdateNewsFeedComment(Long userId, Long newsFeedId, NewsFeedCommentDTO newsFeedCommentDTO);

    void deleteNewsFeed(Long newsFeedId);

    NewsFeedDTO deleteNewsFeedComment(Long userId, Long newsFeedId, Long newsFeedCommentId);

    NewsFeedDTO addAttachment(Long newsFeedId, boolean isLogo, MultipartFile file);

    NewsFeedDTO removeAttachment(Long newsFeedId, Long attachmentId);

    ResponseEntity<byte[]> downloadAttachment(Long attachmentId);

    void generateAINewsFeed(AIChatClient aiChatClient);
}
