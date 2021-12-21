package com.example.pocketmark.repository;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//BDD mock
import static org.mockito.BDDMockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DB - 폴더")
@DataJpaTest
public class FolderRepositoryTest {

    @Autowired
    private FolderRepository folderRepository;

    @Mock
    private User user;

    private Folder folder;

    @BeforeEach
    void init(){
        folder = makeFolder(0L, 1L, user);
        
    }

    public Folder makeFolder(Long parent, Long depth, User user){
        Folder folder = Folder.builder()
                        .parent(parent)
                        .depth(depth)
                        .user(user)
                        .visitCount(0)
                        .build();

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
        
        folders.forEach(System.out::println);

    }

    @DisplayName("DB - 폴더 Update")
    @Test
    void folderRepoUpdateTest(){
        //Given
        folderRepository.save(folder);

        //When
        Folder folder = folderRepository.findById(1L).orElse(null);
        int prev = folder.getVisitCount();
        folder.visitCountUpdate(folder.getVisitCount()+1);
        folderRepository.save(folder);
        List<Folder> folders = folderRepository.findAll();
        

        //Then
        assertThat(folders.size()).isEqualTo(1);
        assertThat(folders.get(0))
            .hasFieldOrPropertyWithValue("parent", 0L)
            .hasFieldOrPropertyWithValue("depth", 1L)
            .hasFieldOrPropertyWithValue("user", user)
            .hasFieldOrPropertyWithValue("visitCount", prev+1);

    }

    @DisplayName("DB - 폴더 Delete")
    @Test
    void folderRepoDeleteTest(){
        //Given
        folderRepository.save(folder);

        //When
        Folder folder = folderRepository.findById(1L).orElse(null);
        folderRepository.deleteById(folder.getId()); // 이것도  flush
        List<Folder> folders = folderRepository.findAll();
        
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
    


    
}
