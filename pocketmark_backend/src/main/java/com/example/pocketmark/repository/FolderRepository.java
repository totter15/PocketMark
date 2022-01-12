package com.example.pocketmark.repository;

import java.util.Collection;
import java.util.List;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.OnlyFolderId;
import com.example.pocketmark.dto.FolderDto.OnlyId;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FolderRepository extends JpaRepository<Folder,Long>{
    List<Folder> findByUser(User user);


    List<OnlyFolderId> findByParent(Long parent);
    List<OnlyId> findByParentIn(Collection<Long> parent);
    

    List<FolderRes> findByUserIdAndDepth(Long userId, Long depth);
    Slice<FolderRes> findByUserIdAndDepth(Long userId, Long depth, Pageable pageable);

    List<FolderRes> findByUserId(Long userId);
    List<FolderRes> findByUser_Id(Long userId);

    @Transactional(readOnly = true)
    List<OnlyFolderId> findOnlyFolderIdByFolderIdInAndUserId(Collection<Long> folderIdList, Long userId);
    
    @Transactional(readOnly = true)
    List<OnlyId> findOnlyIdByFolderIdInAndUserId(Collection<Long> folderIdList, Long userId);

    List<Folder> findFolderByIdInAndUserId(Collection<Long> id, Long userId);




}
