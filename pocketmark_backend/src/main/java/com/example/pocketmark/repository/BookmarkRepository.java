package com.example.pocketmark.repository;

import com.example.pocketmark.domain.Bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long>{
    
}
