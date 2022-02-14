package com.example.pocketmark.repository;

import java.util.Collection;
import java.util.List;

import com.example.pocketmark.domain.main.Item;
import com.example.pocketmark.domain.main.Item.ItemPK;
import com.example.pocketmark.dto.main.ItemDto.ItemIdOnly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, String> {
    //DateService - Delete
    List<ItemIdOnly> findItemIdOnlyByParentIdInAndUserId(
        Collection<Long> parentIdList, Long userId);
    
    //get Last itemId (it wouldn't be null) && should ignore @Where
    //엔티티레벨의 @where를 지우고 쿼리메소드에 @where를 다 붙여주는 방법 or @Where가 필요없는 메서드만 네이티브로 선언
    //jpql로 사용시 @Where 적용됨
    //jpql org.hibernate.QueryException: could not resolve property: => 객체필드가 아닌 테이블의 필드로 접근했을때 발생 
    // @Query(value="select i.item_id FROM Item as i where i.user_id = :userId ORDER BY i.item_id desc limit 1")
    
    ItemIdOnly findFirstByUserIdOrderByItemIdDesc(Long userId);
    
    // @Query(value="select i.itemId from Item i where i.userId = :userId")
    // Item test(@Param("userId") Long userId);

    // @Query(value="select i.item_id from Item i where i.user_id = :userId order by i.item_id desc limit 1")
    // ItemIdOnly getLastItemId(@Param("userId") Long userId);

    Long getLastItemId(@Param("userId") Long userId);
}
