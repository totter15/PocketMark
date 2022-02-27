package com.example.pocketmark.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import com.example.pocketmark.domain.main.Item;
import com.example.pocketmark.domain.main.QItem;
import com.example.pocketmark.domain.main.QTag;
import com.example.pocketmark.domain.main.Tag;
import com.example.pocketmark.domain.main.Item.ItemPK;
import com.example.pocketmark.dto.main.TagDto.TagCreateBulkReq;
import com.example.pocketmark.dto.main.TagDto.TagCreateReq;
import com.example.pocketmark.dto.main.TagDto.TagDeleteBulkReq;
import com.example.pocketmark.dto.main.TagDto.TagDeleteReq;
import com.example.pocketmark.dto.main.TagDto.TagIdOnly;
import com.example.pocketmark.dto.main.TagDto.TagRes;
import com.example.pocketmark.dto.main.TagDto.TagResWithItemId;
import com.example.pocketmark.dto.main.TagDto.TagCreateReq.TagCreateServiceReq;
import com.example.pocketmark.repository.ItemRepository;
import com.example.pocketmark.repository.TagRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    private final EntityManagerFactory emf;
    

    //Create
    // 1 Select + (if(new) N*insert) + (if(dup is exist) 1update) + 1 update
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean createTags(
        TagCreateBulkReq req, Long userId
    ){ 
        List<TagCreateReq> tagReqs = req.getTags();
        if(req.getTags().size()==0) return true;

        Set<String> tagIdList= tagReqs.stream().map(o->{
            return Tag.makePK(o.getItemId(), userId, o.getName());
        }).collect(Collectors.toSet());

        
        //지워진 태그랑 동일한 태그네임으로 다시 생성되었을때 체크
        List<String> duplicateIdList = tagRepository.findByIdIn(tagIdList).stream().map(TagIdOnly::getId).collect(Collectors.toList());
        //중복된 request 제거
        for(String tagPk :duplicateIdList){
            tagIdList.remove(tagPk);
        }

        List<Long> itemIdList = new ArrayList<>();
        //no select item query created
        Long itemId; Item item; 
        String name; List<Tag> tags=new ArrayList<>();
        for(String tagPk : tagIdList){
            itemId = Long.valueOf(tagPk.split(", ")[0]);
            name = tagPk.split(", ")[2];
            item = itemRepository.getById(Item.makePK(itemId, userId));
            itemIdList.add(itemId); // for item update
            tags.add(new Tag(itemId, userId, name, item));
        }
        tagRepository.saveAll(tags);

        em.flush();
        em.clear();
        
        

        // 지워진 태그 복구 
        QTag qTag = QTag.tag;
        JPAUpdateClause update= new JPAUpdateClause(em, qTag);
        if(duplicateIdList.size()!=0){
            update.set(qTag.deleted, false)
                .where(qTag.id.in(duplicateIdList))
                .execute();
        }

    

        
        

        // QItem qItem = QItem.item;
        // update= new JPAUpdateClause(em, qItem);
    
        // update.set(qItem.tagExist, true)
        //     .where(qItem.itemId.in(itemIdList))
        //     .execute();

        em.flush();
        em.clear();
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
    
    ///Read By SuperEntity - test
    @Transactional(readOnly = true)
    public List<TagResWithItemId> getTagsByItemPKIn(Collection<String> itemPKList){
        // return tagRepository.findByFK(itemPK);
        return tagRepository.findByItemPkIn(itemPKList);
    }
    


    //Delete
    //1*update
    @Transactional(readOnly = true)
    public boolean deleteTagsInBatch(TagDeleteBulkReq req, Long userId){
        QTag qTag = QTag.tag;
        
        //대리키로 N회 update -> 1회 update
        List<String> tagIdList= new ArrayList<>();
        for(TagDeleteReq singleReq : req.getTags()){
            tagIdList.add(
                Tag.makePK(singleReq.getItemId(),userId, singleReq.getName())
            );
        }
        
        JPAUpdateClause update= new JPAUpdateClause(em, qTag);
        
        update.set(qTag.deleted, true)
            .where(qTag.id.in(tagIdList))
            .execute();            

        // Tag 변경기록이 추후 데이터사이언스에서 유용한 정보로 사용할 수 있을까?
        // Category Classfication 에 사용될 수는 있겠지만, target vetor로 적절치 않음
        // Delete 안된게 Target Vector로 가치가있지 지운건 가치가없을 것 같은데..
        // 쓸모없는데 그냥 업데이트 치지말고 지울까?
        // 없는것보단 낫나...?

        



        return true;
    }






}
