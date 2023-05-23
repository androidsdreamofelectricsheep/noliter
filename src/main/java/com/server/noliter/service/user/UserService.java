package com.server.noliter.service.user;

import com.server.noliter.domain.post.PostRepository;
import com.server.noliter.domain.reply.ReplyRepository;
import com.server.noliter.domain.user.User;
import com.server.noliter.domain.user.UserRepository;
import com.server.noliter.domain.user.exception.UserErrorCode;
import com.server.noliter.global.exception.NoliterException;
import com.server.noliter.service.user.dto.request.UserRequest;
import com.server.noliter.service.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final ReplyRepository replyRepository;

    public UserResponse findById(Long id){
        User user = userRepository.findById(id).
                orElseThrow(() -> new NoliterException(UserErrorCode.USER_NOT_FOUND));

        return new UserResponse(user);
    }

    @Transactional
    public UserResponse update(Long id, UserRequest userRequest){
        User user = userRepository.findById(id).
                orElseThrow(() -> new NoliterException(UserErrorCode.USER_NOT_FOUND));

        user.updateUser(userRequest.getUsername());

        return new UserResponse(user);
    }

    @Transactional
    public void delete(Long id){
        replyRepository.deleteByUserId(id);
        postRepository.deleteByUserId(id);
        userRepository.delete(userRepository.findById(id)
                .orElseThrow(() -> new NoliterException(UserErrorCode.USER_NOT_FOUND)));
    }
}
