package com.example.pocketmark.controller;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.dto.LoginDto.LoginReq;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.service.FolderService;
import com.example.pocketmark.util.DataBaseCleanUpHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        folderService.saveByCreateReq(makeFolderReq().toServiceReq(),1L);
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
        folderService.saveByCreateReq(makeFolderReq().toServiceReq(),1L);

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
        // assertEquals(folderResList.get(0).getUserId(), 1L);

    }

    // @DisplayName("Controller - FolderApi [PUT : /folder/{folderId}]")
    // @Test
    // void updateApiTest() throws Exception{
    //     //given
    //     String url = "/api/v1/folder/1";
    //     folderService.saveByCreateReq(makeFolderReq().toServiceReq(),1L);
    //     FolderUpdateReq req = new FolderUpdateReq(1L, 1L,1L,"재미있는 요리공부",15);
    //     String content = objectMapper.writeValueAsString(req);
        
        
    //     //when
    //     mockMvc
    //     .perform(MockMvcRequestBuilders.put(url)
    //     .content(content)
    //     .contentType(MediaType.APPLICATION_JSON))
    //     .andExpect( result -> {
    //         MockHttpServletResponse response = result.getResponse();
    //         assertEquals(response.getStatus(), 204);
    //     });
        
    //     //then
    //     assertEquals(folderService.getFolders(1L).get(0).getName(), "재미있는 요리공부");
    // }


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


    private String token="";
    @Test
    void authorizationTest() throws Exception{
        //given
        String urlLogin = "/api/v1/login";
        String urlTest = "/api/v1/folder/test";
        String urlGetData = "/api/v1/data";
        userRepository.findAll().forEach(System.out::println);

        LoginReq req = LoginReq.builder().email("testEmail").pw("testPw").build();
        String content = objectMapper.writeValueAsString(req);

        //when
        MvcResult firstResult=mockMvc
                        .perform(MockMvcRequestBuilders.post(urlLogin)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        System.out.println(firstResult.getResponse().getContentAsString());
        String jwt = firstResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        String[] tokens = jwt.split("\\.");
        System.out.println(jwt);

        System.out.println(">> "+ Base64.getDecoder().decode(tokens[1]));

        //make test set
        MvcResult wantedResult = mockMvc
                                .perform(MockMvcRequestBuilders.get(urlTest)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwt)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andReturn();
        System.out.println(">>> : "+wantedResult.getResponse().getContentAsString());
        
        //get List of data
        MvcResult wantedResult2 = mockMvc
                                .perform(MockMvcRequestBuilders.get(urlGetData)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwt)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andReturn();
        System.out.println(">>>22 : "+wantedResult2.getResponse().getContentAsString());

//        String token = Jwts.builder()
//                            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
//                            .setSubject("fresh")
//                            .signWith(SignatureAlgorithm.HS256, "I'm manipulating u.")
//                            .compact();
//
        String token = null;

        // 위변조
        mockMvc
            .perform(MockMvcRequestBuilders.get(urlTest)
            .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(result -> System.out.println(result.getResponse().getContentAsString()))
            .andExpect(result -> assertEquals(HttpStatus.UNAUTHORIZED.value(),result.getResponse().getStatus()))
            .andExpect(result-> assertEquals("JWT Token Expired",result.getResponse().getErrorMessage()));
            
        Thread.sleep(3500);
        //기한만료 
                wantedResult = mockMvc
                                .perform(MockMvcRequestBuilders.get(urlTest)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwt)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andReturn();
        System.out.println(">>> : "+wantedResult.getResponse().getContentAsString());

    }



    public FolderCreateReq makeFolderReq(){
        return FolderCreateReq.builder()
                .parent(1L)
                .depth(1L)
                .name("JPA")
                .build();
    }













}
