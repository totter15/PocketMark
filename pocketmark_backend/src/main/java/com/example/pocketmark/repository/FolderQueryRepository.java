package com.example.pocketmark.repository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.QFolder;
import com.example.pocketmark.dto.QFolderDto_FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;

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

    @Deprecated
    public List<FolderResImpl> getFoldersByUserIdAndDepth(
        Long userId, Long depth, Long size, Long page
    ){
        Long offset = (page-1)*size;
        return queryFactory
            .select(new QFolderDto_FolderResImpl(qFolder.id, qFolder.userId, qFolder.parent, qFolder.depth, qFolder.name, qFolder.visitCount))
            .from(qFolder)
            .where(qFolder.userId.eq(userId)
            .and(qFolder.depth.eq(depth)))
            .orderBy(qFolder.name.desc())
            .offset(offset)
            .limit(size)
            .fetch();
    }

    public Map<Long,Long> getFoldersIdMapByFolderId(
        Long userId, Set<Long> folderIdSet
    ){
        return queryFactory
            .select(new QFolderDto_FolderResImpl(qFolder.id, qFolder.folderId))
            .from(qFolder)
            .where(qFolder.userId.eq(userId).and(qFolder.folderId.in(folderIdSet)))
            .fetch()
            .stream().collect(Collectors.toMap(it->it.getFolderId(), it->it.getId()));
            
    }






    public boolean isExist(Long id){
        Folder fetchOne = queryFactory.selectFrom(qFolder)
                        .where(qFolder.id.eq(id)).fetchFirst();
        if(fetchOne ==null) return false;
        else return true;
    }
    public boolean isExistWithUserId(Long id, Long userId){
        Folder fetchOne = queryFactory.selectFrom(qFolder)
                        .where(qFolder.id.eq(id).and(qFolder.userId.eq(userId))).fetchFirst();
        if(fetchOne ==null) return false;
        else return true;
    }
    public boolean isAllExistWithUserId(List<Long> idList, Long userId){
        List<Folder> fetchList = queryFactory.selectFrom(qFolder)
                        .where(qFolder.id.in(idList).and(qFolder.userId.eq(userId)))
                        .limit(idList.size())
                        .fetch();
        if(fetchList ==null || fetchList.size()!=idList.size() ) return false;
        else return true;
    }

    @Transactional(readOnly = true)
    public List<Folder> findByAll(String name, Long parent, Long depth, Pageable pageable){
        return queryFactory.selectFrom(qFolder)
                .where(
                    eqName(name),
                    eqParent(parent),
                    eqDepth(depth))
                .orderBy(qFolder.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    //no snapshot and persistence needed
    public Long update(FolderUpdateReq req){
        UpdateClause<JPAUpdateClause> builder = queryFactory.update(qFolder);
        
        if(StringUtils.hasText(req.getName())){
            builder.set(qFolder.name, req.getName());
        }
        if(req.getParent() != null){
            builder.set(qFolder.parent, req.getParent());
        }
        if(req.getDepth() != null){
            builder.set(qFolder.depth, req.getDepth());
        }
        if(req.getVisitCount() != null){
            builder.set(qFolder.visitCount, req.getVisitCount());
        }

        // userId check needed to prevent JS attack from Hacker
        // should be coded in Service Layer
        return builder
                .where(qFolder.id.eq(req.getId()))
                .execute();
    }

    private BooleanExpression eqName(String name){
        if(StringUtils.hasText(name)){
            return null;
        }
        return qFolder.name.eq(name);
    }

    private BooleanExpression eqParent(Long parent){
        if(parent== null){
            return null;
        }
        return qFolder.parent.eq(parent);
    }
    private BooleanExpression eqDepth(Long depth){
        if(depth== null){
            return null;
        }
        return qFolder.depth.eq(depth);
    }

    
    
}
