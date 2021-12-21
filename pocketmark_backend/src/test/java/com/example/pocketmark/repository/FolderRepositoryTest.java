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

    private Folder folder;

    @Mock
    private User user;


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
        
        // folders.forEach(System.out::println);
        // System.out.println(">>> Select : "+folders.get(0));
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
    


    
}
