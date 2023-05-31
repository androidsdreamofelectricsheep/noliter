package com.server.noliter.service.post;

import com.server.noliter.domain.post.Post;
import com.server.noliter.domain.post.PostCategory;
import com.server.noliter.domain.post.PostRepository;
import com.server.noliter.domain.reply.Reply;
import com.server.noliter.domain.reply.ReplyRepository;
import com.server.noliter.domain.user.Role;
import com.server.noliter.domain.user.User;
import com.server.noliter.domain.user.UserRepository;
import com.server.noliter.service.post.dto.request.PostRequest;
import com.server.noliter.service.post.dto.response.PostResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void 게시글_저장() {

        PostRequest postRequest = PostRequest.builder()
                .title("박대깁니다.")
                .content("네 서울 시내에는 지금 이 시각 현재 눈앞이 제대로 보이지 않을 정도의 많은 눈이 내리고 있습니다.")
                .categoryName("SOCIETY")
                .build();

        User user = new User(1L, "박대기", "waiting@kbs.co.kr", "not_snowman.jpg", Role.USER);

        Post buildPost = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .category(PostCategory.valueOf(postRequest.getCategoryName()))
                .user(user)
                .views(0)
                .build();

        Post savePost = new Post(1L, buildPost.getTitle(), buildPost.getContent(), buildPost.getViews(), buildPost.getCategory(), user);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(postRepository.save(any(Post.class))).willReturn(savePost);

        PostResponse postResponse = postService.save(1L, postRequest);

        assertThat(postResponse.getContent()).isEqualTo(postRequest.getContent());
        assertThat(postResponse.getCategoryName()).isEqualTo(PostCategory.valueOf(postRequest.getCategoryName()).getLangKor());
    }

    @Test
    void 게시글_조회() {
        User user = new User(1L, "정대만", "taiwan@slamdunk.com", "sg.jpg", Role.USER);

        Post post = new Post(1L, "그래 난 정대만", "포기를 모르는 남자지...", 13, PostCategory.SPORTS, user);

        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        PostResponse postResponse = postService.findById(1L);

        assertThat(postResponse.getTitle()).isEqualTo(post.getTitle());
        assertThat(postResponse.getContent()).isEqualTo(post.getContent());
        assertThat(postResponse.getWriter()).isEqualTo(post.getUser().getUsername());
        assertThat(postResponse.getCategoryName()).isEqualTo(post.getCategory().getLangKor());
        assertThat(postResponse.getViews()).isEqualTo(14);
    }

    @Test
    void 게시글_수정() {
        User user = new User(1L, "이름", "이메일", "이미지", Role.USER);

        Post post = new Post(1L, "글 제목", "본문", 13, PostCategory.SPORTS, user);

        PostRequest postRequest = PostRequest.builder()
                .title("수정된 글 제목")
                .content("수정된 글")
                .categoryName("ETC")
                .build();

        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        PostResponse postResponse = postService.update(1L, postRequest);

        assertThat(post.getTitle()).isEqualTo(postResponse.getTitle());
        assertThat(post.getContent()).isEqualTo(postResponse.getContent());
        assertThat(post.getCategory().getLangKor()).isEqualTo(postResponse.getCategoryName());
    }

    @Test
    void 게시글_삭제() {
        User user = new User(1L, "이름", "이메일", "이미지", Role.USER);

        Post post = new Post(1L, "글 제목", "본문", 13, PostCategory.SPORTS, user);

        Reply reply = new Reply(1L, "댓글", post, user);

        willDoNothing().given(replyRepository).deleteByPostId(1L);
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        willDoNothing().given(postRepository).delete(post);

        postService.delete(1L);
    }

}
