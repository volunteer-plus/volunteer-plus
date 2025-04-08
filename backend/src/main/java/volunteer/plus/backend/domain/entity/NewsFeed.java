package volunteer.plus.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"comments", "author"})
@Entity
@Table(name = "news_feed")
public class NewsFeed extends BaseEntity {
    @Column
    private String subject;

    @Column
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "newsFeed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsFeedComment> comments = new ArrayList<>();

    public void addComment(final NewsFeedComment comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
        comment.setNewsFeed(this);
    }

    public void removeComment(final NewsFeedComment comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.remove(comment);
        comment.setNewsFeed(null);
    }
}
