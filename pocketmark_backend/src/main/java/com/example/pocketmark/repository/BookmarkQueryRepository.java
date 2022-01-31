package com.example.pocketmark.repository;


import java.util.Collection;
import java.util.List;

import com.example.pocketmark.domain.main.Bookmark;
import com.example.pocketmark.domain.main.QBookmark;
import com.example.pocketmark.dto.main.ItemDto.BookmarkUpdateReq.BookmarkUpdateServiceReq;
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

    //완료 - But fetch가 null 인경우 size()가 작동하는지 체크해야함
    //projection 으로 최소조회 처리하기 
    public boolean existAll(Collection<Long> itemIdList, Long userId){
        List<Bookmark> fetch = queryFactory.selectFrom(qBookmark)
                        .where(qBookmark.itemId.in(itemIdList).and(qBookmark.userId.eq(userId)))
                        .limit(itemIdList.size()).fetch();

        if(fetch.size() == itemIdList.size()) return true;
        else return false;
    }


    // 완료
    // (Name, URL, Comment, ParentId, VisitCount)
    public Long update(BookmarkUpdateServiceReq req,Long userId){
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
        if(req.getParentId() != null){
            builder.set(qBookmark.parentId, req.getParentId());
        }
        if(req.getVisitCount() != null){
            builder.set(qBookmark.visitCount, req.getVisitCount());
        }

        // userId check needed to prevent JS attack from Hacker
        // should be coded in Service Layer
        return builder
                .where(qBookmark.itemId.eq(req.getItemId())
                .and(qBookmark.userId.eq(userId)))
                .execute();
    }
    
}
