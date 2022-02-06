package com.example.pocketmark.repository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.pocketmark.domain.main.Folder;
import com.example.pocketmark.domain.main.Item;
import com.example.pocketmark.domain.main.QFolder;
import com.example.pocketmark.domain.main.QItem;
import com.example.pocketmark.dto.main.ItemDto.FolderUpdateReq.FolderUpdateServiceReq;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Repository
public class FolderQueryRepository {
    private final JPAQueryFactory queryFactory;
    private QFolder qFolder = QFolder.folder;
    private QItem qItem = QItem.item;

    
    //완료 - But fetch가 null 인경우 size()가 작동하는지 체크해야함 
     
    //qFolder 도 JPQL이므로 하이버네이트가 조인처리함 (중요)
    //qItem 으로 변경 -> Hibernate 디폴트로 조인여전함
    //projection 으로 최소조회 처리하기 
    public boolean existAll(Collection<Long> itemIdList, Long userId){
        List<Item> fetch = queryFactory.selectFrom(qItem)
                        .where(qItem.itemId.in(itemIdList).and(qItem.userId.eq(userId)))
                        .limit(itemIdList.size()).fetch();
        
        // fetch에 0개 결과가돌아오면 빈 리스트 -> size작동함 
        // System.out.println("existAll(Folder)>> : "+fetch);

        if(fetch.size()==itemIdList.size()) return true;
        else return false;
    }


    //no snapshot and persistence needed
    //완료 
    //업데이트 시 조인발생할거임 
    //qItem 필드 따로, qFolder 따로 업데이트를 치는게 나을까?
    public Long update(FolderUpdateServiceReq req, Long userId){
        UpdateClause<JPAUpdateClause> builder = queryFactory.update(qFolder);
        
        if(StringUtils.hasText(req.getName())){ // name
            builder.set(qFolder.name, req.getName());
        }
        if(req.getParentId() != null){ //parentId
            builder.set(qFolder.parentId, req.getParentId());
        }
        if(req.getVisitCount() != null){ //visitCount
            builder.set(qFolder.visitCount, req.getVisitCount());
        }

        // userId check needed to prevent JS attack from Hacker
        // should be coded in Service Layer
        return builder
                .where(qFolder.itemId.eq(req.getItemId())
                .and(qFolder.userId.eq(userId)))
                .execute(); //Where PK
    }

    private BooleanExpression eqName(String name){
        if(StringUtils.hasText(name)){
            return null;
        }
        return qFolder.name.eq(name);
    }

    private BooleanExpression eqParentId(Long parent){
        if(parent== null){
            return null;
        }
        return qFolder.parentId.eq(parent);
    }

    
    
}
