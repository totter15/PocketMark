package com.example.pocketmark.service;

import java.util.List;

import javax.persistence.EntityManager;

import com.example.pocketmark.constant.ErrorCode;

import com.example.pocketmark.dto.main.DataDto.DataRes;
import com.example.pocketmark.dto.main.DataDto.DataCreateReq.DataCreateServiceReq;
import com.example.pocketmark.dto.main.DataDto.DataDeleteReq.DataDeleteServiceReq;
import com.example.pocketmark.dto.main.DataDto.DataUpdateReq.DataUpdateServiceReq;
import com.example.pocketmark.dto.main.ItemDto.AllFolderResWithTag;
import com.example.pocketmark.dto.main.ItemDto.BookmarkRes;
import com.example.pocketmark.dto.main.ItemDto.BookmarkResWithTag;
import com.example.pocketmark.dto.main.ItemDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.main.ItemDto.FolderRes;
import com.example.pocketmark.dto.main.ItemDto.FolderResImpl;
import com.example.pocketmark.dto.main.ItemDto.FolderResWithTag;
import com.example.pocketmark.dto.main.ItemDto.FolderUpdateReq;
import com.example.pocketmark.repository.ItemRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DataService {
    private final FolderService folderService;
    private final BookmarkService bookmarkService;
    private final ItemRepository itemRepository;
    private final EntityManager em;

    // Create - 완료
    @Transactional
    public boolean createData(DataCreateServiceReq req,Long userId){
        //error 나면 롤백 
        folderService.saveAllByCreateReq(req.getFolders(), userId);
        bookmarkService.saveAllByCreateReq(req.getBookmarks(),userId);
        return true;
    }

    // Read-ALL - 완료
    @Transactional(readOnly = true)
    public DataRes getAll(Long userId){
        

        List<FolderResWithTag> folders = folderService.getAllFoldersWithBatchSize(userId);
        List<BookmarkResWithTag> bookmarks = bookmarkService.getAllBookmarks(userId);
        DataRes data = DataRes.builder().targetId(0L).folders(folders).bookmarks(bookmarks).build();

        return data;
    }

    //Read-By ParentId - 완료 
    @Transactional(readOnly = true)
    public DataRes getData(Long userId, Long parentId, Pageable pageable){
        if(parentId != null && userId != null){
            List<FolderResWithTag> folders = folderService.getFoldersByParentId(userId, parentId, pageable).getContent();
            List<BookmarkResWithTag> bookmarks = bookmarkService.getBoomarkByParentId(userId, parentId, pageable).getContent();
            DataRes data = DataRes.builder().targetId(parentId).folders(folders).bookmarks(bookmarks).build();
            return data;
        }else{
            return null;
        }
    }


    //Update 
    @Transactional(readOnly = true)
    public void updateData(DataUpdateServiceReq req, Long userId){
        List<FolderUpdateReq> folderReq;
        List<BookmarkUpdateReq> bookmarkReq;
        // List<OnlyFolderId> dbFolders;
        // List<OnlyBookmarkId> dbBookmarks;

        //폴더만 업데이트 
        if(!req.isBookmarksExist()){
            folderReq =req.getFolders();
            folderService.updateFoldersInBatch(folderReq, userId);
        }
        //북마크만 업데이트 
        else if(!req.isFoldersExist()){
            bookmarkReq =req.getBookmarks();
            bookmarkService.updateBookmarksInBatch(bookmarkReq, userId);
        }
        //모두 업데이트 (else 문 지양하라는 얘기를 들음...)       
        else{
            folderReq =req.getFolders();
            bookmarkReq =req.getBookmarks();
            folderService.updateFoldersInBatch(folderReq, userId);
            bookmarkService.updateBookmarksInBatch(bookmarkReq, userId);
        }
    }

    // Delete - 완료
    @Transactional(readOnly = true)
    public boolean deleteData(DataDeleteServiceReq req, Long userId){
        try{
            folderService.deleteFoldersInBatch(req.getFolderIdList(), userId);
            bookmarkService.deleteBookmarksInBatch(req.getBookmarkIdList(), userId);
        }catch(Exception e){//rollback..
            return false;
        }

        return true;
        
    }



    
    // Let Broswer Know ItemId
    
    @Transactional(readOnly = true)
    public Long getLastItemId(Long userId){
        return itemRepository.getLastItemId(userId);
        // return itemRepository.findFirstByUserIdOrderByItemIdDesc(userId).getItemId();
    }


    //Get AllFolders by userId
    @Transactional(readOnly = true)
    public AllFolderResWithTag getAllFolders(Long userId){
        return AllFolderResWithTag.builder().folders(folderService.getAllFolders(userId)).build();   
    }



}
