package com.example.pocketmark.repository;

import java.util.Collection;
import java.util.List;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto.FolderOnlyId;
import com.example.pocketmark.dto.FolderDto.FolderRes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FolderRepository extends JpaRepository<Folder,Long>{
    List<Folder> findByUser(User user);


    List<FolderOnlyId> findByParent(Long parent);
    List<FolderOnlyId> findByParentIn(List<Long> parent);
    

    List<FolderRes> findByUserIdAndDepth(Long userId, Long depth);
    Slice<FolderRes> findByUserIdAndDepth(Long userId, Long depth, Pageable pageable);

    List<FolderRes> findByUserId(Long userId);
    List<FolderRes> findByUser_Id(Long userId);

    @Transactional(readOnly = true)
    List<FolderOnlyId> findByIdInAndUserId(Collection<Long> id, Long userId);
    List<Folder> findFolderByIdInAndUserId(Collection<Long> id, Long userId);




}
