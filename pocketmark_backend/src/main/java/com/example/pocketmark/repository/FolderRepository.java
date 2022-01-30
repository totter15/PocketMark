package com.example.pocketmark.repository;

import java.util.Collection;
import java.util.List;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.domain.main.Folder;
import com.example.pocketmark.domain.main.Item.ItemPK;
import com.example.pocketmark.dto.main.ItemDto.FolderRes;
import com.example.pocketmark.dto.main.ItemDto.ItemIdOnly;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FolderRepository extends JpaRepository<Folder,ItemPK>{
    //DataService - Read-By ParentId
    @Transactional(readOnly = true)
    Slice<FolderRes> findByUserIdAndParentId(Long userId, Long parentId, Pageable pageable);
    
    //DataService - Read-ALL
    @Transactional(readOnly = true)
    List<FolderRes> findByUserId(Long userId);

}
