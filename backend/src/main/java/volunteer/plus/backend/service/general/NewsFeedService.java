package volunteer.plus.backend.service.general;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import volunteer.plus.backend.domain.dto.NewsFeedCommentDTO;
import volunteer.plus.backend.domain.dto.NewsFeedDTO;

public interface NewsFeedService {
    Page<NewsFeedDTO> getNewsFeeds(Pageable pageable);

    NewsFeedDTO getNewsFeed(Long newsFeedId);

    NewsFeedDTO createOrUpdateNewsFeed(Long userId, NewsFeedDTO newsFeedDTO);

    NewsFeedDTO createOrUpdateNewsFeedComment(Long userId, Long newsFeedId, NewsFeedCommentDTO newsFeedCommentDTO);

    void deleteNewsFeed(Long userId, Long newsFeedId);

    NewsFeedDTO deleteNewsFeedComment(Long userId, Long newsFeedId, Long newsFeedCommentId);
}
