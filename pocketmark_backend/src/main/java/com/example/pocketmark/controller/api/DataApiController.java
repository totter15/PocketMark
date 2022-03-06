package com.example.pocketmark.controller.api;

import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.dto.main.DataDto.DataCreateReq;
import com.example.pocketmark.dto.main.DataDto.DataDeleteReq;
import com.example.pocketmark.dto.main.DataDto.DataRes;
import com.example.pocketmark.dto.main.DataDto.DataUpdateReq;
import com.example.pocketmark.dto.main.ItemDto.AllFolderResWithTag;
import com.example.pocketmark.security.provider.UserPrincipal;
import com.example.pocketmark.service.DataService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
@Api(tags={"Data API (폴더/북마크)"})
public class DataApiController {

    private final DataService dataService;

    private Long getUserId() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return Long.parseLong(userPrincipal.getUsername());
    }

    // Test
    @GetMapping(value = "/data-all")
    @ApiOperation(value="사용자의 모든 폴더/북마크를 가져옴")
    public ApiDataResponse<DataRes> getAll() {
        return ApiDataResponse.of(dataService.getAll(getUserId()));
    }

    // C
    @PostMapping(value = "/data")
    @ApiOperation(value="사용자의 폴더/북마크를 일괄 생성")
    public ApiDataResponse<DataRes> createData(
            @RequestBody DataCreateReq req) {
        System.out.println("test");
        dataService.createData(req.toServiceReq(), getUserId());
        return ApiDataResponse.of(HttpStatus.OK.value(), "데이터가 성공적으로 생성되었습니다.", null);
    }

    // R
    @GetMapping(value = "/data") // /data?depth=1
    @ApiOperation(value="folder-id를 부모로 하는 자식 폴더/북마크를 페이징처리하여 가져옴")
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
    @ApiOperation(value="사용자의 폴더/북마크를 일괄 변경")
    public ApiDataResponse<DataRes> updateData(
            @RequestBody DataUpdateReq req) {
        dataService.updateData(req.toServcieReq(), getUserId());
        return ApiDataResponse.of(HttpStatus.OK.value(), "데이터가 성공적으로 수정되었습니다.", null);

    }

    // D
    @PutMapping(value = "/data/delete")
    @ApiOperation(value="사용자의 폴더/북마크를 일괄 삭제")
    public ApiDataResponse<DataRes> deleteData(
            @RequestBody DataDeleteReq req) {
        dataService.deleteData(req.toServiceReq(), getUserId());
        return ApiDataResponse.of(HttpStatus.OK.value(), "데이터가 성공적으로 삭제되었습니다.", null);
    }



    //Read ALL Folders
    @GetMapping(value="/folder")
    @ApiOperation(value="사용자의 모든폴더를 가져옴")
    public ApiDataResponse<AllFolderResWithTag> getAllFolders(

    ) {
        return ApiDataResponse.of(dataService.getAllFolders(getUserId()));
    }

}
