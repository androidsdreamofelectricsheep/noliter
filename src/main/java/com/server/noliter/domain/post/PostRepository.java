package com.server.noliter.domain.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop3ByUserIdOrderByCreatedDateDesc(Long id);
    void deleteByUserId(Long id);
    Slice<Post> findByContentContainingIgnoreCaseOrderByCreatedDateDesc(String word, Pageable pageable);

    Slice<Post> findByCategoryAndContentContainingIgnoreCaseOrderByCreatedDateDesc(PostCategory category, String word, Pageable pageable);

    List<Post> findTop5ByCreatedDateGreaterThanOrderByViewsDesc(LocalDateTime twoDaysAgo);
}
