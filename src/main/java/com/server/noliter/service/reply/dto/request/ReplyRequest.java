package com.server.noliter.service.reply.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyRequest {
    private Long postId;
    private String content;
    @Builder
    public ReplyRequest(Long postId, String content){
        this.postId = postId;
        this.content = content;
    }
}
