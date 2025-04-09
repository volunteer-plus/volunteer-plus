package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = "newsFeed")
@Entity
@Table(name = "news_feed_attachment")
public class NewsFeedAttachment extends BaseEntity {

    @Column(name = "is_logo")
    private boolean logo;

    @Column(name = "filename")
    private String filename;

    @Column(name = "s3_link")
    private String s3Link;

    @ManyToOne
    @JoinColumn(name = "news_feed_id")
    private NewsFeed newsFeed;
}