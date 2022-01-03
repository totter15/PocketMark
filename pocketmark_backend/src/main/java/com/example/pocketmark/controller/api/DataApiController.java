package com.example.pocketmark.controller.api;
import java.util.List;

import javax.persistence.EntityManager;

import com.example.pocketmark.dto.DataDto.DataRes;
import com.example.pocketmark.dto.DataDto.DataUpdateReq;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.service.DataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    DataService dataService;
    
    @GetMapping(value="/data/{user-id}") // /data/1?depth=1
    public ApiDataResponse<DataRes> getData(
        @PathVariable("user-id") Long userId,
        @RequestParam("depth") Long depth,
        @PageableDefault(size=2) Pageable pageable
    )
    {
        return ApiDataResponse.of(dataService.getData(userId, depth, pageable));
    }

    @PutMapping(value="/data/{user-id}")
    public ApiDataResponse<DataRes> updateData(
        @RequestBody DataUpdateReq req,
        @PathVariable("user-id") Long userId
    ){
        //validation in service

        dataService.updateData(req.toServcieReq(), userId);
        return ApiDataResponse.empty();

    }


    
    
    
}
