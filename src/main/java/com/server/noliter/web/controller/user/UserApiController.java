package com.server.noliter.web.controller.user;

import com.server.noliter.security.annotation.LoginUser;
import com.server.noliter.security.dto.SessionUser;
import com.server.noliter.service.post.PostService;
import com.server.noliter.service.post.dto.response.PostResponse;
import com.server.noliter.service.reply.ReplyService;
import com.server.noliter.service.reply.dto.response.ReplyResponse;
import com.server.noliter.service.user.UserService;
import com.server.noliter.service.user.dto.request.UserRequest;
import com.server.noliter.service.user.dto.response.UserResponse;
import com.server.noliter.web.controller.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserApiController {
    private final UserService userService;
    private final PostService postService;
    private final ReplyService replyService;

    @GetMapping
    public ResponseEntity<Response<SessionUser>> getLoginUser(@LoginUser SessionUser loginUser) {
        if (loginUser != null) {
            log.info("[GET] LOGIN-USER : {}", loginUser);
            return new ResponseEntity<>(new Response<SessionUser>(loginUser), HttpStatus.OK);
        } else {
            throw new AccessDeniedException("데이터 접근 권한이 없습니다.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UserResponse>> getUserProfile(@PathVariable Long id) {
        UserResponse response = userService.findById(id);

        return new ResponseEntity<>(new Response<>(response), HttpStatus.OK);
    }

    @PreAuthorize("@webSecurity.checkUserAuthority(#id, #loginUser)")
    @PutMapping("/{id}")
    public ResponseEntity<Response<UserResponse>> update(@PathVariable Long id, @RequestBody UserRequest request, @LoginUser SessionUser loginUser) {
        UserResponse response = userService.update(id, request);

        return new ResponseEntity<>(new Response<>(response), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("@webSecurity.checkUserAuthority(#id, #loginUser)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> delete(@LoginUser SessionUser loginUser, @PathVariable Long id) {
        userService.delete(id);

        return new ResponseEntity<>(new Response<>(null), HttpStatus.OK);
    }

    @GetMapping("/{id}/post/top3")
    public ResponseEntity<Response<List<PostResponse>>> getTop3Posts(@PathVariable Long id) {
        List<PostResponse> responses = postService.getTop3PostsByUserId(id);

        return new ResponseEntity<>(new Response<>(responses), HttpStatus.OK);
    }

    @GetMapping("/{id}/reply/top3")
    public ResponseEntity<Response<List<ReplyResponse>>> getTop3Replies(@PathVariable Long id) {
        List<ReplyResponse> responses = replyService.getTop3ByUserId(id);

        return new ResponseEntity<>(new Response<>(responses), HttpStatus.OK);
    }
}
