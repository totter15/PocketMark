package com.bookmarkmanager.bookmarkmanager.controller;

import java.util.Enumeration;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
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
import org.springframework.web.bind.annotation.RequestHeader;
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

    @GetMapping("/header-test")
    public ResponseEntity<String> getTest(@RequestHeader Map<String, Object> reqHeader){
        
        // HttpHeaders httpHeaders = new HttpHeaders();
        // httpHeaders.set("Access-Control-Allow-Origin","http://localhost:3000");
        // httpHeaders.set("Access-Control-Allow-Methods","GET");
        // httpHeaders.set("Access-Control-Allow-Headers","Content-Type, Authorization, Content-Length, X-Requested-With");
        
        System.out.println("#Request Header");
        for(String key : reqHeader.keySet()){
            System.out.println(key + " : " + reqHeader.get(key));
        }
        System.out.println("# Header End");

        return ResponseEntity.ok().body("I'm responding.. with header xD");
    }
}
