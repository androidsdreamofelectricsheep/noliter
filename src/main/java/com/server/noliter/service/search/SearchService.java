package com.server.noliter.service.search;

import com.server.noliter.domain.post.Post;
import com.server.noliter.domain.post.PostCategory;
import com.server.noliter.domain.post.PostRepository;
import com.server.noliter.service.post.dto.response.PostResponse;
import com.server.noliter.service.post.dto.response.PostSliceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {
    private final PostRepository postRepository;

    public PostSliceResponse getSearchedPosts(PageRequest pageRequest, String categoryName, String word) {
        Slice<Post> slice;

        if (categoryName.equals("ALL")) {
            slice = postRepository.findByTitleContainingIgnoreCaseOrderByCreatedDateDesc(word, pageRequest);
        } else {
            slice = postRepository.findByCategoryAndTitleContainingIgnoreCaseOrderByCreatedDateDesc(PostCategory.valueOf(categoryName), word, pageRequest);
        }

        List<PostResponse> content = slice.getContent().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

        return new PostSliceResponse(content, slice.hasNext());
    }
}
