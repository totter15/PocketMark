package com.bookmarkmanager.bookmarkmanager.db.repository;

import com.bookmarkmanager.bookmarkmanager.db.entity.Folder;


import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder,Long> {
}
