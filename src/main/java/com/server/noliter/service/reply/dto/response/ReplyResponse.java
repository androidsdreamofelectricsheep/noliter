package com.server.noliter.service.reply.dto.response;

import com.server.noliter.domain.reply.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReplyResponse {
    private Long id;
    private String content;
    private String writer;
    private Long writerId;

    public ReplyResponse(Reply reply){
        id = reply.getId();
        content = reply.getContent();
        writer = reply.getUser().getUsername();
        writerId = reply.getUser().getId();
    }
}
