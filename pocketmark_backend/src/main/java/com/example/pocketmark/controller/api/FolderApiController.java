package com.example.pocketmark.controller.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.DataDto.DataRes;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.dto.common.ApiDataResponse;
import com.example.pocketmark.repository.BookmarkRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.service.DataService;
import com.example.pocketmark.service.FolderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class FolderApiController {
    private final FolderService folderService;

    // test 후 삭제할 bean 
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    public static Long getUserId(){
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }
    
    
    //C
    // @PreAuthorize("isAuthenticated()")
    @PostMapping("/folder")
    @CrossOrigin(origins = "*")
    public FolderResImpl createFolder(
        @RequestBody FolderCreateReq req 
    ){
        //create without any select
        return folderService.saveByCreateReq(req.toServiceReq(),getUserId());
    }


    //R
    @GetMapping("/folder")
    public List<FolderRes> readFolder(
    )
    {
        return folderService.getFolders(getUserId());
    }

    //U
    @PutMapping("/folder/{folder-id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> updateFolder(
        @PathVariable("folder-id") Long folderId,
        @Valid @RequestBody FolderUpdateReq req
    ){

        // folderService.updateFolder(req.toServiceReq(),folderId);

        return ResponseEntity.status(204).body("Update Succeed! \nBut we not yet dicide what will be returned.");
    }

    //D
    @DeleteMapping("/folder/{folder-id}")
    public ResponseEntity<String> deleteFolder(
        @PathVariable("folder-id") Long folderId
    ){
        

        folderService.deleteFolderBySelfId(folderId,getUserId());

        return ResponseEntity.status(204).body("delete Succeed! \nBut we not yet dicide what will be returned.");
        // subordinate Bookmarks 도 지워야해 (구현필요) - Cascade 설정 나중에 하기 
    }


    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    DataService dataService;

    // @PreAuthorize("isAuthenticated()")
    @GetMapping("/folder/test")
    public ApiDataResponse<DataRes> test(
        @PageableDefault(size=2) Pageable pageable

    ){
        User user = CU("test@gmial.com","Ping91");
        userRepository.save(user);
        userRepository.save(CU("test@gmial1.com","Ping92"));
        userRepository.save(CU("test@gmial2.com","Ping93"));
        // Folder folder = Folder.builder().parent(1L).depth(1L).user(user).build();
        Folder rootFolder = folderRepository.save(Folder.builder().name("북마크서비스").parent(0L).depth(0L).user(user).build());
        folderRepository.save(Folder.builder().name("JPA").parent(1L).depth(1L).user(user).build());
        folderRepository.save(Folder.builder().name("PUBG").parent(1L).depth(1L).user(user).build());
        bookmarkRepository.save(Bookmark.builder().name("영국음식레시피").folder(rootFolder).url("testUrl.com").build() );
        bookmarkRepository.save(Bookmark.builder().name("서핑하기좋은곳").folder(rootFolder).url("testUrl.com").build() );
        bookmarkRepository.save(Bookmark.builder().name("가을서울관광명소").folder(rootFolder).url("testUrl.com").build() );

        user = CU("test@gmial3.com","Ping94");
        userRepository.save(user);
        rootFolder = folderRepository.save(Folder.builder().name("MUSIC").parent(0L).depth(0L).user(user).build());
        folderRepository.save(Folder.builder().name("Ed Sheeran").parent(rootFolder.getId()).depth(1L).user(user).build());
        bookmarkRepository.save(Bookmark.builder().name("J-POP").folder(rootFolder).url("testUrl.com").build() );
        bookmarkRepository.save(Bookmark.builder().name("여행하기좋은곳").folder(rootFolder).url("testUrl.com").build() );
        bookmarkRepository.save(Bookmark.builder().name("필리핀관광명소").folder(rootFolder).url("testUrl.com").build() );
        
        

        folderRepository.findAll().forEach(System.out::println);
    
        return ApiDataResponse.of(dataService.getData(1L, 1L,pageable));
    }

    public User CU(String email, String nickname){
        return new User(email,"1234",nickname);
        
    }

    
}
