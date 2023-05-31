package com.server.noliter.web.search;

import com.server.noliter.service.post.dto.response.PostSliceResponse;
import com.server.noliter.service.search.SearchService;
import com.server.noliter.web.controller.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class SearchApiController {
    private final SearchService searchService;

    @GetMapping("/post")
    public ResponseEntity<Response<PostSliceResponse>> getPosts(@RequestParam("categoryName") String categoryName,
                                                                @RequestParam("word") String word,
                                                                @RequestParam("page") String page,
                                                                @RequestParam("size") String size) {

        PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
        PostSliceResponse response = searchService.getSearchedPosts(pageRequest, categoryName, word);

        return new ResponseEntity<>(new Response<>(response), HttpStatus.OK);
    }

}
