package com.example.pocketmark.repository;

import java.util.List;

import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


//BDD mock
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DB - 북마크")
@DataJpaTest
public class BookmarkRepositoryTest {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Mock
    private Folder folder;

    private Bookmark bookmark;

    @BeforeEach
    void init(){
        bookmark = makeBookmark("SpringFrameWork", "www.blahblah.com", "JPA 기초",  folder);
    }

    

    public Bookmark makeBookmark(String name, String url, String comment, Folder folder){
        Bookmark bookmark = Bookmark.builder()
                        .name(name)
                        .url(url)
                        .comment(comment)
                        .folder(folder)
                        .visitCount(0)
                        .build();

                        return bookmark;
    }



    @DisplayName("DB - 북마크 Create & Read")
    @Test
    void bookmarkRepoCreateAndReadTest(){
        //Given
       
        //When
        bookmarkRepository.save(bookmark);
        List<Bookmark> bookmarks=  bookmarkRepository.findAll();

        //Then
        assertThat(bookmarks.size()).isEqualTo(1);
        assertThat(bookmarks.get(0))
            .hasFieldOrPropertyWithValue("name", "SpringFrameWork")
            .hasFieldOrPropertyWithValue("url", "www.blahblah.com")
            .hasFieldOrPropertyWithValue("comment", "JPA 기초")
            .hasFieldOrPropertyWithValue("folder", folder)
            .hasFieldOrPropertyWithValue("visitCount", 0);
        
        bookmarks.forEach(System.out::println);

    }

    @DisplayName("DB - 북마크 Update")
    @Test
    void bookmarkRepoUpdateTest(){
        //Given
        bookmarkRepository.save(bookmark);

        //When
        List<Bookmark> bookmarks = bookmarkRepository.findByFolder(folder);
        assertThat(bookmarks.isEmpty()).isFalse();

        Bookmark selectedBookmark = bookmarks.get(0);
        int prev = selectedBookmark.getVisitCount();
        selectedBookmark.visitCountUpdate(selectedBookmark.getVisitCount()+1);
        bookmarkRepository.save(selectedBookmark);

        bookmarks =  bookmarkRepository.findAll();

        
        

        //Then
        assertThat(bookmarks.size()).isEqualTo(1);
        assertThat(bookmarks.get(0))
            .hasFieldOrPropertyWithValue("name", "SpringFrameWork")
            .hasFieldOrPropertyWithValue("url", "www.blahblah.com")
            .hasFieldOrPropertyWithValue("comment", "JPA 기초")
            .hasFieldOrPropertyWithValue("folder", folder)
            .hasFieldOrPropertyWithValue("visitCount", prev+1);

    }

    @DisplayName("DB - 북마크 Delete")
    @Test
    void bookmarkRepoDeleteTest(){
        //Given
        bookmarkRepository.save(bookmark);

        //When

        List<Bookmark> bookmarks = bookmarkRepository.findByFolder(folder);
        assertThat(bookmarks.isEmpty()).isFalse();

        Bookmark selectedBookmark = bookmarks.get(0);
        bookmarkRepository.deleteById(selectedBookmark.getId()); // 이것도  flush
        bookmarks =  bookmarkRepository.findAll();
        
        //Then
        assertThat(bookmarks.size()).isEqualTo(0);
        assertThat(bookmarks).isEmpty();;
    }


    @DisplayName("DB - 북마크 Update visitCnt")
    @Test
    void visitCntUpdateTest(){
        //Given
        

        //When&Then
        assertThat(bookmark.visitCountUpdate(-1)).isFalse();
        assertThat(bookmark.visitCountUpdate(30)).isTrue();
        
    }
}
