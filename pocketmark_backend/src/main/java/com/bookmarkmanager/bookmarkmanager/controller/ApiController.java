package com.bookmarkmanager.bookmarkmanager.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookmarkmanager.bookmarkmanager.db.entity.Bookmark;
import com.bookmarkmanager.bookmarkmanager.db.entity.Folder;
import com.bookmarkmanager.bookmarkmanager.db.entity.Url;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    // //add
    // @PostMapping("add")
    // public void add(@RequestBody Bookmark bookMark,
    //                 @RequestBody Folder folder,
    //                 @RequestBody Url urls
    //                 ){

    //     return;
    // }

    @GetMapping("/get")
    public ResponseEntity<String> getTest(HttpServletRequest httpServletRequest){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Access-Control-Allow-Origin","*");
        httpHeaders.set("Access-Control-Allow-Methods","GET");
        httpHeaders.set("Access-Control-Allow-Headers","Content-Type, Authorization, Content-Length, X-Requested-With");
        
        return ResponseEntity.ok().headers(httpHeaders).body("I'm responding.. with header xD");
    }
}
