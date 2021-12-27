package com.example.pocketmark.controller.api;

import java.util.List;

import javax.persistence.EntityManager;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.service.FolderService;

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
public class FolderApiController {
    private final FolderService folderService;

    // test 후 삭제할 bean 
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    
    
    //C
    @PostMapping("/folder")
    @CrossOrigin(origins = "*")
    public FolderResImpl createFolder(
        @RequestBody FolderCreateReq req 
    ){
        //create without any select
        return folderService.saveByCreateReq(req);
    }


    //R
    @GetMapping("/folder/{user-id}")
    public List<FolderRes> readFolder(
        @PathVariable("user-id") Long userId
    )
    {
        return folderService.getFolders(userId);
    }

    //U
    @PutMapping("/folder/{folder-id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> updateFolder(
        @PathVariable("folder-id") Long folderId,
        @RequestBody FolderUpdateReq req
    ){
        System.out.println(">>>[API] : "+req);
        System.out.println(">>>[API] : "+folderId);
        folderService.updateFolder(req, folderId);

        return ResponseEntity.status(204).body("Update Succeed! \nBut we not yet dicide what will be returned.");
    }

    //D
    @DeleteMapping("/folder/{folder-id}")
    public ResponseEntity<String> deleteFolder(
        @PathVariable("folder-id") Long folderId
    ){
        folderService.deleteFolderBySelfId(folderId);

        return ResponseEntity.status(204).body("delete Succeed! \nBut we not yet dicide what will be returned.");
        // subordinate Bookmarks 도 지워야해 (구현필요) - Cascade 설정 나중에 하기 
    }




    @GetMapping("/folder/test")
    public List<FolderRes> test(){
        User user = CU();
        userRepository.save(user);
        userRepository.save(CU());
        userRepository.save(CU());
        // Folder folder = Folder.builder().parent(1L).depth(1L).user(user).build();
        folderRepository.save(Folder.builder().parent(1L).depth(1L).user(user).build());
        folderRepository.save(Folder.builder().parent(1L).depth(1L).user(user).build());
        folderRepository.save(Folder.builder().parent(1L).depth(1L).user(user).build());

        folderRepository.findAll().forEach(System.out::println);
    
        return folderRepository.findFolderResByUserIdWithoutJoin(user.getId());
    }

    public User CU(){
        return new User("test@gmail.com","1234","JyuKa");
        
    }

    
}
