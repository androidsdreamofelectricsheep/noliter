package com.server.noliter.domain.reply;


import com.server.noliter.domain.BaseTimeEntity;
import com.server.noliter.domain.post.Post;
import com.server.noliter.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class Reply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Reply(String content, Post post, User user){
        this.content = content;
        this.post = post;
        this.user = user;
    }

    public void updateReply(String content){
        this.content = content;
    }
}
