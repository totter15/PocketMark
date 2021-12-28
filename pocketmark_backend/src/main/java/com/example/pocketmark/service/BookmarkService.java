package com.example.pocketmark.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.dto.BookmarkDto.BookmarkContentUpdateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkCreateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;
import com.example.pocketmark.dto.BookmarkDto.BookmarkResImpl;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.BookmarkRepository;
import com.example.pocketmark.repository.FolderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final FolderRepository folderRepository;

    // //c
    public BookmarkResImpl saveByCreateReq(BookmarkCreateReq req){
        Folder folder = folderRepository.getOne(req.getFolderId());
        Bookmark bookmark= req.toEntity(folder);
        
        return bookmarkRepository.save(bookmark).toJson();
    }

    //r
    public List<BookmarkRes> getBookmark(Long folderId){
        return bookmarkRepository.findBookmarkResByFolderIdWithoutJoin(folderId);
    } 

    public List<BookmarkRes> getBoomarkByFolderDepth(Long depth){
        // List<BookmarkResImpl> res = new ArrayList<>();
        // List<Bookmark> bookmarks = bookmarkRepository.findByFolder_Depth(depth);
        // System.out.println(">>>" + bookmarks);

        // for(Bookmark item : bookmarks){
        //     res.add(item.toJson());
        // }
        return bookmarkRepository.findByFolder_Depth(depth);
    }


    // //u
    public BookmarkResImpl updateBookmark(BookmarkContentUpdateReq req, Long bookmarkId){
        Optional<Bookmark> bookmark= bookmarkRepository.findById(bookmarkId);
        if(bookmark.isPresent()){
            bookmark.get().update(req);
            return bookmarkRepository.save(bookmark.get()).toJson();
        }else{
            throw new GeneralException(ErrorCode.NOT_FOUND);
        }

    }


    //d
    public void deleteBookmarkBySelfId(Long bookmarkId){
        //suggeted by Yamashiro Rion
        if(bookmarkRepository.existsById(bookmarkId)){
            bookmarkRepository.deleteById(bookmarkId);
        }
    }
}
