package com.server.noliter.web.controller.reply;

import com.server.noliter.security.annotation.LoginUser;
import com.server.noliter.security.dto.SessionUser;
import com.server.noliter.service.reply.ReplyService;
import com.server.noliter.service.reply.dto.request.ReplyRequest;
import com.server.noliter.service.reply.dto.response.ReplyResponse;
import com.server.noliter.service.reply.dto.response.ReplySliceResponse;
import com.server.noliter.web.controller.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reply")
@RestController
public class ReplyApiController {
    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<Response<ReplyResponse>> save(@LoginUser SessionUser loginUser,
                                                        @RequestBody ReplyRequest request) {

        ReplyResponse response = replyService.save(loginUser.getId(), request);

        return new ResponseEntity<>(new Response<>(response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Response<ReplySliceResponse>> getReplies(@RequestParam("postId") Long postId,
                                                                   @RequestParam("page") String page,
                                                                   @RequestParam("size") String size) {
        PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));

        ReplySliceResponse response = replyService.findReplies(pageRequest, postId);

        return new ResponseEntity<>(new Response<>(response), HttpStatus.OK);
    }

    @PreAuthorize("@webSecurity.checkUserHasReplyAuthority(#id, #loginUser)")
    @PutMapping("/{id}")
    public ResponseEntity<Response<ReplyResponse>> update(@LoginUser SessionUser loginUser,
                                                          @PathVariable Long id,
                                                          @RequestBody ReplyRequest request) {
        ReplyResponse response = replyService.update(id, request);

        return new ResponseEntity<>(new Response<>(response), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("@webSecurity.checkUserHasReplyAuthority(#id, #loginUser)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> delete(@LoginUser SessionUser loginUser, @PathVariable Long id){
        replyService.delete(id);

        return new ResponseEntity<>(new Response<>(null), HttpStatus.OK);
    }
}
