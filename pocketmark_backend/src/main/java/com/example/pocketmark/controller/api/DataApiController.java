package com.example.pocketmark.controller.api;
import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.DataDto;
import com.example.pocketmark.dto.BookmarkDto.BookmarkCreateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;
import com.example.pocketmark.dto.BookmarkDto.BookmarkResImpl;
import com.example.pocketmark.dto.DataDto.DataRes;
import com.example.pocketmark.dto.DataDto.DataUpdateReq;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.repository.BookmarkRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.service.BookmarkService;
import com.example.pocketmark.service.DataService;
import com.example.pocketmark.service.FolderService;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
