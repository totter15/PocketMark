package com.example.pocketmark.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.QBookmark;
import com.example.pocketmark.dto.BookmarkDto.BookmarkCreateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;
import com.example.pocketmark.dto.BookmarkDto.BookmarkResImpl;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateServiceReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkCreateReq.BookmarkCreateServiceReq;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.BookmarkQueryRepository;
import com.example.pocketmark.repository.BookmarkRepository;
import com.example.pocketmark.repository.FolderQueryRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.querydsl.jpa.impl.JPAUpdateClause;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkQueryRepository BookmarkQueryRepository;
    private final FolderRepository folderRepository;
    private final FolderQueryRepository folderQueryRepository;
    private final EntityManager em;

    // //c
    public BookmarkResImpl saveByCreateReq(BookmarkCreateServiceReq req){
        Folder folder = folderRepository.getById(req.getFolderId());
        Bookmark bookmark= req.toEntity(folder);
        
        return bookmarkRepository.save(bookmark).toJson();
    }

    // @Transactional
    // public boolean saveAllByCreateReq(List<BookmarkCreateReq> req){
    //     Folder folder;
        
    //     List<Bookmark> bookmarks= new ArrayList<>();
    //     for(BookmarkCreateReq singleReq : req){
    //         folder = folderRepository.getById(singleReq.getTempFolderId());
    //         bookmarks.add(singleReq.toEntity(folder));
    //     }
    //     bookmarkRepository.saveAll(bookmarks);

    //     return true;
    // }
    @Transactional
    public boolean saveAllByCreateReq(
        List<BookmarkCreateReq> req,
        Long userId
    ){
        if(req.size() ==0) return true;
        
        Set<Long> folderIdSet = req.stream()
                            .map(BookmarkCreateReq::getFolderId)
                            .collect(Collectors.toSet());

        // (Key,Value) - (tempFolderId, DBFolderId)
        // 눈물의 select......
        // folder Persist 이후 
        Map<Long,Long> map =folderQueryRepository.getFoldersIdMapByFolderId(userId, folderIdSet);

        
        Folder folder;
        List<Bookmark> bookmarks= new ArrayList<>();
        for(BookmarkCreateReq singleReq : req){
            folder = folderRepository.getById(map.get(singleReq.getFolderId()));
            bookmarks.add(singleReq.toEntity(folder, userId));            
        }
        
        bookmarkRepository.saveAll(bookmarks);
        return true;
    }




    //r
    public List<BookmarkRes> getBookmark(Long folderId){
        return bookmarkRepository.findByFolderId(folderId);
    } 

    public Slice<BookmarkRes> getBoomarkByFolderId(Long userId, Long folderId, Pageable pageable){
        return bookmarkRepository.findByFolder_UserIdAndFolderId(userId, folderId, pageable);
    }


    // //u
    public BookmarkResImpl updateBookmark(BookmarkUpdateServiceReq req, Long bookmarkId){
        Optional<Bookmark> bookmark= bookmarkRepository.findById(bookmarkId);
        if(bookmark.isPresent()){
            bookmark.get().update(req);
            return bookmarkRepository.save(bookmark.get()).toJson();
        }else{
            throw new GeneralException(ErrorCode.NOT_FOUND);
        }

    }


    public void deleteBookmarkBySelfId(Long bookmarkId, Long userId){
        //suggeted by Yamashiro Rion
        if(BookmarkQueryRepository.exist(bookmarkId)){ // no count query
            if(bookmarkRepository.findById(bookmarkId).get().isDeleted()){ // check if it's already deleted
                return;
            }
            QBookmark qBookmark = QBookmark.bookmark;
            JPAUpdateClause update = new JPAUpdateClause(em, qBookmark);
            update
                .set(qBookmark.deleted, true)
                .where(qBookmark.id.eq(bookmarkId))
                .execute();
        }
        //SQL 구문 throw
        em.flush();
    }

    public void deleteBookmarksInBatch(List<Long> IdList, Long userId){
        if(IdList.size() == 0
            || !BookmarkQueryRepository.existAll(IdList)){
            return;
        }

        QBookmark qBookmark = QBookmark.bookmark;
        JPAUpdateClause update = new JPAUpdateClause(em, qBookmark);
            update
                .set(qBookmark.deleted, true)
                .where(qBookmark.id.in(IdList).and(qBookmark.userId.eq(userId)))
                .execute();
                

        em.flush();
        em.clear();
        
    }

    

}
