package com.server.noliter.service.reply.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReplySliceResponse {
    private List<ReplyResponse> replies;
    private boolean hasNext;
}
