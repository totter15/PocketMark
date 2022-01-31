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
public class DataApiController {

    private final DataService dataService;

    
    private Long getUserId(){
        UserPrincipal userPrincipal = (UserPrincipal)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return Long.parseLong(userPrincipal.getUsername());
    }

    //Test
    @GetMapping(value="/data-all")
    public ApiDataResponse<DataRes> getAll(){
        return ApiDataResponse.of(dataService.getAll(getUserId())); 
    }

    //C
    @PostMapping(value="/data")
    public ApiDataResponse<DataRes> createData(
        @RequestBody DataCreateReq req
    ){
        dataService.createData(req.toServiceReq(), getUserId());
        return ApiDataResponse.empty();
    }

    //R
    @GetMapping(value="/data") // /data?depth=1
    public ApiDataResponse<DataRes> getData(
        // @RequestParam(value ="depth", required = false, defaultValue = "1")
        // Long depth,
        @RequestParam(value="folder-id", required = false, defaultValue = "0")
        Long folderId,
        @PageableDefault(size=2) Pageable pageable
        
    )
    {
        return ApiDataResponse.of(dataService.getData(getUserId(), folderId, pageable));
    }

    //U
    @PutMapping(value="/data")
    public ApiDataResponse<DataRes> updateData(
        @RequestBody DataUpdateReq req
    ){
        dataService.updateData(req.toServcieReq(), getUserId());
        return ApiDataResponse.empty();

    }
    
    //D
    @PutMapping(value="/data/delete")
    public ApiDataResponse<DataRes> deleteData(
        @RequestBody DataDeleteReq req
    ){
        dataService.deleteData(req.toServiceReq(), getUserId());
        return ApiDataResponse.empty();
    }


    //Read ALL Folders
    @GetMapping(value="/folder")
    public ApiDataResponse<AllFolderResWithTag> getAllFolders(
        
    ){
        return ApiDataResponse.of(dataService.getAllFolders(getUserId()));
    }

    
    
    
}
