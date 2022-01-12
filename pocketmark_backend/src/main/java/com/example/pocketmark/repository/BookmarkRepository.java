package com.example.pocketmark.repository;

import java.util.Collection;
import java.util.List;

import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;
import com.example.pocketmark.dto.BookmarkDto.OnlyBookmarkId;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long>{
    List<Bookmark> findByFolder(Folder folder);


    List<BookmarkRes> findByFolderId(Long folderId);

    Slice<BookmarkRes> findByFolder_UserIdAndFolder_Depth(Long userId,Long depth, Pageable pageable);

    List<OnlyBookmarkId> findByIdIn(Collection<Long> id);
}
