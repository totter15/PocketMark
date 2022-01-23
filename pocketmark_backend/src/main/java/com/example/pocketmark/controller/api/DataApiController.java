package com.example.pocketmark.controller.api;

import com.example.pocketmark.dto.DataDto.DataCreateReq;
import com.example.pocketmark.dto.DataDto.DataDeleteReq;
import com.example.pocketmark.dto.DataDto.DataRes;
import com.example.pocketmark.dto.DataDto.DataUpdateReq;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.security.provider.UserPrincipal;
import com.example.pocketmark.service.DataService;

import com.example.pocketmark.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class DataApiController {

    private final DataService dataService;

    private Long getUserId() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return Long.parseLong(userPrincipal.getUsername());
    }

    // C
    @PostMapping(value = "/data")
    public ApiDataResponse<DataRes> createData(
            @RequestBody DataCreateReq req) {
        System.out.println("test");
        dataService.createData(req.toServiceReq(), getUserId());
        return ApiDataResponse.empty();
    }

    // R
    @GetMapping(value = "/data") // /data?depth=1
    public ApiDataResponse<DataRes> getData(
            // @RequestParam(value ="depth", required = false, defaultValue = "1")
            // Long depth,
            @RequestParam(value = "folder-id", required = false, defaultValue = "0") Long folderId,
            @PageableDefault(size = 2) Pageable pageable

    ) {
        return ApiDataResponse.of(dataService.getData(getUserId(), folderId, pageable));
    }

    // U
    @PutMapping(value = "/data")
    public ApiDataResponse<DataRes> updateData(
            @RequestBody DataUpdateReq req) {
        dataService.updateData(req.toServcieReq(), getUserId());
        return ApiDataResponse.empty();

    }

    // D
    @PutMapping(value = "/data/delete")
    public ApiDataResponse<DataRes> deleteData(
            @RequestBody DataDeleteReq req) {
        dataService.deleteData(req.toServiceReq(), getUserId());
        return ApiDataResponse.empty();
    }

}
