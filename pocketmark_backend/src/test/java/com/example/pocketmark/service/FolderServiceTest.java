package com.example.pocketmark.service;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.service.FolderService;

import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FolderServiceTest {
    
    @InjectMocks FolderService folderService;
    @Mock FolderRepository folderRepository;
    @Mock UserRepository userRepository;
    
    
    @Test
    void saveByCreateReqTest(){
        //given 
        FolderCreateReq req = FolderCreateReq.builder().depth(1L).parent(1L).name("JPA").build();
        User user = new User("test@gmail.com","1234","Ping9");
        user.setId(2L);
        List<FolderRes> folderResList = new ArrayList<>();
        

        //mocking
        given(userRepository.getOne(any())).willReturn(user);
        given(folderRepository.save(any())).willReturn(req.toServiceReq().toEntity(user)); 

        //when
        FolderResImpl folderRes = folderService.saveByCreateReq(req.toServiceReq(),2L);
        folderResList.add(folderRes);
        
        //then
        // assertEquals(folderRes.getUserId(), user.getId());
        assertEquals(folderRes.getDepth(), 1L);
        assertEquals(folderRes.getParent() ,1L);
        assertEquals(folderRes.getName(), "JPA");
    }


    @Test
    void getFoldersTest(){

        //when
        folderService.getFolders(1L);
    }



}
