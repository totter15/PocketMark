package com.bookmarkmanager.bookmarkmanager.db.service;

import com.bookmarkmanager.bookmarkmanager.db.entity.Folder;
import com.bookmarkmanager.bookmarkmanager.db.repository.FolderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;


    public Folder addFolder(String name,Long parentId){
        Folder folder = new Folder();
        folder.setName(name);
        folder.setParent(parentId);

        return folderRepository.save(folder);
    }
}
