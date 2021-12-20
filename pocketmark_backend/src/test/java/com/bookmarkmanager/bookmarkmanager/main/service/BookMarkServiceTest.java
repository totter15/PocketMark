package com.bookmarkmanager.bookmarkmanager.main.service;

import com.bookmarkmanager.bookmarkmanager.db.entity.Folder;
import com.bookmarkmanager.bookmarkmanager.db.repository.BookmarkRepository;
import com.bookmarkmanager.bookmarkmanager.db.repository.FolderRepository;
import com.bookmarkmanager.bookmarkmanager.db.service.BookMarkService;
import com.bookmarkmanager.bookmarkmanager.db.service.FolderService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BookMarkServiceTest {

    @Autowired
    private BookMarkService bookMarkService;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private FolderRepository folderRepository;


    @Test
    @Transactional
    void addBookmarkTest() {
        // Data.sql 사용하지말고, 테스트할때 만들어서 사용하도록
        // 외부에서 데이터를 주입하지말것, 내부에서 주입하고 내부에서 사용하고 내부에서 삭제되도록!

        //create folder // save 메서드있음
        // Folder folder = folderService.add("My Interest",1L);

        //transaction 안걸면 함수로 folder 넘어가면서 folder정보는 전달되나
        //위의 addFolder에서 save 를 호출했기에 영속성이 사라진상태로 가서 오류난거였음
        // bookMarkService.addBookmark("DB 무료 ERD 웹 사이트", folder);
        // bookMarkService.addBookmark("Spring 관련 인강 모음", folder);
        // bookMarkService.addBookmark("서머너즈워 꿀팁모음", folder);

        bookmarkRepository.findAll().forEach(System.out::println);
        folderRepository.findAll().forEach(System.out::println);
    }
}