package com.server.noliter.service.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {

    private String title;
    private String content;
    private String categoryName;

    @Builder
    public PostRequest(String title, String content, String categoryName){
    // public PostRequest(String content, String categoryName){
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
    }
}
