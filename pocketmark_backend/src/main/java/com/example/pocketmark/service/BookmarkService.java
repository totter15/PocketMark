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
import com.example.pocketmark.domain.main.Item;
import com.example.pocketmark.domain.main.QBookmark;
import com.example.pocketmark.domain.main.QItem;
import com.example.pocketmark.domain.main.Item.ItemPK;
import com.example.pocketmark.dto.main.ItemDto.BookmarkCreateReq;
import com.example.pocketmark.dto.main.ItemDto.BookmarkRes;
import com.example.pocketmark.dto.main.ItemDto.BookmarkResWithTag;
import com.example.pocketmark.dto.main.ItemDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.main.ItemDto.BookmarkUpdateReq.BookmarkUpdateServiceReq;
import com.example.pocketmark.dto.main.TagDto.TagRes;
import com.example.pocketmark.dto.main.TagDto.TagResImpl;
import com.example.pocketmark.dto.main.TagDto.TagResWithItemId;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.BookmarkQueryRepository;
import com.example.pocketmark.repository.BookmarkRepository;
import com.example.pocketmark.repository.FolderQueryRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.ItemRepository;
import com.querydsl.jpa.impl.JPAUpdateClause;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
    private final TagService tagService;

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
    public List<BookmarkResWithTag> getAllBookmarks(Long userId){
        List<BookmarkRes> bookmarkResList = bookmarkRepository.findByUserId(userId);
        List<BookmarkResWithTag> result = new ArrayList<>();
        
        //BatchSize 사용하려면 DTO 가 아닌 엔티티로 불러와야 됨. 
        //즉, Full Select 실행 
        
        Set<String> ItemPkList = bookmarkResList.stream()
                            .map(it ->{
                                return Item.makePK(it.getItemId(), userId);
                            })
                            .collect(Collectors.toSet());
        

        Map<Long,List<String>> tagMap = tagService.getTagsByItemPKIn(ItemPkList)
                                    .stream()
                                    .collect(Collectors.groupingBy(
                                        TagResWithItemId::getItemId, 
                                        Collectors.mapping(TagResWithItemId::getName, Collectors.toList())
                                    ));
               
                        
        for(BookmarkRes it : bookmarkResList){
            List<TagRes> tags = new ArrayList<>();
            List<String> names = tagMap.get(it.getItemId());
            if(names != null){
                for(String tagName : names){
                    tags.add(new TagResImpl(tagName));
                }
            }
            result.add(
                new BookmarkResWithTag(it.getItemId(), it.getParentId(), it.getName(), it.getUrl(), it.getComment(), tags.size()==0?null:tags, it.getVisitCount())
            );
        }

        return result;
        // return bookmarkRepository.findByUserId(userId);
    }

    //Read-By ParentId - 완료
    public Slice<BookmarkResWithTag> getBoomarkByParentId(Long userId, Long parentId, Pageable pageable){
        

        Slice<BookmarkRes> bookmarkResList = bookmarkRepository.findByUserIdAndParentId(userId, parentId, pageable);
        List<BookmarkResWithTag> result = new ArrayList<>();

        boolean hasNext = bookmarkResList.hasNext();
        
        Set<String> ItemPkList = bookmarkResList.stream()
                            .map(it ->{
                                return Item.makePK(it.getItemId(), userId);
                            })
                            .collect(Collectors.toSet());
        

        Map<Long,List<String>> tagMap = tagService.getTagsByItemPKIn(ItemPkList)
                                    .stream()
                                    .collect(Collectors.groupingBy(
                                        TagResWithItemId::getItemId, 
                                        Collectors.mapping(TagResWithItemId::getName, Collectors.toList())
                                    ));
               
                        
        for(BookmarkRes it : bookmarkResList){
            List<TagRes> tags = new ArrayList<>();
            List<String> names = tagMap.get(it.getItemId());
            if(names != null){
                for(String tagName : names){
                    tags.add(new TagResImpl(tagName));
                }
            }
            result.add(
                new BookmarkResWithTag(it.getItemId(), it.getParentId(), it.getName(), it.getUrl(), it.getComment(), tags.size()==0?null:tags, it.getVisitCount())
            );
        }

        return new SliceImpl<BookmarkResWithTag>(result, pageable, hasNext);
        
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
        System.out.println("B >>> :"+itemIdList);

        if(itemIdList==null || itemIdList.size() == 0
            || !bookmarkQueryRepository.existAll(itemIdList,userId)){
            return;
        }

        // qBookmark 로 업데이트문 어떻게 날라가는지 확인 후 qItem 으로 바꿀것 
        /*
        update
        item 
    set
        deleted=? 
    where
        (
            pk
        ) in (
            select
                bookmark0_.pk as pk 
            from
                bookmark bookmark0_ 
            inner join
                item bookmark0_1_ 
                    on bookmark0_.pk=bookmark0_1_.pk 
            where
                (
                    deleted = 0
                ) 
                and bookmark0_1_.item_id=? 
                and bookmark0_1_.user_id=?
        )
        */
        QItem qItem = QItem.item;
        // QBookmark qBookmark = QBookmark.bookmark;
        // dsl 도 JPQL기반이라 item도 Hibernate떄문에 조인됨.... ㄹㅇ....
        JPAUpdateClause update = new JPAUpdateClause(em, qItem);
            update
                .set(qItem.deleted, true)
                .where(qItem.itemId.in(itemIdList).and(qItem.userId.eq(userId)))
                .execute();
            //where 절 요소는 PK임 
        em.flush();
        em.clear();
        
    }

    

}
