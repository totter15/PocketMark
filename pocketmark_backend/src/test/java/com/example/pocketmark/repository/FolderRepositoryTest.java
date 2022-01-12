package com.example.pocketmark.repository;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.pocketmark.config.JpaConfig;
import com.example.pocketmark.configuration.TestConfig;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.QFolder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.service.FolderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

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
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.util.StringUtils;

//BDD mock
import static org.mockito.BDDMockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.TransactionScoped;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DB - 폴더")
// // @SpringBootTest
@DataJpaTest // 1. auditing 적용되게 하려면 메인app 에 @jpaauditing 달아주는 방법이 있음  || 2. Import(Myconfig.class)
@Import({JpaConfig.class,FolderQueryRepository.class,TestConfig.class})
// @SpringBootTest
// @Transactional
@ActiveProfiles("ping9")
public class FolderRepositoryTest {

    @Autowired private FolderRepository folderRepository;
    @Autowired private EntityManager em;
    @Mock UserRepository userRepository;

    // @Autowired private FolderService folderService;



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
        List<FolderRes> result = folderRepository.findByUserId(user.getId());
        
        //then
        assertEquals(result.get(0).getParent(), 0L);
        assertEquals(result.get(0).getDepth(), 1L);
        assertEquals(result.get(0).getName(),"요리블로그 모음");
        // assertEquals(result.get(0).get(), user.getId());
    }


    @DisplayName("DB - JPA QueryMethod Test (findByIn) ")
    @Test
    void findByInTest(){
        //given
        folderRepository.save(folder);
        folder = makeFolder(0L, 1L, "돼지두루치기", user);
        folderRepository.save(folder);
        folder = makeFolder(0L, 1L, "제이슨므라즈", user);
        folderRepository.save(folder);
        Set<Long> ids = new HashSet<>();
        ids.add(1L); ids.add(3L);
        

        //when
        List<Folder> folders = folderRepository.findFolderByIdInAndUserId(ids,1L);

        //then
        assertEquals("요리블로그 모음", folders.get(0).getName());
        assertEquals("제이슨므라즈", folders.get(1).getName());
    }


    // @Test
    // @Transactional
    // void findAllPersistenceTest(){
    //     folderRepository.save(folder);
    //     folder = makeFolder(0L, 1L, "돼지두루치기", user);
    //     folderRepository.save(folder);
    //     folder = makeFolder(0L, 1L, "돼지두루치기2", user);
    //     folderRepository.save(folder);

    //     // folder = folderRepository.findAll().get(0);

    //     FolderUpdateReq req = new FolderUpdateReq(1L,2L,2L,"변경된제목",0);
    //     folder.update(req.toServiceReq());

    //     folderRepository.findAll().get(0);


        
    // }


    // @Test
    // void querydslPersistenceSelectTest(){
    //     folderRepository.save(folder);
    //     folder = makeFolder(0L, 1L, "돼지두루치기", user);
    //     folderRepository.save(folder);
    //     folder = makeFolder(0L, 1L, "돼지두루치기2", user);
    //     folderRepository.save(folder);
    //     FolderUpdateReq req = new FolderUpdateReq(1L,2L,2L,"persist 호출 후",0);
    //     folder.update(req.toServiceReq());
    //     folderRepository.save(folder); //merge
    //     folder = folderRepository.findById(3L).get();
    //     System.out.println(folder);
    //     req = new FolderUpdateReq(1L,2L,2L,"merge 호출 후 ",0);
    //     folder.update(req.toServiceReq());
        
        

    //     QFolder qFolder = QFolder.folder;
    //     JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        
    //     // folder = folderRepository.findAll().get(0);

    //     // FolderUpdateReq req = new FolderUpdateReq(1L,2L,2L,"queryDSL 전",0);
    //     // folder.update(req);

    //     Folder findFolder = queryFactory
    //                     .selectFrom(qFolder)
    //                     .where(qFolder.id.eq(1L)).fetchOne();

    //     req = new FolderUpdateReq(1L,2L,2L,"변경된제목",0);
    //     findFolder.update(req.toServiceReq());

    //     //JPQL 이었음 ㄷㄷㄷ flush... 
    //     folderRepository.findAll().forEach(System.out::println);

        
    //     queryFactory.update(qFolder)
    //         .set(qFolder.name, "오리두루치기")
    //         .where(qFolder.id.eq(1L))
    //         .execute();

    //     folderRepository.findAll().forEach(System.out::println);
    //     // System.out.println(">>>>");
    //     // // em.getEntityManagerFactory().getCache().contains(cls, primaryKey);
    //     // // System.out.println(  em.getReference(QFolder.class, 1L).name.toString());
    //     // System.out.println( em.getMetamodel().getEntities().size());
    //     // em.getMetamodel().getEntities().forEach(System.out::println);

        

    //     // System.out.println("key set>>>>");
    //     // em.getProperties().keySet().forEach(System.out::println);
    //     // System.out.println("values >>>>");
    //     // em.getProperties().values().forEach(System.out::println);
        
    //     // em.getEntityManagerFactory().getCache().contains(cls, primaryKey);


    // }

    @Test
    void querydslPersistenceUpdateTest(){
        folderRepository.save(folder);
        folder = makeFolder(0L, 1L, "돼지두루치기", user);
        folderRepository.save(folder);
        folder = makeFolder(0L, 1L, "돼지두루치기2", user);
        folderRepository.save(folder);

        
        QFolder qFolder = QFolder.folder;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        
        queryFactory.update(qFolder)
            .set(qFolder.name, "오리두루치기")
            .where(qFolder.id.eq(1L))
            .execute();

        // JPAUpdateClause update = new JPAUpdateClause(em, qFolder);
        // update
        //     .set(qFolder.name, "오리두루치기")
        //     .where(qFolder.id.eq(1L))
        //     .execute();

        folderRepository.findAll().forEach(System.out::println);
        // em.flush();
        em.clear();

        System.out.println(">>> After em flush and Clear");
        folderRepository.findAll().forEach(System.out::println);
        
    }


    @Autowired FolderQueryRepository folderQueryRepository;
    @Test
    void querydslRepoTest(){
        folderRepository.save(folder);
        folder = makeFolder(0L, 1L, "돼지두루치기", user);
        folderRepository.save(folder);
        folder = makeFolder(0L, 1L, "제이슨므라즈", user);
        folderRepository.save(folder);
        Set<Long> ids = new HashSet<>();
        ids.add(1L); ids.add(3L);



        folderRepository.findByUserId(1L).forEach(System.out::println);
        folderRepository.findByUser_Id(1L).forEach(System.out::println);
        folderRepository.findOnlyIdByFolderIdInAndUserId(ids,1L).forEach(System.out::println);
    }

    @Test
    void stringTest(){
        String str = "";
        String str2=null;
        String str3=" ";
        String str4="          ";
        System.out.println(">>> : "+ StringUtils.hasText(str));
        System.out.println(">>> : "+ StringUtils.hasText(str2));
        System.out.println(">>> : "+ StringUtils.hasText(str3));
        System.out.println(">>> : "+ StringUtils.hasText(str4));
    }

    @Test
    void test(){
        Folder folder = Folder.builder().depth(1L).build();
        System.out.println(folder);
    }

    
    


    
}
