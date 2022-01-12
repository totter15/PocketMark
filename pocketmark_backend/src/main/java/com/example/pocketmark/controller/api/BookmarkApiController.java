package com.example.pocketmark.controller.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.BookmarkDto.BookmarkCreateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;
import com.example.pocketmark.dto.BookmarkDto.BookmarkResImpl;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkCreateReq.BookmarkCreateServiceReq;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.repository.BookmarkRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.service.BookmarkService;
import com.example.pocketmark.service.FolderService;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class BookmarkApiController {

    private final BookmarkService bookmarkService;

    // test 후 삭제할 bean 
    private final FolderService folderService;
    private final FolderRepository folderRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    //C
    @PostMapping("/bookmark")
    @CrossOrigin(origins = "*")
    public BookmarkResImpl createBookmark(
        @Valid @RequestBody BookmarkCreateReq req 
    ){
        //create without any select
        return bookmarkService.saveByCreateReq(req.toServiceReq());
    }


    //R
    @GetMapping("/bookmark/{folder-id}")
    public List<BookmarkRes> readBookmark(
        @PathVariable("folder-id") Long folderId
    )
    {
        return bookmarkService.getBookmark(folderId);
    }

    //U
    @PutMapping("/bookmark/{bookmark-id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> updateBookmark(
        @PathVariable("bookmark-id") Long bookmarkId,
        @Valid @RequestBody BookmarkUpdateReq req
    ){
        bookmarkService.updateBookmark(req.toServiceReq(), bookmarkId);

        return ResponseEntity.status(204).body("Update Succeed! \nBut we not yet dicide what will be returned.");
    }

    //D
    @DeleteMapping("/bookmark/{bookmark-id}")
    public ResponseEntity<String> deleteBookmark(
        @PathVariable("bookmark-id") Long bookmarkId
    ){
        //토큰 디코딩후 userId 넣어주면됨. 
        Long userId = 1L; // jwt 구현이후 수정필요 
        bookmarkService.deleteBookmarkBySelfId(bookmarkId, userId);

        return ResponseEntity.status(204).body("delete Succeed! \nBut we not yet dicide what will be returned.");
        
    }



    @GetMapping("/bookmark/test")
    public List<BookmarkRes> test(){
        userRepository.save(new User("test@email.com","1234","Ping9"));

        folderService.saveByCreateReq(makeFolderReq().toServiceReq(),1L);
        folderService.saveByCreateReq(makeFolderReq().toServiceReq(),1L);
        
        bookmarkService.saveByCreateReq(new BookmarkCreateServiceReq(1L,"JPA 영속성", "testUrl", "유익함"));
        bookmarkService.saveByCreateReq(new BookmarkCreateServiceReq(2L,"JPA 영속성", "testUrl", "유익함"));
        bookmarkService.saveByCreateReq(new BookmarkCreateServiceReq(2L,"JPA 영속성", "testUrl", "유익함"));

        userRepository.findAll().forEach(System.out::println);
        folderRepository.findAll().forEach(System.out::println);
        bookmarkRepository.findAll().forEach(System.out::println);
    
        return bookmarkRepository.findByFolderId(2L);
    }

    public FolderCreateReq makeFolderReq(){
        return FolderCreateReq.builder()
                .parent(1L)
                .depth(1L)
                .name("JPA")
                .build();
    }


    
}
