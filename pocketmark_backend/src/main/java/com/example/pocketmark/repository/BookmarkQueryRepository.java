package com.example.pocketmark.repository;


import java.util.Collection;
import java.util.List;

import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.QBookmark;
import com.example.pocketmark.dto.QBookmarkDto_BookmarkResImpl;
import com.example.pocketmark.dto.BookmarkDto.BookmarkResImpl;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateReq;

import com.querydsl.core.dml.UpdateClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Repository
public class BookmarkQueryRepository {
    private final JPAQueryFactory queryFactory;
    private QBookmark qBookmark = QBookmark.bookmark;

    // public List<BookmarkResImpl> getBoomarkByFolder_UserIdAndFolder_Depth(
    //     Long userId, Long depth, Long size, Long page
    // ){
    //     Long offset = (page-1)*size;
    //     return queryFactory
    //         .select(new QBookmarkDto_BookmarkResImpl(qBookmark.name, qBookmark.url, qBookmark.comment, qBookmark.folderId, qBookmark.visitCount))
    //         .from(qBookmark)
    //         .where(qBookmark.folder.userId.eq(userId)
    //         .and(qBookmark.folder.depth.eq(depth)))
    //         .orderBy(qBookmark.name.desc())
    //         .offset(offset)
    //         .limit(size)
    //         .fetch();
            
    // }

    public boolean exist(Long id){
        Bookmark fetchOne = queryFactory.selectFrom(qBookmark)
                        .where(qBookmark.id.eq(id)).fetchFirst();
        if(fetchOne ==null) return false;
        else return true;
    }

    public boolean existAll(Collection<Long> ids){
        List<Bookmark> fetch = queryFactory.selectFrom(qBookmark)
                        .where(qBookmark.id.in(ids)).limit(ids.size()).fetch();

        if(fetch.size() == ids.size()) return true;
        else return false;
    }


    public Long update(BookmarkUpdateReq req){
        UpdateClause<JPAUpdateClause> builder = queryFactory.update(qBookmark);
        
        if(StringUtils.hasText(req.getName())){
            builder.set(qBookmark.name, req.getName());
        }
        if(StringUtils.hasText(req.getUrl())){
            builder.set(qBookmark.url, req.getUrl());
        }
        if(StringUtils.hasText(req.getComment())){
            builder.set(qBookmark.comment, req.getComment());
        }
        if(req.getFolderId() != null){
            builder.set(qBookmark.folder.id, req.getFolderId());
        }
        if(req.getVisitCount() != null){
            builder.set(qBookmark.visitCount, req.getVisitCount());
        }

        // userId check needed to prevent JS attack from Hacker
        // should be coded in Service Layer
        return builder
                .where(qBookmark.id.eq(req.getId()))
                .execute();
    }
    
}
