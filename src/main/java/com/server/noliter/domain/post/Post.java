package com.server.noliter.domain.post;

import com.server.noliter.domain.BaseTimeEntity;
import com.server.noliter.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Lob
    private String content;

    @ColumnDefault("0")
    private Integer views;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(String title, String content, Integer views, PostCategory category, User user) {
        this.title = title;
        this.content = content;
        this.views = views;
        this.category = category;
        this.user = user;
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addViewCount() {
        this.views += 1;
    }
}
