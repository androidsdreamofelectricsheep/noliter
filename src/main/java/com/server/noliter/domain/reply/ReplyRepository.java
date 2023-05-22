package com.server.noliter.domain.reply;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Slice<Reply> findByQuestionIdOrderByCreatedDateDesc(Long id, Pageable pageable);

    List<Reply> findTop3ByUserIdOrderByCreatedDateDesc(Long no);

    void deleteByReplyId(Long id);

    void deleteByUserId(Long id);
}
