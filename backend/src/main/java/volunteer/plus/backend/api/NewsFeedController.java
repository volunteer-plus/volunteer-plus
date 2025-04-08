package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.NewsFeedCommentDTO;
import volunteer.plus.backend.domain.dto.NewsFeedDTO;
import volunteer.plus.backend.service.general.NewsFeedService;


@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NewsFeedController {
    private final NewsFeedService newsFeedService;

    @GetMapping("/news-feeds")
    @Operation(description = "Retrieve news feeds with comments")
    public ResponseEntity<Page<NewsFeedDTO>> getNewsFeeds(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(newsFeedService.getNewsFeeds(PageRequest.of(page, size)));
    }

    @GetMapping("/news-feed")
    @Operation(description = "Retrieve news feed with comments")
    public ResponseEntity<NewsFeedDTO> getNewsFeed(@RequestParam final Long newsFeedId) {
        return ResponseEntity.ok(newsFeedService.getNewsFeed(newsFeedId));
    }

    @PostMapping("/news-feed/create-or-update")
    @Operation(description = "Create or update news feed")
    public ResponseEntity<NewsFeedDTO> createOrUpdateNewsFeed(@RequestParam final Long userId,
                                                              @Valid @RequestBody final NewsFeedDTO newsFeedDTO) {
        return ResponseEntity.ok(newsFeedService.createOrUpdateNewsFeed(userId, newsFeedDTO));
    }

    @PostMapping("/news-feed-comment/create-or-update")
    @Operation(description = "Create or update news feed comment")
    public ResponseEntity<NewsFeedDTO> createOrUpdateNewsFeedComment(@RequestParam final Long userId,
                                                                     @RequestParam final Long newsFeedId,
                                                                     @Valid @RequestBody final NewsFeedCommentDTO newsFeedCommentDTO) {
        return ResponseEntity.ok(newsFeedService.createOrUpdateNewsFeedComment(userId, newsFeedId, newsFeedCommentDTO));
    }

    @DeleteMapping("/news-feed/delete")
    @Operation(description = "Delete news feed")
    public void deleteNewsFeed(@RequestParam final Long userId,
                               @RequestParam final Long newsFeedId) {
        newsFeedService.deleteNewsFeed(userId, newsFeedId);
    }

    @DeleteMapping("/news-feed-comment/delete")
    @Operation(description = "Delete news feed comment")
    public ResponseEntity<NewsFeedDTO> deleteNewsFeedComment(@RequestParam final Long userId,
                                                             @RequestParam final Long newsFeedId,
                                                             @RequestParam final Long newsFeedCommentId) {
        return ResponseEntity.ok(newsFeedService.deleteNewsFeedComment(userId, newsFeedId, newsFeedCommentId));
    }
}
