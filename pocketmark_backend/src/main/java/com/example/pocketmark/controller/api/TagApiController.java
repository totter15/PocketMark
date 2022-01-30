package com.example.pocketmark.controller.api;

import java.util.List;

import com.example.pocketmark.domain.main.Tag;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.dto.main.DataDto.DataCreateReq;
import com.example.pocketmark.dto.main.DataDto.DataDeleteReq;
import com.example.pocketmark.dto.main.DataDto.DataRes;
import com.example.pocketmark.dto.main.DataDto.DataUpdateReq;
import com.example.pocketmark.dto.main.TagDto.TagCreateBulkReq;
import com.example.pocketmark.dto.main.TagDto.TagCreateReq;
import com.example.pocketmark.dto.main.TagDto.TagDeleteBulkReq;
import com.example.pocketmark.dto.main.TagDto.TagDeleteReq;
import com.example.pocketmark.dto.main.TagDto.TagRes;
import com.example.pocketmark.repository.TagRepository;
import com.example.pocketmark.security.provider.UserPrincipal;
import com.example.pocketmark.service.DataService;
import com.example.pocketmark.service.TagService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class TagApiController {
    /* Create 와 Delete만 있음 */
    /* Update 는 Immutable 엔티티라 없고,
       Read 는 DataController에서 처리
    */

    private final TagService tagService;

    private Long getUserId(){
        UserPrincipal userPrincipal = (UserPrincipal)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return Long.parseLong(userPrincipal.getUsername());
    }

    //Test
    @GetMapping(value="/tag-all")
    public ApiDataResponse<List<TagRes>> getAll(){
        return ApiDataResponse.of(tagService.getAll(getUserId())); 
    }

    //Create
    @PostMapping(value="/tag")
    public ApiDataResponse<String> createTag(
        @RequestBody TagCreateBulkReq req
    ){
        tagService.createTags(req, getUserId());
        return ApiDataResponse.empty();
    }

    //Delete
    @PutMapping(value="/tag-delete")
    public ApiDataResponse<TagRes> deleteTag(
        @RequestBody TagDeleteBulkReq req
    ){
        tagService.deleteTagsInBatch(req, getUserId());
        return ApiDataResponse.empty();
    }   


}
