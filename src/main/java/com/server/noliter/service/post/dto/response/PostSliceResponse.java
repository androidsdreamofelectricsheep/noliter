package com.server.noliter.service.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostSliceResponse {
    List<PostResponse> posts;

    private boolean hasNext;
}
