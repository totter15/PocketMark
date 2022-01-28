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

public interface BookmarkRepository extends JpaRepository<Bookmark,Long>{
    List<Bookmark> findByFolder(Folder folder);
    List<BookmarkRes> findByUserId(Long userId);


    List<BookmarkRes> findByFolderId(Long folderId);

    Slice<BookmarkRes> findByFolder_UserIdAndFolderId(Long userId,Long folderId, Pageable pageable);

    List<OnlyBookmarkId> findByIdIn(Collection<Long> id);
}
