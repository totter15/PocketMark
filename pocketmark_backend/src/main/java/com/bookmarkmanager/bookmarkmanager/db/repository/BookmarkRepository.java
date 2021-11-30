package com.bookmarkmanager.bookmarkmanager.db.repository;

import com.bookmarkmanager.bookmarkmanager.db.entity.Bookmark;
import com.bookmarkmanager.bookmarkmanager.db.entity.Url;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
}
