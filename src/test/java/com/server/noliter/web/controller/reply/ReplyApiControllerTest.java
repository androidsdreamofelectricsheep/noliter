package com.server.noliter.web.controller.reply;

import com.google.gson.Gson;
import com.server.noliter.security.annotation.LoginUserArgumentResolver;
import com.server.noliter.service.reply.ReplyService;
import com.server.noliter.service.reply.dto.request.ReplyRequest;
import com.server.noliter.service.reply.dto.response.ReplyResponse;
import com.server.noliter.service.reply.dto.response.ReplySliceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
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


@WebMvcTest(ReplyApiController.class)
public class ReplyApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReplyService replyService;

    @MockBean
    LoginUserArgumentResolver loginUserArgumentResolver;

    @WithMockUser
    @Test
    void 댓글_작성() throws Exception {
        // given
        ReplyRequest replyRequest = ReplyRequest.builder()
                .content("테스트")
                .postId(1L)
                .build();

        Gson gson = new Gson();
        String requestBody = gson.toJson(replyRequest);
        ReplyResponse replyResponse = new ReplyResponse(1L, "테스트", "작성자", 1L);
        given(replyService.save(any(), any(ReplyRequest.class))).willReturn(replyResponse);

        // when, then
        mockMvc.perform(post("/api/v1/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(replyResponse.getId()))
                .andExpect(jsonPath("$.data.content").value(replyResponse.getContent()))
                .andExpect(jsonPath("$.data.writer").value(replyResponse.getWriter()))
                .andExpect(jsonPath("$.data.writerId").value(replyResponse.getWriterId()))
                .andDo(print());
        verify(replyService).save(any(), any(ReplyRequest.class));
    }

    @WithMockUser
    @Test
    void 게시글번호로_댓글_페이징_조회() throws Exception {
        // given
        PageRequest pageRequest = PageRequest.of(Integer.parseInt("1"), Integer.parseInt("3"));
        ReplyResponse replyResponse = new ReplyResponse(1L, "댓글", "작성자", 2L);
        List<ReplyResponse> replyList = new ArrayList<>();
        replyList.add(replyResponse);
        ReplySliceResponse replySliceResponse = new ReplySliceResponse(replyList, true);

        given(replyService.findReplies(pageRequest, 1L)).willReturn(replySliceResponse);

        // when, then
        mockMvc.perform(get("/api/v1/reply")
                        .param("postId", "1")
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.replies").isArray())
                .andExpect(jsonPath("$.data.hasNext").value(true))
                .andDo(print());

        verify(replyService).findReplies(pageRequest, 1L);
    }

    @WithMockUser
    @Test
    void 댓글_수정()throws Exception{
        // given
        ReplyRequest buildReply = ReplyRequest.builder()
                .postId(1L)
                .content("댓글 수정")
                .build();

        Gson gson = new Gson();
        String requestBody = gson.toJson(buildReply);
        ReplyResponse replyResponse = new ReplyResponse(1L, "댓글 수정", "작성자", 2L);

        given(replyService.update(eq(1L), any(ReplyRequest.class))).willReturn(replyResponse);

        // when, then
        mockMvc.perform(put("/api/v1/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.data.id").value(replyResponse.getId()))
                .andExpect(jsonPath("$.data.content").value(replyResponse.getContent()))
                .andExpect(jsonPath("$.data.writer").value(replyResponse.getWriter()))
                .andExpect(jsonPath("$.data.writerId").value(replyResponse.getWriterId()))
                .andDo(print());

        verify(replyService).update(eq(1L), any(ReplyRequest.class));
    }

    @WithMockUser
    @Test
    void 댓글_삭제() throws Exception{
        // given
        willDoNothing().given(replyService).delete(1L);

        // when, then
        mockMvc.perform(delete("/api/v1/reply/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());

        verify(replyService).delete(1L);
    }
}
