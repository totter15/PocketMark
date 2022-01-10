package com.example.pocketmark.controller.api;
import java.util.List;

import javax.persistence.EntityManager;

import com.example.pocketmark.dto.DataDto.DataCreateReq;
import com.example.pocketmark.dto.DataDto.DataDeleteReq;
import com.example.pocketmark.dto.DataDto.DataRes;
import com.example.pocketmark.dto.DataDto.DataUpdateReq;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.service.DataService;

import com.example.pocketmark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final UserService userService;

    private Long getUserId(){
        Long userId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userService.selectUserByToken(userId).getId();
    }




    
    @GetMapping(value="/data") // /data?depth=1
    public ApiDataResponse<DataRes> getData(
        @RequestParam(value ="depth", required = false, defaultValue = "1") Long depth,
        @PageableDefault(size=2) Pageable pageable
        
    )
    {
        return ApiDataResponse.of(dataService.getData(getUserId(), depth, pageable));
    }

    @PutMapping(value="/data")
    public ApiDataResponse<DataRes> updateData(
        @RequestBody DataUpdateReq req
    ){
        //validation in service

        dataService.updateData(req.toServcieReq(), getUserId());
        return ApiDataResponse.empty();

    }

    @PostMapping(value="/data")
    public ApiDataResponse<DataRes> createData(
        @RequestBody DataCreateReq req
    ){
        //
        System.out.println(">>> createData : "+ getUserId());

        dataService.createData(req.toServiceReq(), getUserId());
        return ApiDataResponse.empty();
    }

    @DeleteMapping(value="/data")
    public ApiDataResponse<DataRes> deleteData(
        @RequestBody DataDeleteReq req
    ){
        dataService.deleteData(req.toServiceReq(), getUserId());
        return ApiDataResponse.empty();
    }


    
    
    
}
