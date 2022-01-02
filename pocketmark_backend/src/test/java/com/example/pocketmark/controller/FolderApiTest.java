package com.example.pocketmark.controller;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.service.FolderService;
import com.example.pocketmark.util.DataBaseCleanUpHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.mockito.BDDMockito.*;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
// @WebAppConfiguration
// @WebMvcTest
@Slf4j
@ActiveProfiles("ping9")
public class FolderApiTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    FolderService folderService;

    @Autowired
    private DataBaseCleanUpHelper dbCleanup;
   

    private FolderRes folderRes;
    private List<FolderResImpl> folderResList;


    @BeforeEach
    void init() throws Exception{
        dbCleanup.afterPropertiesSet();
        dbCleanup.execute();
        
        userRepository.save(new User("testEmail","testPw","Ping9"));
        userRepository.save(new User("testEmail2","testPw2","Ming9"));
        folderService.saveByCreateReq(makeFolderReq().toServiceReq());
        objectMapper = Jackson2ObjectMapperBuilder.json()
                        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .modules(new JavaTimeModule())
                        .build();
    }

    @DisplayName("Controller - FolderApi [POST : /folder]")
    @Test
    public void createApiTest() throws Exception{
        //given
        String url = "/api/v1/folder";
        FolderCreateReq req = makeFolderReq();
        String content = objectMapper.writeValueAsString(req);

        //when
        mockMvc
        .perform(MockMvcRequestBuilders.post(url)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect( result -> {
            MockHttpServletResponse response = result.getResponse();
            folderRes = objectMapper.readValue(response.getContentAsString(), FolderResImpl.class);
        });



        assertThat(folderRes)
            .hasFieldOrPropertyWithValue("parent", 1L)
            .hasFieldOrPropertyWithValue("depth", 1L)
            .hasFieldOrPropertyWithValue("name", "JPA")
            .hasFieldOrPropertyWithValue("userId", 1L);
    }



    @DisplayName("Controller - FolderApi [GET : /folder/{userId}]")
    @Test
    void readApiTest() throws Exception{
        //given
        String url = "/api/v1/folder/1";
        folderService.saveByCreateReq(makeFolderReq().toServiceReq());

        //when
        mockMvc
        .perform(MockMvcRequestBuilders.get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect( result -> {
            MockHttpServletResponse response = result.getResponse();
            folderResList = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<FolderResImpl>>(){});
        });

        //then
        assertEquals(folderResList.size(), 2);
        assertEquals(folderResList.get(0).getUserId(), 1L);

    }

    @DisplayName("Controller - FolderApi [PUT : /folder/{folderId}]")
    @Test
    void updateApiTest() throws Exception{
        //given
        String url = "/api/v1/folder/1";
        folderService.saveByCreateReq(makeFolderReq().toServiceReq());
        FolderUpdateReq req = new FolderUpdateReq(1L, 1L,1L,"재미있는 요리공부",15);
        String content = objectMapper.writeValueAsString(req);
        
        
        //when
        mockMvc
        .perform(MockMvcRequestBuilders.put(url)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect( result -> {
            MockHttpServletResponse response = result.getResponse();
            assertEquals(response.getStatus(), 204);
        });
        
        //then
        assertEquals(folderService.getFolders(1L).get(0).getName(), "재미있는 요리공부");
    }


    @DisplayName("Controller - FolderApi [DELETE : /folder/{folderId}]")
    @Test
    void deleteTest() throws Exception{
        //given
        String url = "/api/v1/folder/1";

        //when
        mockMvc
        .perform(MockMvcRequestBuilders.delete(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect( result -> {
            MockHttpServletResponse response = result.getResponse();
            assertEquals(response.getStatus(), 204);
        });

        //then
        assertEquals(folderService.getFolders(1L).size(), 0);


    }


    public FolderCreateReq makeFolderReq(){
        return FolderCreateReq.builder()
                .parent(1L)
                .depth(1L)
                .name("JPA")
                .userId(1L).build();
    }













}
