package com.example.pocketmark.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.main.Bookmark;
import com.example.pocketmark.domain.main.Folder;
import com.example.pocketmark.domain.main.QBookmark;
import com.example.pocketmark.domain.main.QItem;
import com.example.pocketmark.domain.main.Item.ItemPK;
import com.example.pocketmark.dto.main.ItemDto.BookmarkCreateReq;
import com.example.pocketmark.dto.main.ItemDto.BookmarkRes;
import com.example.pocketmark.dto.main.ItemDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.main.ItemDto.BookmarkUpdateReq.BookmarkUpdateServiceReq;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.BookmarkQueryRepository;
import com.example.pocketmark.repository.BookmarkRepository;
import com.example.pocketmark.repository.FolderQueryRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.ItemRepository;
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
    private final BookmarkQueryRepository bookmarkQueryRepository;
    private final EntityManager em;

    //Create - 완료
    @Transactional
    public boolean saveAllByCreateReq(
        List<BookmarkCreateReq> req,
        Long userId
    ){
        if(req.size() ==0) return true;

        // folder Persist 이후 (영속성 존재)

        List<Bookmark> bookmarks= new ArrayList<>();
        for(BookmarkCreateReq singleReq : req){
            bookmarks.add(singleReq.toEntity(userId));            
        }
        
        bookmarkRepository.saveAll(bookmarks);
        return true;
    }



    //Read-ALL - 완료
    public List<BookmarkRes> getAllBookmarks(Long userId){
        return bookmarkRepository.findByUserId(userId);
    }

    //Read-By ParentId - 완료
    public Slice<BookmarkRes> getBoomarkByParentId(Long userId, Long parentId, Pageable pageable){
        return bookmarkRepository.findByUserIdAndParentId(userId, parentId, pageable);
    }

    //Update - 완료
    public void updateBookmarksInBatch(List<BookmarkUpdateReq> req, Long userId){
        Set<Long> bookmarkIdSet = req.stream().map(BookmarkUpdateReq::getItemId).collect(Collectors.toSet());
        if(bookmarkQueryRepository.existAll(bookmarkIdSet, userId)){
            for(BookmarkUpdateReq singleReq : req){
                BookmarkUpdateServiceReq updateServiceReq = singleReq.toServiceReq();
                bookmarkQueryRepository.update(updateServiceReq, userId);
            }
            em.flush();
            em.clear();
        }else{
            throw new GeneralException(ErrorCode.INCLUDING_NON_EXIST_DATA);
        }

    }

    //Delete - 완료
    public void deleteBookmarksInBatch(List<Long> itemIdList, Long userId){
        //ItemPk = itemId + userId

        if(itemIdList.size() == 0
            || !bookmarkQueryRepository.existAll(itemIdList,userId)){
            return;
        }

        // qBookmark 로 업데이트문 어떻게 날라가는지 확인 후 qItem 으로 바꿀것 
        // QItem qItem = QItem.item;
        QBookmark qBookmark = QBookmark.bookmark;
        JPAUpdateClause update = new JPAUpdateClause(em, qBookmark);
            update
                .set(qBookmark.deleted, true)
                .where(qBookmark.itemId.in(itemIdList).and(qBookmark.userId.eq(userId)))
                .execute();
            //where 절 요소는 PK임 
        em.flush();
        em.clear();
        
    }

    

}
