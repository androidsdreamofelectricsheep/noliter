package com.server.noliter.service.post.dto.response;

import com.server.noliter.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostResponse {
    private Long id;
    private String categoryName;
    private String title;
    private String content;
    private String writer;
    private Long writerId;
    private Integer views;

    public PostResponse(Post post){
        this.id = post.getId();
        this.categoryName = post.getCategory().getLangKor();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getUser().getUsername();
        this.writerId = post.getUser().getId();
        this.views = post.getViews();
    }
}
