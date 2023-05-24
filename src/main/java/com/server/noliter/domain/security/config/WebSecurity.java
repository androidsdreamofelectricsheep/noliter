package com.server.noliter.domain.security.config;

import com.server.noliter.domain.security.dto.SessionUser;
import com.server.noliter.service.post.PostService;
import com.server.noliter.service.reply.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("webSecurity")
public class WebSecurity {
    private final PostService postService;
    private final ReplyService replyService;

    public boolean checkUserAuthority(Long userId, SessionUser loginUser){
        log.info("USER-NO:{}, LOGIN-USER:{}", userId, loginUser);

        return userId.equals(loginUser.getId());
    }

    public boolean checkUserHasPostAuthority(Long postId, SessionUser loginUser){
        Long writerId = postService.getWriterId(postId);
        log.info("WRITER-NO:{}, LOGIN-USER:{}", postId, loginUser);

        return writerId.equals(loginUser.getId());
    }

    public boolean checkUserHasReplyAuthority(Long replyId, SessionUser loginUser){
        Long writerId = replyService.getReplyWriter(replyId);
        log.info("WRITER-NO:{}, LOGIN-USER:{}", writerId, loginUser);

        return writerId.equals(loginUser.getId());
    }
}
