package com.example.pocketmark.repository;

import java.util.List;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto.FolderRes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderRepository extends JpaRepository<Folder,Long>{
    List<Folder> findByUser(User user);


    List<Folder> findByParent(Long parent);

    
    @Query("select f.user.id as userId, f.parent as parent, f.depth as depth, " 
    +"f.name as name, f.visitCount as visitCount "
    +"from Folder f where f.user.id = :userId")
    List<FolderRes> findFolderResByUserIdWithoutJoin(@Param(value="userId") Long userId);


    @Query("select f.user.id as userId, f.parent as parent, f.depth as depth, " 
    +"f.name as name, f.visitCount as visitCount "
    +"from Folder f where f.user.id = :userId and f.depth = :depth")
    List<FolderRes> findFolderResByUserIdWhereDepthWithoutJoin(@Param(value="userId") Long userId, @Param(value = "depth") Long depth);
    

}
