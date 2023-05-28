package com.server.noliter.web.controller.search;

import com.server.noliter.service.post.dto.response.PostResponse;
import com.server.noliter.service.post.dto.response.PostSliceResponse;
import com.server.noliter.service.search.SearchService;
import com.server.noliter.web.search.SearchApiController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchApiController.class)
public class SearchApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @WithMockUser
    @Test
    void 게시글_검색() throws Exception {
        // given
        PageRequest pageRequest = PageRequest.of(Integer.parseInt("1"), Integer.parseInt("5"));
        PostResponse postResponse = new PostResponse(1L, "GAME", "Minesweeper", "windows game", "sweeper", 4L, 4);
        List<PostResponse> list = new ArrayList<>();
        list.add(postResponse);
        PostSliceResponse slice = new PostSliceResponse(list, true);

        given(searchService.getSearchedPosts(pageRequest, "GAME", "Minesweeper")).willReturn(slice);

        // when, then
        mockMvc.perform(get("/api/v1/search/post")
                        .param("categoryName", "GAME")
                        .param("word", "Minesweeper")
                        .param("page", "1")
                        .param("size", "5"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.posts").isNotEmpty())
                .andExpect(jsonPath("$.data.hasNext").value(true))
                .andDo(print());

        verify(searchService).getSearchedPosts(pageRequest, "GAME", "Minesweeper");
    }
}
