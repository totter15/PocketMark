package com.example.pocketmark.repository;

import java.util.List;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder,Long>{
    List<Folder> findByUser(User user);
}
