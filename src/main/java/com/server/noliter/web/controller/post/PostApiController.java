package com.server.noliter.web.controller.post;

import com.server.noliter.domain.post.PostCategory;
import com.server.noliter.domain.security.annotation.LoginUser;
import com.server.noliter.domain.security.dto.SessionUser;
import com.server.noliter.service.post.PostService;
import com.server.noliter.service.post.dto.request.PostRequest;
import com.server.noliter.service.post.dto.response.PostResponse;
import com.server.noliter.web.controller.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@RestController
public class PostApiController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Response<PostResponse>> save(@LoginUser SessionUser loginUser, @RequestBody PostRequest request){
        PostResponse response = postService.save(loginUser.getId(), request);

        return new ResponseEntity<>(new Response<>(response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<PostResponse>> getPost(@PathVariable Long id){
        PostResponse post = postService.findById(id);

        return new ResponseEntity<>(new Response<>(post), HttpStatus.OK);
    }

    @PreAuthorize("@webSecurity.checkUserHasPostAuthority(#id, #loginUser)")
    @PutMapping("/{id}")
    public ResponseEntity<Response<PostResponse>> update(@LoginUser SessionUser loginUser, @PathVariable Long id, @RequestBody PostRequest request){
        PostResponse response = postService.update(id, request);

        return new ResponseEntity<>(new Response<>(response), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("@webSecurity.checkUserHasPostAuthority(#id, #loginUser)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<PostResponse>> delete(@LoginUser SessionUser loginUser, @PathVariable Long id){
        postService.delete(id);

        return new ResponseEntity<>(new Response<>(null), HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<Response<PostCategory[]>> getCategoryList(){
        PostCategory[] categories = PostCategory.values();

        return new ResponseEntity<>(new Response<>(categories), HttpStatus.OK);
    }

    @GetMapping("/top5")
    public ResponseEntity<Response<List<PostResponse>>> getTop5Posts(){
        List<PostResponse> top5Posts = postService.getTop5ByViews();

        return new ResponseEntity<>(new Response<>(top5Posts), HttpStatus.OK);
    }
}
