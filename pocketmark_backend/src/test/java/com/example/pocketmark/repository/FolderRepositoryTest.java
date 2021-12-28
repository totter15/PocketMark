package com.example.pocketmark.repository;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.pocketmark.config.JpaConfig;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

//BDD mock
import static org.mockito.BDDMockito.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.TransactionScoped;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DB - 폴더")
@DataJpaTest // 1. auditing 적용되게 하려면 메인app 에 @jpaauditing 달아주는 방법이 있음  || 2. Import(Myconfig.class)
// @SpringBootTest
@Import(JpaConfig.class)
@ActiveProfiles("ping9")
public class FolderRepositoryTest {

    @Autowired private FolderRepository folderRepository;
    @Autowired private EntityManager em;
    @Mock UserRepository userRepository;


    private Folder folder;

    private User user;


    @BeforeEach
    void init(){
        user = new User("test@email.com","1234","Ping9");
        folder = makeFolder(0L, 1L, "요리블로그 모음", user);
    }

    public Folder makeFolder(Long parent, Long depth, String name, User user){
        Folder folder = Folder.builder()
                        .parent(parent)
                        .depth(depth)
                        .user(user)
                        .name(name)
                        .visitCount(0)
                        .build();
        // System.out.println(">>> Make Folder : " + folder);
                        return folder;
    }

    @DisplayName("DB - 폴더 Create & Read")
    @Test
    void folderRepoCreateAndReadTest(){
        //Given
       
        //When
        folderRepository.save(folder);
        List<Folder> folders = folderRepository.findAll();
        
        //Then
        assertThat(folders.size()).isEqualTo(1);
        assertThat(folders.get(0))
            .hasFieldOrPropertyWithValue("parent", 0L)
            .hasFieldOrPropertyWithValue("depth", 1L)
            .hasFieldOrPropertyWithValue("user", user)
            .hasFieldOrPropertyWithValue("visitCount", 0);
    }

    @DisplayName("DB - 폴더 Update")
    @Test
    void folderRepoUpdateTest(){
        //Given
        folderRepository.save(folder);

        //When
        List<Folder> folders = folderRepository.findByUser(user);
        assertThat(folders.isEmpty()).isFalse();
        System.out.println(">>> Before Update : "+ folders.get(0));

        Folder selectedFolder = folders.get(0);
        int prev = selectedFolder.getVisitCount();
        selectedFolder.visitCountUpdate(selectedFolder.getVisitCount()+1);
        folderRepository.save(selectedFolder);

        folders = folderRepository.findAll();
        
        

        //Then
        assertThat(folders.size()).isEqualTo(1);
        assertThat(folders.get(0))
            .hasFieldOrPropertyWithValue("parent", 0L)
            .hasFieldOrPropertyWithValue("depth", 1L)
            .hasFieldOrPropertyWithValue("user", user)
            .hasFieldOrPropertyWithValue("visitCount", prev+1);

        System.out.println(">>> After Update : "+ folders.get(0));

    }

    @DisplayName("DB - 폴더 Delete")
    @Test
    void folderRepoDeleteTest(){
        //Given
        folderRepository.save(folder);

        //When
        List<Folder> folders = folderRepository.findByUser(user);
        assertThat(folders.isEmpty()).isFalse();

        Folder selectedFolder = folders.get(0);
        folderRepository.deleteById(selectedFolder.getId()); // 이것도  flush
        folders = folderRepository.findAll();
        
        //Then
        assertThat(folders.size()).isEqualTo(0);
        assertThat(folders).isEmpty();;
    }


    @DisplayName("DB - 폴더 Update visitCnt")
    @Test
    void visitCntUpdateTest(){
        //Given
        

        //When&Then
        assertThat(folder.visitCountUpdate(-1)).isFalse();
        assertThat(folder.visitCountUpdate(30)).isTrue();    
    }


    @DisplayName("DB - 조인없이 폴더 id 로 Select ")
    @Test
    void findFolderResByUserIdWithoutJoin(){
        //given
        folderRepository.save(folder);

        //when
        List<FolderRes> result = folderRepository.findFolderResByUserIdWithoutJoin(user.getId());
        
        //then
        assertEquals(result.get(0).getParent(), 0L);
        assertEquals(result.get(0).getDepth(), 1L);
        assertEquals(result.get(0).getName(),"요리블로그 모음");
        assertEquals(result.get(0).getUserId(), user.getId());
    }

    @Test
    void test(){
        Folder folder = Folder.builder().depth(1L).build();
        System.out.println(folder);
    }

    
    


    
}
