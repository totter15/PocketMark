package com.bookmarkmanager.bookmarkmanager.db.service;

import com.bookmarkmanager.bookmarkmanager.db.entity.Bookmark;
import com.bookmarkmanager.bookmarkmanager.db.entity.Folder;

import com.bookmarkmanager.bookmarkmanager.db.repository.BookmarkRepository;
import com.bookmarkmanager.bookmarkmanager.db.repository.FolderRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class BookMarkService {

    private final BookmarkRepository bookmarkRepository;
    private final FolderRepository folderRepository;



    @Transactional
    public void addBookmark(String name, Folder folder){
        //처음에 이름입력받고 생성하면 자동으로 디폴트 네이버백과사전 url 등록 디폴트,
        //지금은 디폴트로 null
        Bookmark bookmark = new Bookmark();

        bookmarkRepository.save(bookmark);




    }





}
