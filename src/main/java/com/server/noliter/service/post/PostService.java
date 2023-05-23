package com.server.noliter.service.post;

import com.server.noliter.domain.post.PostCategory;
import com.server.noliter.domain.post.exception.PostErrorCode;
import com.server.noliter.domain.user.exception.UserErrorCode;
import com.server.noliter.global.exception.NoliterException;
import com.server.noliter.domain.post.Post;
import com.server.noliter.domain.post.PostRepository;
import com.server.noliter.domain.reply.ReplyRepository;
import com.server.noliter.domain.user.UserRepository;
import com.server.noliter.service.post.dto.request.PostRequest;
import com.server.noliter.service.post.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse save(Long userId, PostRequest postRequest) {
        Post buildPost = Post.builder()
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new NoliterException(UserErrorCode.USER_NOT_FOUND)))
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .category(PostCategory.valueOf(postRequest.getCategoryName()))
                .views(0)
                .build();

        Post newPost = postRepository.save(buildPost);

        return new PostResponse(newPost);
    }

    @Transactional
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoliterException(PostErrorCode.POST_NOT_FOUND));

        post.addViewCount();

        return new PostResponse(post);
    }

    @Transactional
    public PostResponse update(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoliterException(PostErrorCode.POST_NOT_FOUND));
        post.updatePost(postRequest.getTitle(), postRequest.getContent(), PostCategory.valueOf(postRequest.getCategoryName()));

        return new PostResponse(post);
    }

    @Transactional
    public void delete(Long id){
        replyRepository.deleteByPostId(id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoliterException(PostErrorCode.POST_NOT_FOUND));

        postRepository.delete(post);
    }

    public List<PostResponse> getTop5ByViews(){
        LocalDateTime twoDaysAgo = LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.of(0, 0, 0));

        List<Post> top5PostList = postRepository.findTop5ByCreatedDateGreaterThanOrderByViewsDesc(twoDaysAgo);

        return top5PostList.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public Long getWriterId(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoliterException(PostErrorCode.POST_NOT_FOUND));

        return post.getUser().getId();
    }
}
