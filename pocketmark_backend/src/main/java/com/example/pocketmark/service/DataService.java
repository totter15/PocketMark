package com.example.pocketmark.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.QBookmark;
import com.example.pocketmark.domain.QFolder;
import com.example.pocketmark.dto.BookmarkDto.BookmarkCreateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkOnlyId;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;
import com.example.pocketmark.dto.BookmarkDto.BookmarkResImpl;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.DataDto.DataCreateReq;
import com.example.pocketmark.dto.DataDto.DataDeleteReq;
import com.example.pocketmark.dto.DataDto.DataDeleteServiceReq;
import com.example.pocketmark.dto.DataDto.DataRes;
import com.example.pocketmark.dto.DataDto.DataUpdateReq;
import com.example.pocketmark.dto.DataDto.DataUpdateServiceReq;
import com.example.pocketmark.dto.DataDto.DataCreateReq.DataCreateServiceReq;
import com.example.pocketmark.dto.FolderDto.FolderOnlyId;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.BookmarkQueryRepository;
import com.example.pocketmark.repository.BookmarkRepository;
import com.example.pocketmark.repository.FolderQueryRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DataService {
    private final FolderRepository folderRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FolderQueryRepository folderQueryRepository;
    private final BookmarkQueryRepository bookmarkQueryRepository;
    private final FolderService folderService;
    private final BookmarkService bookmarkService;
    private final EntityManager em;

    // @Transactional //같은 트랜잭션으로 못묶으면 folder 통과하고 bookmark에서 에러나면? 거참,,, 
    // 근데 같은 트랜잭션이면 영속성 공유되지않나? 왜 못찾지? (추후 개선필요) 
    // (나중에 시간나면 getOne 말고 findby로 실제 쿼리 날라가는지 확인할것)
    @Transactional
    public boolean createData(DataCreateServiceReq req,Long userId){
        //error 나면 롤백 
        Map<Long,Long> map = folderService.saveAllByCreateReq(req.getFolders(), userId);
        bookmarkService.saveAllByCreateReq(req.getBookmarks(),userId);
        return true;
    }


    @Transactional(readOnly = true)
    public boolean deleteData(DataDeleteServiceReq req, Long userId){
        try{
            folderService.deleteFoldersInBatch(req.getFolderIdList(), userId);
            bookmarkService.deleteBookmarksInBatch(req.getBookmarkIdList(), userId);
        }catch(Exception e){//rollback..
            return false;
        }

        return true;
        
    }


    @Transactional(readOnly = true)
    public DataRes getData(Long userId, Long depth, Pageable pageable){
        if(depth != null && userId != null){
            List<FolderRes> folders = folderService.getFoldersByDepth(userId, depth, pageable).getContent();
            List<BookmarkRes> bookmarks = bookmarkService.getBoomarkByFolderDepth(userId, depth-1L,pageable).getContent();
            DataRes data = DataRes.builder().depth(depth).folders(folders).bookmarks(bookmarks).build();
            return data;
        }else{
            return null;
        }
    }


    @Transactional(readOnly = true)
    public void updateData(DataUpdateServiceReq req, Long userId){
        List<FolderUpdateReq> folders;
        List<BookmarkUpdateReq> bookmarks;
        List<FolderOnlyId> dbFolders;
        List<BookmarkOnlyId> dbBookmarks;

        //폴더만 업데이트 (이름, 위치(parent,depth))
        if(req.getBookmarks().size()==0){
            folders =req.getFolders();
            
            Map<Long, FolderUpdateReq> reqMap = 
                folders.stream()
                .collect(Collectors.toMap(it->it.getId() , it->it));
            //n번 호출로 영속성을 가질것인가, QueryDSL 1번 호출로 성능상이점을 가질것인가
            //그것이 문제로다 (아니면 JPA method 1번호출로 영속성가지는게..?)
            // ** 영속성 있음

            // 악의적 스크립트 공격 방지 ( userid 검증 ) 는 Service 단에서?
            // Filter에서 매번하기엔 조회비용이 아까움
            dbFolders = folderRepository.findByIdInAndUserId(reqMap.keySet(),userId);
            if(reqMap.size() != dbFolders.size()){
                throw new GeneralException(ErrorCode.INVALID_DATA_ACCESS_REQUEST);
            }
            // for(Folder dbFolder : dbFolders){
            //     FolderUpdateReq updateReq = reqMap.get(dbFolder.getId());
            //     dbFolder.update(updateReq);
            // }
            // folderRepository.saveAll(dbFolders);


            // sql 저장소에 쌓아두고
            for(FolderOnlyId dbFolder : dbFolders){
                FolderUpdateReq updateReq = reqMap.get(dbFolder.getId());
                folderQueryRepository.update(updateReq);
            }
            // 쌓아둔 sql문 날리고 트랜잭션 종료
            em.flush();
            // DataService 영속성 clear 하면 Tread-Safe 한지 확인필요
            // Readonly 라 clear 안해도 되긴함            

        }
        //북마크만 업데이트 (이름, url, comment, folder_id, visitCount)
        else if(req.getFolders().size()==0){
            bookmarks =req.getBookmarks();
            Map<Long, BookmarkUpdateReq> reqMap= 
                bookmarks.stream()
                .collect(Collectors.toMap(it->it.getId(), it->it));

            dbBookmarks = bookmarkRepository.findByIdIn(reqMap.keySet());
            if(reqMap.size() != dbBookmarks.size()){
                throw new GeneralException(ErrorCode.INVALID_DATA_ACCESS_REQUEST);
            }


            for(BookmarkOnlyId dbObj : dbBookmarks){
                BookmarkUpdateReq updateReq = reqMap.get(dbObj.getId());
                bookmarkQueryRepository.update(updateReq);
            }
            em.flush();
        }
        //모두 업데이트        
        else{
            folders =req.getFolders();
            bookmarks =req.getBookmarks();
            Map<Long, FolderUpdateReq> reqFolderMap = 
                folders.stream()
                .collect(Collectors.toMap(it->it.getId() , it->it));
            Map<Long, BookmarkUpdateReq> reqBookmarkMap= 
                bookmarks.stream()
                .collect(Collectors.toMap(it->it.getId(), it->it));

            dbFolders = folderRepository.findByIdInAndUserId(reqFolderMap.keySet(),userId);
            dbBookmarks = bookmarkRepository.findByIdIn(reqBookmarkMap.keySet());
            if(reqFolderMap.size() != dbFolders.size()
                || reqBookmarkMap.size() != dbBookmarks.size()){
                throw new GeneralException(ErrorCode.INVALID_DATA_ACCESS_REQUEST);
            }
            
            for(FolderOnlyId dbFolder : dbFolders){
                FolderUpdateReq updateReq = reqFolderMap.get(dbFolder.getId());
                folderQueryRepository.update(updateReq);
            }
            for(BookmarkOnlyId dbObj : dbBookmarks){
                BookmarkUpdateReq updateReq = reqBookmarkMap.get(dbObj.getId());
                bookmarkQueryRepository.update(updateReq);
            }

            em.flush();
        }
    }

    // no create and delete in batch
    // to log all




}
