package com.example.pocketmark.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.example.pocketmark.domain.main.Item;
import com.example.pocketmark.domain.main.QItem;
import com.example.pocketmark.domain.main.QTag;
import com.example.pocketmark.domain.main.Tag;
import com.example.pocketmark.domain.main.Item.ItemPK;
import com.example.pocketmark.dto.main.TagDto.TagCreateBulkReq;
import com.example.pocketmark.dto.main.TagDto.TagCreateReq;
import com.example.pocketmark.dto.main.TagDto.TagDeleteBulkReq;
import com.example.pocketmark.dto.main.TagDto.TagDeleteReq;
import com.example.pocketmark.dto.main.TagDto.TagRes;
import com.example.pocketmark.dto.main.TagDto.TagResWithItemId;
import com.example.pocketmark.dto.main.TagDto.TagCreateReq.TagCreateServiceReq;
import com.example.pocketmark.repository.ItemRepository;
import com.example.pocketmark.repository.TagRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    /* No Update Here, Tag is Immutable Entity */

    private final TagRepository tagRepository;
    private final ItemRepository itemRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;
    

    //Create
    // N*insert + 1 update
    public boolean createTags(
        TagCreateBulkReq req, Long userId
    ){ 
        List<Tag> tags = new ArrayList<>();
        
        List<Long> itemIdList = new ArrayList<>();
        //no select item query created
        Long itemId; Item item;
        for(TagCreateReq singleReq : req.getTags()){
            itemId = singleReq.getItemId();
            // item = itemRepository.getById(new ItemPK(itemId, userId));
            item = itemRepository.getById(Item.makePK(itemId, userId));
            itemIdList.add(itemId);
            tags.add(singleReq.toEntity(itemId,userId,item));
        }

        tagRepository.saveAll(tags);
        em.flush();
        em.clear();

        QItem qItem = QItem.item;
        JPAUpdateClause update= new JPAUpdateClause(em, qItem);
    
        update.set(qItem.tagExist, true)
            .where(qItem.itemId.in(itemIdList))
            .execute();

        return true;
    }

    //ReadALL - for test
    @Transactional(readOnly = true)
    public List<TagRes> getAll(Long userId){
        return tagRepository.findByUserId(userId);
    }

    //본인 게시물 태그검색
    @Transactional(readOnly = true)
    public List<TagRes> getTagsByNameAndUserId(String name, Long userId){
        return tagRepository.findByNameAndUserId(name, userId);
    }

    //공유게시판 태그검색 (itemId 로 상위엔티티 역추적해야함)
    @Transactional(readOnly = true)
    public List<TagResWithItemId> getTagsByName(String name){
        return tagRepository.findByName(name);
    }
    //Read By SuperEntity
    @Transactional(readOnly = true)
    public List<TagRes> getTagsByItemPK(String itemPK){
        // return tagRepository.findByFK(itemPK);
        return tagRepository.findByItemPk(itemPK);
    }

    //Delete
    //1*update
    @Transactional(readOnly = true)
    public boolean deleteTagsInBatch(TagDeleteBulkReq req, Long userId){
        QTag qTag = QTag.tag;
        
        //대리키로 N회 update -> 1회 update
        List<String> itemPkList= new ArrayList<>();
        for(TagDeleteReq singleReq : req.getTags()){
            itemPkList.add(
                Item.makePK(singleReq.getItemId(), userId) 
            );
        }
        
        JPAUpdateClause update= new JPAUpdateClause(em, qTag);
        
        update.set(qTag.deleted, true)
            .where(qTag.itemPk.in(itemPkList))
            .execute();            

        // Tag 변경기록이 추후 데이터사이언스에서 유용한 정보로 사용할 수 있을까?
        // Category Classfication 에 사용될 수는 있겠지만, target vetor로 적절치 않음
        // Delete 안된게 Target Vector로 가치가있지 지운건 가치가없을 것 같은데..
        // 쓸모없는데 그냥 업데이트 치지말고 지울까?
        // 없는것보단 낫나...?

        



        return true;
    }






}
