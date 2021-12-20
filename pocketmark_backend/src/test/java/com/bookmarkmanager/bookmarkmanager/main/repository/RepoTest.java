package com.bookmarkmanager.bookmarkmanager.main.repository;

import java.util.List;

import com.bookmarkmanager.bookmarkmanager.db.entity.Bookmark;
import com.bookmarkmanager.bookmarkmanager.db.entity.Folder;
import com.bookmarkmanager.bookmarkmanager.db.entity.User;
import com.bookmarkmanager.bookmarkmanager.db.repository.BookmarkRepository;
import com.bookmarkmanager.bookmarkmanager.db.repository.FolderRepository;
import com.bookmarkmanager.bookmarkmanager.db.repository.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
public class RepoTest {
 
    @Autowired
    UserRepository userRepository;
    @Autowired
    FolderRepository folderRepository;
    @Autowired
    BookmarkRepository bookmarkRepository;

    //@Before : Test 갯수만큼 Test전에 실행
    //@BeforeAll : 클래스내에서 딱 한번만 실행(static)  ;; 주의점,, spring context 로드(DB만들어지기)전에 실행됨..
    @BeforeEach
    public void setEnv(){
        System.out.println("# >>> Before Test Class : Excuting");
        makeData("sim2280", "재미있는 IT지식", "HTTP 1.1 vs 2.0");
        makeData("sim2626", "흥미로운 요리영상", "영국의 도시락 [스카치에그]");
        makeData("f2522", "Kaggle Data Competiton", "SanFranCisco Crime Classification");
        System.out.println("# >>> Before Test Class : Excuted");
    }


    @Test
    public void showSchema(){
        System.out.println("#Excuted done");
    }


    @Transactional
    public void makeData(String id, String folderName, String BookName){
         
        User user = User.builder()
                    .userId(id)
                    .userPw("1234")
                    .userEmail("sim2280@naver.com")
                    .build();
        Folder folder = Folder.builder()
                    .name(folderName)
                    .parent(null)
                    .user(user)
                    .build();
        Bookmark bookmark = Bookmark.builder()
                    .name(BookName)
                    .url("www.naver.com")
                    .folder(folder)
                    .build();
        // System.out.println("ㅇㄷㅇㄷ?222");
        // userRepository.save(user);
        // folderRepository.save(folder);
        // builder로 연관관계를 주입받은 최종객체를 persist 해야함 
        bookmarkRepository.save(bookmark);
        // System.out.println("ㅇㄷㅇㄷ?333");
    }

    

    @Test
    public void userRepoTest(){
        

        System.out.println("# >>> User Repo");
        userRepository.findAll().forEach(System.out::println);
        System.out.println("# >>> Folder Repo");
        folderRepository.findAll().forEach(System.out::println);
        System.out.println("# >>> Bookmark Repo");
        bookmarkRepository.findAll().forEach(System.out::println);
        
        // 특정 유저 하위 데이터 모두 가져오기
        
        System.out.println("# >>> User Repo / FindByUserId");
        System.out.println("# >>> "+userRepository.findByUserId("sim2280"));
        
        
        System.out.println("# >>> getFolders()");
        List<Folder> result= userRepository.findByUserId("sim2280").getFolders();
        result.forEach(System.out::println);

    }
    
}
