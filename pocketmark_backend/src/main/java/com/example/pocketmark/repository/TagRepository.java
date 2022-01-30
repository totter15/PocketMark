package com.example.pocketmark.repository;

import java.util.List;

import com.example.pocketmark.domain.main.Tag;
import com.example.pocketmark.domain.main.Item.ItemPK;
import com.example.pocketmark.dto.main.TagDto.TagRes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>{
    //공유게시판 태그검색
    List<Tag> findByName(String name);

    //본인 게시물 태그검색 (name + userId)
    List<TagRes> findByNameAndUserId(String name, Long userId);

    //해당 아이템 태그 검색 (FK)
    List<TagRes> findByItemIdAndUserId(Long itemId, Long userId);
    default List<TagRes> findByFK(ItemPK pk){
        return findByItemIdAndUserId(pk.getItemId(), pk.getUserId());
    }

    //Read-ALL
    List<TagRes> findByUserId(Long userId);
}
