package com.server.noliter.service.user;

import com.server.noliter.domain.post.PostRepository;
import com.server.noliter.domain.reply.ReplyRepository;
import com.server.noliter.domain.user.Role;
import com.server.noliter.domain.user.User;
import com.server.noliter.domain.user.UserRepository;
import com.server.noliter.service.user.dto.request.UserRequest;
import com.server.noliter.service.user.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 사용자_조회() {
        User user = new User(8L, "고길동", "ko@dooli.com", "image.jpg", Role.USER);

        given(userRepository.findById(8L)).willReturn(Optional.of(user));

        UserResponse userResponse = userService.findById(8L);

        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getUsername()).isEqualTo(user.getUsername());
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponse.getProfileImage()).isEqualTo(user.getProfileImage());
    }

    @Test
    void 사용자_수정() {
        User user = new User(5L, "방국봉", "gas@google.com", "image.jpg", Role.USER);
        UserRequest userRequest = new UserRequest("방현수");

        given(userRepository.findById(5L)).willReturn(Optional.of(user));

        UserResponse userResponse = userService.update(5L, userRequest);

        assertThat(userResponse.getId()).isEqualTo(5L);
        assertThat(userResponse.getUsername()).isEqualTo(userRequest.getUsername());
    }

    @Test
    void 사용자_삭제(){
        User user = new User(8L, "고길동", "ko@dooli.com", "image.jpg", Role.USER);

        willDoNothing().given(replyRepository).deleteByUserId(8L);
        willDoNothing().given(postRepository).deleteByUserId(8L);

        given(userRepository.findById(8L)).willReturn(Optional.of(user));

        willDoNothing().given(userRepository).delete(user);

        userService.delete(8L);
    }

}
