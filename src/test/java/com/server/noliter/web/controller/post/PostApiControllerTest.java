package com.server.noliter.web.controller.post;

import com.google.gson.Gson;
import com.server.noliter.domain.post.Post;
import com.server.noliter.domain.post.PostCategory;
import com.server.noliter.domain.user.Role;
import com.server.noliter.domain.user.User;
import com.server.noliter.domain.user.UserRepository;
import com.server.noliter.service.post.PostService;
import com.server.noliter.service.post.dto.request.PostRequest;
import com.server.noliter.service.post.dto.response.PostResponse;
import com.server.noliter.service.user.UserService;
import com.server.noliter.service.user.dto.request.UserRequest;
import com.server.noliter.web.controller.post.PostApiController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.BDDMockito.willDoNothing;


@WebMvcTest(PostApiController.class)
public class PostApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @WithMockUser
    @Test
    void 게시글_조회() throws Exception {
        Post buildPost = Post.builder()
                .title("테스트 제목")
                .content("테스트 본문")
                .category(PostCategory.ART)
                .user(User.builder()
                        .username("작성자")
                        .email("qa1@gmail.com")
                        .profileImage("qa1.jpg")
                        .role(Role.USER)
                        .build())
                .views(1)
                .build();

        PostResponse postResponse = new PostResponse(buildPost);

        given(postService.findById(1L)).willReturn(postResponse);

        mockMvc.perform(
                get("/api/v1/post/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(postResponse.getTitle()))
                .andExpect(jsonPath("$.data.content").value(postResponse.getContent()))
                .andExpect(jsonPath("$.data.categoryName").value(postResponse.getCategoryName()))
                .andExpect(jsonPath("$.data.writer").value(postResponse.getWriter()))
                .andExpect(jsonPath("$.data.views").value(1))
                .andDo(print());
        verify(postService).findById(1L);
    }

    @WithMockUser
    @Test
    void 게시글_수정() throws Exception{
        PostRequest buildPost = PostRequest.builder()
                .title("(거래 완료) 리얼포스 키보드")
                .content("판매 완료되었습니다.")
                .categoryName("H/W")
                .build();

        Gson gson = new Gson();
        String requestBody = gson.toJson(buildPost);
        PostResponse postResponse =
                new PostResponse(1L, "H/W", "(거래 완료) 리얼포스 키보드", "판매 완료되었습니다.", "판교 개발자", 1L, 1);

        given(postService.update(eq(1L), any(PostRequest.class)))
                .willReturn(postResponse);

        mockMvc.perform(put("/api/v1/post/1").contentType(MediaType.APPLICATION_JSON).content(requestBody).with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.data.title").value(postResponse.getTitle()))
                .andExpect(jsonPath("$.data.content").value(postResponse.getContent()))
                .andExpect(jsonPath("$.data.categoryName").value(postResponse.getCategoryName()))
                .andExpect(jsonPath("$.data.writer").value(postResponse.getWriter()))
                .andDo(print());

        verify(postService).update(eq(1L), any(PostRequest.class));
    }

    @WithMockUser
    @Test
    void 게시글_삭제() throws Exception {
        Post buildPost = Post.builder()
                .title("제목")
                .content("본문")
                .category(PostCategory.TRAVEL)
                .user(User.builder()
                        .username("여행자")
                        .email("tourist@gmail.com")
                        .profileImage("alps.jpg")
                        .build())
                .views(1)
                .build();

        willDoNothing().given(postService).delete(1L);

        mockMvc.perform(delete("/api/v1/post/1").contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());

        verify(postService).delete(1L);
    }

    @WithMockUser
    @Test
    void 카테고리_리스트() throws Exception {
        mockMvc.perform(get("/api/v1/post/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
    }

    @WithMockUser
    @Test
    void top5_게시글() throws Exception{
        Post buildPost = Post.builder()
                .title("제목")
                .content("본문")
                .category(PostCategory.TRAVEL)
                .user(User.builder()
                        .username("여행자")
                        .email("tourist@gmail.com")
                        .profileImage("alps.jpg")
                        .build())
                .views(1)
                .build();

        PostResponse postResponse = new PostResponse(buildPost);
        List<PostResponse> postList = new ArrayList<>();

        postList.add(postResponse);

        given(postService.getTop5ByViews()).willReturn(postList);

        mockMvc.perform(
                        get("/api/v1/post/top5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(postResponse.getId()))
                .andExpect(jsonPath("$.data[0].categoryName").value(postResponse.getCategoryName()))
                .andExpect(jsonPath("$.data[0].title").value(postResponse.getTitle()))
                .andExpect(jsonPath("$.data[0].content").value(postResponse.getContent()))
                .andExpect(jsonPath("$.data[0].writer").value(postResponse.getWriter()))
                .andExpect(jsonPath("$.data[0].views").value(postResponse.getViews()))
                .andDo(print());
    }
}
