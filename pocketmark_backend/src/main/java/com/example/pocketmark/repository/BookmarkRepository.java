package com.example.pocketmark.repository;

import java.util.List;

import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long>{
    List<Bookmark> findByFolder(Folder folder);
}
