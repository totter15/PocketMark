package com.example.pocketmark.repository;

import com.example.pocketmark.domain.Folder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder,Long>{
    
}
