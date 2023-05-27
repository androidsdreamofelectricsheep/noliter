package com.server.noliter.service.reply;

import com.server.noliter.domain.post.PostRepository;
import com.server.noliter.domain.reply.Reply;
import com.server.noliter.domain.reply.ReplyRepository;
import com.server.noliter.domain.reply.exception.ReplyErrorCode;
import com.server.noliter.domain.user.UserRepository;
import com.server.noliter.global.exception.NoliterException;
import com.server.noliter.service.reply.dto.request.ReplyRequest;
import com.server.noliter.service.reply.dto.response.ReplyResponse;
import com.server.noliter.service.reply.dto.response.ReplySliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public ReplyResponse save(Long userId, ReplyRequest request){
        Reply replyBuilder = Reply.builder()
                .user(userRepository.findById(userId)
                        // .orElseThrow(() -> new NoliterException(UserErrorCode.USER_NOT_FOUND)))
                        .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다.")))
                .post(postRepository.findById(request.getPostId())
                        // .orElseThrow(() -> new NoliterException(PostErrorCode.POST_NOT_FOUND)))
                        .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다.")))
                .content(request.getContent())
                .build();

        Reply reply = replyRepository.save(replyBuilder);

        return new ReplyResponse(reply);
    }

    public ReplySliceResponse findReplies(PageRequest pageRequest, Long postId){
        Slice<Reply> slice = replyRepository.findByPostIdOrderByCreatedDateDesc(postId, pageRequest);

        List<ReplyResponse> contents = slice.getContent().stream()
                .map(ReplyResponse::new).collect(Collectors.toList());

        return new ReplySliceResponse(contents, slice.hasNext());
    }

    public List<ReplyResponse> getTop3ByUserId(Long id){
        List<Reply> top3Replies = replyRepository.findTop3ByUserIdOrderByCreatedDateDesc(id);

        return top3Replies.stream()
                .map(ReplyResponse::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public ReplyResponse update(Long id, ReplyRequest request){
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new NoliterException(ReplyErrorCode.REPLY_NOT_FOUND));

        reply.updateReply(request.getContent());

        return new ReplyResponse(reply);
    }

    public void delete(Long id){
        replyRepository.delete(replyRepository.findById(id)
                .orElseThrow(() -> new NoliterException(ReplyErrorCode.REPLY_NOT_FOUND)));
    }

    public Long getReplyWriter(Long id){
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new NoliterException(ReplyErrorCode.REPLY_NOT_FOUND));

        return reply.getUser().getId();
    }
}
