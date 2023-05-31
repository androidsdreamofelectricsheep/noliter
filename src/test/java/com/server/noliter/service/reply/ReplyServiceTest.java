package com.server.noliter.service.reply;

import com.server.noliter.domain.post.Post;
import com.server.noliter.domain.post.PostCategory;
import com.server.noliter.domain.post.PostRepository;
import com.server.noliter.domain.reply.Reply;
import com.server.noliter.domain.reply.ReplyRepository;
import com.server.noliter.domain.user.Role;
import com.server.noliter.domain.user.User;
import com.server.noliter.domain.user.UserRepository;
import com.server.noliter.service.reply.dto.request.ReplyRequest;
import com.server.noliter.service.reply.dto.response.ReplyResponse;
import com.server.noliter.service.reply.dto.response.ReplySliceResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
public class ReplyServiceTest {
    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private ReplyService replyService;

    @Test
    void 댓글_저장(){
        // given
        User user = new User(1L, "이름", "이메일", "이미지", Role.USER);

        Post post = new Post(2L, "글 제목", "본문", 13, PostCategory.SPORTS, user);

        ReplyRequest replyRequest = ReplyRequest.builder()
                .postId(2L)
                .content("1빠")
                .build();

        Reply replyBuilder = Reply.builder()
                .user(user)
                .post(post)
                .content(replyRequest.getContent())
                .build();

        Reply saveReply = new Reply(3L, replyRequest.getContent(), post, user);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(postRepository.findById(2L)).willReturn(Optional.of(post));
        given(replyRepository.save(eq(replyBuilder))).willReturn(saveReply);

        // when
        ReplyResponse replyResponse = replyService.save(1L, replyRequest);

        // then
        assertThat(replyResponse.getWriterId()).isEqualTo(1L);
        assertThat(replyResponse.getContent()).isEqualTo(replyRequest.getContent());
    }

    @Test
    void 댓글목록_조회(){
        // given
        PageRequest pageRequest = PageRequest.of(1, 3);
        List<Reply> replies = new ArrayList<>();
        Reply reply = new Reply(1L, "1빠", new Post(), new User());
        replies.add(reply);
        SliceImpl<Reply> slice = new SliceImpl<>(replies, Pageable.ofSize(3), true);
        given(replyRepository.findByPostIdOrderByCreatedDateDesc(eq(1L), eq(pageRequest))).willReturn(slice);

        // when
        ReplySliceResponse replySliceResponse = replyService.findReplies(pageRequest, 1L);

        // then
        assertThat(replySliceResponse.getReplies()).hasSize(1);
        assertThat(replySliceResponse.isHasNext()).isEqualTo(true);
    }

    @Test
    void 댓글_수정(){
        // given
        ReplyRequest replyRequest = ReplyRequest.builder()
                .postId(1L)
                .content("2빠")
                .build();

        Reply reply = new Reply(2L, "1빠", new Post(), new User());

        given(replyRepository.findById(1L)).willReturn(Optional.of(reply));

        // when
        ReplyResponse replyResponse = replyService.update(1L, replyRequest);

        // then
        assertThat(replyResponse.getId()).isEqualTo(reply.getId());
        assertThat(replyResponse.getContent()).isEqualTo(reply.getContent());
    }

    @Test
    void 댓글_삭제(){
        // given
        Reply reply = new Reply(1L, "멋지네요!", new Post(), new User());

        given(replyRepository.findById(1L)).willReturn(Optional.of(reply));

        // when, then
        replyService.delete(1L);
    }

}
