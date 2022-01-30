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
            item = itemRepository.getById(new ItemPK(itemId, userId));
            itemIdList.add(itemId);
            tags.add(singleReq.toEntity(item));
        }

        tagRepository.saveAll(tags);
        em.flush();
        em.clear();

        QItem qItem = QItem.item;
        JPAUpdateClause update= new JPAUpdateClause(em, qItem);
    
        update.set(qItem.isTagExist, true)
            .where(qItem.itemId.in(itemIdList))
            .execute();

        return true;
    }

    //ReadALL - for test
    @Transactional(readOnly = true)
    public List<TagRes> getAll(Long userId){
        return tagRepository.findByUserId(userId);
    }

    //Read ByName for specific User.
    @Transactional(readOnly = true)
    public List<TagRes> getTagsByName(String name, Long userId){
        return tagRepository.findByNameAndUserId(name, userId);
    }

    //Read By SuperEntity
    @Transactional(readOnly = true)
    public List<TagRes> getTagsByItemPK(ItemPK itemPK){
        return tagRepository.findByFK(itemPK);
    }

    //Delete
    //N*update
    @Transactional(readOnly = true)
    public boolean deleteTagsInBatch(TagDeleteBulkReq req, Long userId){
        QTag qTag = QTag.tag;
        // 설계가 잘못됬나...
        // Pk를 찾기위해 Select 를 N개 날리느냐, 
        // Update를 N 개 날리느냐...
        // 일단 Update 를 N개 날리자...
        // 추후 리팩토링 대상   
        for(TagDeleteReq singleReq : req.getTags()){
            JPAUpdateClause update= new JPAUpdateClause(em, qTag);
            
            update.set(qTag.deleted, true)
                .where(qTag.name.eq(singleReq.getName())
                .and(qTag.itemId.eq(singleReq.getItemId()))
                .and(qTag.userId.eq(userId)))
                .execute();            
        }

        // Tag 변경기록이 추후 데이터사이언스에서 유용한 정보로 사용할 수 있을까?
        // Category Classfication 에 사용될 수는 있겠지만, target vetor로 적절치 않음
        // Delete 안된게 Target Vector로 가치가있지 지운건 가치가없을 것 같은데..
        // 쓸모없는데 그냥 업데이트 치지말고 지울까?
        // 없는것보단 낫나...?

        



        return true;
    }






}
