package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"newsFeed", "author"})
@Entity
@Table(name = "news_feed_comment")
public class NewsFeedComment extends BaseEntity {
    @Column
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "news_feed_id")
    private NewsFeed newsFeed;
}