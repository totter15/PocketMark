package com.example.pocketmark.controller;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.BookmarkDto.BookmarkCreateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;
import com.example.pocketmark.dto.BookmarkDto.BookmarkResImpl;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.repository.BookmarkRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.UserRepository;
import com.example.pocketmark.service.BookmarkService;
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
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;



/*
profiles ping9 contains 
server.servlet.encoding:
    charset: UTF-8
    force: true

Api does work without config above, but test doesn't work eventhough folderAPiTest is working..
cannot find the reason why projection doesn't work.
*/


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("ping9")
public class BookmarkApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    FolderService folderService;
    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    FolderRepository folderRepository;

    @Autowired
    private DataBaseCleanUpHelper dbCleanup;
    

    private BookmarkRes bookmarkRes;
    private List<BookmarkResImpl> bookmarkResList;



    @BeforeEach
    void init() throws Exception{
        
        dbCleanup.afterPropertiesSet();
        dbCleanup.execute();

        User user=userRepository.save(new User("test@2mail.com","1234","Ping9"));

        folderService.saveByCreateReq(makeFolderReq().toServiceReq(),user.getId());
        folderService.saveByCreateReq(makeFolderReq().toServiceReq(),user.getId());
    
        bookmarkService.saveByCreateReq(new BookmarkCreateReq(1L,"JPA 영속성", "testUrl", "유익함").toServiceReq());
    

        objectMapper = Jackson2ObjectMapperBuilder.json()
                        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .modules(new JavaTimeModule())
                        .build();
    }

    @DisplayName("Controller - BookmarkApi [POST : /bookmark]")
    @Test
    public void createApiTest() throws Exception{
        //given
        String url = "/api/v1/bookmark";
        BookmarkCreateReq req = makeBookmarkReq();
        String content = objectMapper.writeValueAsString(req);

        //when
        mockMvc
        .perform(MockMvcRequestBuilders.post(url)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect( result -> {
            MockHttpServletResponse response = result.getResponse();
            bookmarkRes = objectMapper.readValue(response.getContentAsString(), BookmarkResImpl.class);
        });

        assertThat(bookmarkRes)
            .hasFieldOrPropertyWithValue("name", "캐럴")
            .hasFieldOrPropertyWithValue("url", "testUrl")
            .hasFieldOrPropertyWithValue("comment", "유익함")
            .hasFieldOrPropertyWithValue("folderId", 2L);
    }



    @DisplayName("Controller - BookmarkApi [GET : /bookmark/{folderId}]")
    @Test
    void readApiTest() throws Exception{
        //given
        String url = "/api/v1/bookmark/2";
        bookmarkService.saveByCreateReq(makeBookmarkReq().toServiceReq());
        bookmarkService.saveByCreateReq(makeBookmarkReq().toServiceReq());

        //when
        mockMvc
        .perform(MockMvcRequestBuilders.get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect( result -> {
            MockHttpServletResponse response = result.getResponse();
            bookmarkResList = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<BookmarkResImpl>>(){});
        });

        //then
        assertEquals(bookmarkResList.size(), 2);
        assertEquals(bookmarkResList.get(0).getFolderId(), 2L);

    }

    @DisplayName("Controller - BookmarkApi [PUT : /bookmark/{bookmarkId}]")
    @Test
    void updateApiTest() throws Exception{
        //given
        String url = "/api/v1/bookmark/1";
        bookmarkService.saveByCreateReq(makeBookmarkReq().toServiceReq());
        BookmarkUpdateReq req = new BookmarkUpdateReq(1L,"바이올린연주","testUrl","최고",1L,300);
        String content = objectMapper.writeValueAsString(req);

        System.out.println( bookmarkRepository.findAll().size());
        bookmarkRepository.findAll().forEach(it->System.out.println(">>> : "+it));
        folderRepository.findAll().forEach(it->System.out.println(">>> : "+it));
        
        //when
        mockMvc
        .perform(MockMvcRequestBuilders.put(url)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect( result -> {
            MockHttpServletResponse response = result.getResponse();
            assertEquals(204, response.getStatus());
        });
        
        //then
        assertEquals(bookmarkRepository.findById(1L).get().getName() , "바이올린연주");
    }


    @DisplayName("Controller - BookmarkApi [DELETE : /bookmark/{bookmarkId}]")
    @Test
    void deleteTest() throws Exception{
        //given
        String url = "/api/v1/bookmark/2";
        bookmarkService.saveByCreateReq(makeBookmarkReq().toServiceReq());

        //when
        mockMvc
        .perform(MockMvcRequestBuilders.delete(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect( result -> {
            MockHttpServletResponse response = result.getResponse();
            assertEquals(response.getStatus(), 204);
        });

        //then
        assertEquals(bookmarkRepository.existsById(2L), false);


    }


    public FolderCreateReq makeFolderReq(){
        return FolderCreateReq.builder()
                .parent(1L)
                .depth(1L)
                .name("JPA")
                .build();
    }

    public BookmarkCreateReq makeBookmarkReq(){
        return new BookmarkCreateReq(2L, "캐럴", "testUrl", "유익함");
    }

    
}
