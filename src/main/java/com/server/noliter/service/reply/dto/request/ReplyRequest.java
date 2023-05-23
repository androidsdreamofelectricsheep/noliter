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
    public ReplyRequest(Long id, String content){
        this.postId = id;
        this.content = content;
    }
}
