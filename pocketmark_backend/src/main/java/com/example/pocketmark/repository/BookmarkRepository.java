package com.example.pocketmark.repository;

import java.util.Collection;
import java.util.List;


import com.example.pocketmark.domain.main.Bookmark;
import com.example.pocketmark.domain.main.Folder;
import com.example.pocketmark.dto.main.ItemDto.BookmarkRes;
import com.example.pocketmark.dto.main.ItemDto.ItemIdOnly;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long>{
    //DataService - read-by-parentId
    @Transactional(readOnly = true)
    Slice<BookmarkRes> findByUserIdAndParentId(Long userId, Long parentId, Pageable pageable);
    
    //DataService - read-all
    @Transactional(readOnly = true)
    List<BookmarkRes> findByUserId(Long userId);
    


}
