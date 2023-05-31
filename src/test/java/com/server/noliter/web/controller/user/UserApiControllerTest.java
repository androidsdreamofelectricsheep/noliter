package com.server.noliter.web.controller.user;

import com.google.gson.Gson;
import com.server.noliter.domain.post.PostCategory;
import com.server.noliter.domain.user.Role;
import com.server.noliter.domain.user.User;
import com.server.noliter.service.post.PostService;
import com.server.noliter.service.post.dto.response.PostResponse;
import com.server.noliter.service.reply.ReplyService;
import com.server.noliter.service.reply.dto.response.ReplyResponse;
import com.server.noliter.service.user.UserService;
import com.server.noliter.service.user.dto.request.UserRequest;
import com.server.noliter.service.user.dto.response.UserResponse;
import com.server.noliter.web.controller.user.UserApiController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserApiController.class)
public class UserApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PostService postService;

    @MockBean
    private ReplyService replyService;

    @WithMockUser
    @Test
    void 회원_프로필_조회() throws Exception {
        // given
        User buildUser = User.builder()
                .username("조용필")
                .email("singerking@gmail.com")
                .profileImage("feelingofyou.jpg")
                .role(Role.USER)
                .build();

        UserResponse userResponse = new UserResponse((buildUser));

        given(userService.findById(1L)).willReturn(userResponse);

        // when, then
        mockMvc.perform(get("/api/v1/user/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.data.username").value(userResponse.getUsername())
                )
                .andExpect(
                        jsonPath("$.data.email").value(userResponse.getEmail())
                )
                .andExpect(
                        jsonPath("$.data.profileImage").value(userResponse.getProfileImage())
                )
                .andDo(print());

        verify(userService).findById(1L);
    }

    @WithMockUser
    @Test
    void 회원_프로필_수정() throws Exception {
        // given
        UserRequest buildUser = UserRequest.builder()
                .username("곽준빈")
                .build();

        Gson gson = new Gson();
        String requestBody = gson.toJson(buildUser);
        UserResponse userResponse = new UserResponse(1L, "곽준빈", "kwaktube@gmail.com", "kwak.jpg");

        given(userService.update(eq(1L), any(UserRequest.class)))
                .willReturn(userResponse);

        // when, then
        mockMvc.perform(put("/api/v1/user/1").contentType(MediaType.APPLICATION_JSON).content(requestBody).with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.data.username").value(userResponse.getUsername()))
                .andExpect(jsonPath("$.data.email").value(userResponse.getEmail()))
                .andExpect(jsonPath("$.data.profileImage").value(userResponse.getProfileImage()))
                .andDo(print());

        verify(userService).update(eq(1L), any(UserRequest.class));
    }

    @WithMockUser
    @Test
    void 회원_탈퇴() throws Exception {
        // given
        willDoNothing().given(userService).delete(1L);

        // when, then
        mockMvc.perform(delete("/api/v1/user/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());

        verify(userService).delete(1L);
    }

    @WithMockUser
    @Test
    void 회원_최근게시글3개_조회() throws Exception {
        // given
        PostResponse postResponse = new PostResponse(1L, "LIVING", "제목", "본문", "작성자", 4L, 100);
        // PostResponse postResponse = new PostResponse(1L, "LIVING",  "본문", "작성자", 4L, 100);


        List<PostResponse> recent3Posts = new ArrayList<>();

        recent3Posts.add(postResponse);

        given(postService.getTop3PostsByUserId(4L)).willReturn(recent3Posts);

        // when, then
        mockMvc.perform(get("/api/v1/user/4/post/top3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(postResponse.getId()))
                .andExpect(jsonPath("$.data[0].categoryName").value(postResponse.getCategoryName()))
                .andExpect(jsonPath("$.data[0].title").value(postResponse.getTitle()))
                .andExpect(jsonPath("$.data[0].content").value(postResponse.getContent()))
                .andExpect(jsonPath("$.data[0].writer").value(postResponse.getWriter()))
                .andExpect(jsonPath("$.data[0].writerId").value(postResponse.getWriterId()))
                .andExpect(jsonPath("$.data[0].views").value(postResponse.getViews()))
                .andDo(print());

        verify(postService).getTop3PostsByUserId(4L);
    }

    // 회원_최근게시글3개_조회()
    @WithMockUser
    @Test
    void 회원_최근댓글3개_조회() throws Exception {
        // given
        ReplyResponse replyResponse = new ReplyResponse(1L, "댓글","작성자",4L);

        List<ReplyResponse> recent3Replies = new ArrayList<>();
        recent3Replies.add(replyResponse);

        given(replyService.getTop3ByUserId(4L)).willReturn(recent3Replies);

        // when, then
        mockMvc.perform(get("/api/v1/user/4/reply/top3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(replyResponse.getId()))
                .andExpect(jsonPath("$.data[0].content").value(replyResponse.getContent()))
                .andExpect(jsonPath("$.data[0].writer").value(replyResponse.getWriter()))
                .andExpect(jsonPath("$.data[0].writerId").value(replyResponse.getWriterId()))
                .andDo(print());

        verify(replyService).getTop3ByUserId(4L);
    }

}
