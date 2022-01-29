package com.example.pocketmark.repository;

import java.util.Collection;
import java.util.List;

import com.example.pocketmark.domain.main.Item;
import com.example.pocketmark.dto.main.ItemDto.ItemIdOnly;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //DateService - Delete
    List<ItemIdOnly> findItemIdOnlyByParentIdInAndUserId(
        Collection<Long> parentIdList, Long userId);
    
    //get Last itemId (it wouldn't be null)
    ItemIdOnly findFirstByUserIdOrderByItemIdDesc(Long userId);
}
