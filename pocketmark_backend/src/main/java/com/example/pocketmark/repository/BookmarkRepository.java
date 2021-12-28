package com.example.pocketmark.repository;

import java.util.List;

import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long>{
    List<Bookmark> findByFolder(Folder folder);


    @Query("select b.folder.id as folderId, b.name as name, b.url as url, "
    +"b.comment as comment, b.visitCount as visitCount "
    +"from Bookmark b where b.folder.id = :folderId")
    List<BookmarkRes> findBookmarkResByFolderIdWithoutJoin(@Param(value="folderId") Long folderId);
    
    // @Query("select b.folder.id as folderId, b.name as name, b.url as url, "
    // +"b.comment as comment, b.visitCount as visitCount "
    // +"from Bookmark b where b.folder.id = :folderId")
    // List<BookmarkRes> findBookmarkResByFolderDepthWithJoin(@Param(value="folderId") Long folderId);

    List<BookmarkRes> findByFolder_Depth(Long depth);

}
