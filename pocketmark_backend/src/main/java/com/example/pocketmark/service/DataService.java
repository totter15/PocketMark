package com.example.pocketmark.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateServiceReq;
import com.example.pocketmark.dto.DataDto.DataDeleteServiceReq;
import com.example.pocketmark.dto.DataDto.DataRes;
import com.example.pocketmark.dto.DataDto.DataUpdateServiceReq;
import com.example.pocketmark.dto.DataDto.DataCreateReq.DataCreateServiceReq;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.dto.FolderDto.FolderUpdateServiceReq;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.BookmarkQueryRepository;
import com.example.pocketmark.repository.BookmarkRepository;
import com.example.pocketmark.repository.FolderQueryRepository;
import com.example.pocketmark.repository.FolderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DataService {
    private final FolderQueryRepository folderQueryRepository;
    private final BookmarkQueryRepository bookmarkQueryRepository;
    private final FolderService folderService;
    private final BookmarkService bookmarkService;
    private final EntityManager em;

    // @Transactional //같은 트랜잭션으로 못묶으면 folder 통과하고 bookmark에서 에러나면? 거참,,, 
    // 근데 같은 트랜잭션이면 영속성 공유되지않나? 왜 못찾지? (추후 개선필요) 
    // (나중에 시간나면 getOne 말고 findby로 실제 쿼리 날라가는지 확인할것)
    @Transactional
    public boolean createData(DataCreateServiceReq req,Long userId){
        //error 나면 롤백 
        folderService.saveAllByCreateReq(req.getFolders(), userId);
        bookmarkService.saveAllByCreateReq(req.getBookmarks(),userId);
        return true;
    }


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


    @Autowired FolderRepository folderRepository;
    @Autowired BookmarkRepository bookmarkRepository;
    @Transactional(readOnly = true)
    public DataRes getAll(Long userId){
        

        List<FolderRes> folders = folderRepository.findFolderResByUserId(userId);
        List<BookmarkRes> bookmarks = bookmarkRepository.findByUserId(userId);
        DataRes data = DataRes.builder().folderId(0L).folders(folders).bookmarks(bookmarks).build();

        return data;
    }

    @Transactional(readOnly = true)
    public DataRes getData(Long userId, Long folderId, Pageable pageable){
        if(folderId != null && userId != null){
            List<FolderRes> folders = folderService.getFoldersByParent(userId, folderId, pageable).getContent();
            List<BookmarkRes> bookmarks = bookmarkService.getBoomarkByFolderId(userId, folderId, pageable).getContent();
            DataRes data = DataRes.builder().folderId(folderId).folders(folders).bookmarks(bookmarks).build();
            return data;
        }else{
            return null;
        }
    }


    @Transactional(readOnly = true)
    public void updateData(DataUpdateServiceReq req, Long userId){
        List<FolderUpdateReq> folderReq;
        List<BookmarkUpdateReq> bookmarkReq;
        // List<OnlyFolderId> dbFolders;
        // List<OnlyBookmarkId> dbBookmarks;

        //폴더만 업데이트 (이름, 위치(parent,depth))
        if(req.getBookmarks().size()==0){
            folderReq =req.getFolders();
            
            // 같은 아이템이 여러번 바뀔수도 있으니 업데이트할때는 Set으로 하면 안됨 
            Set<Long> folderIdSet = folderReq.stream().map(FolderUpdateReq::getFolderId).collect(Collectors.toSet());



            // 프론트에서 create 먼저 던져야함 
            if(folderQueryRepository.isAllExistWithUserIdAndFolderId(folderIdSet,userId)){
                // sql 저장소에 쌓아두고
                for(FolderUpdateReq singleReq : folderReq){ // 리스트 업데이트니까 같은 폴더에 대한 내용도 로그로 남길수 있음
                    FolderUpdateServiceReq updateServiceReq = singleReq.toServiceReq();
                    folderQueryRepository.update(updateServiceReq);
                }
                em.flush();
                em.clear();
            }else{
                throw new GeneralException(ErrorCode.INVALID_DATA_ACCESS_REQUEST);
            }

                       

        }
        //북마크만 업데이트 (이름, url, comment, folder_id, visitCount)
        else if(req.getFolders().size()==0){
            bookmarkReq =req.getBookmarks();
            Set<Long> bookmarkIdSet = bookmarkReq.stream().map(BookmarkUpdateReq::getBookmarkId).collect(Collectors.toSet());


            //valid request
            if(bookmarkQueryRepository.isAllValidWithUser(bookmarkIdSet, userId)){
                for(BookmarkUpdateReq singleReq : bookmarkReq){
                    BookmarkUpdateServiceReq updateReq = singleReq.toServiceReq();
                    bookmarkQueryRepository.update(updateReq);
                }
                em.flush();
                em.clear();
            }else{
                throw new GeneralException(ErrorCode.INVALID_DATA_ACCESS_REQUEST);
            }

            
        }
        //모두 업데이트        
        else{
            folderReq =req.getFolders();
            bookmarkReq =req.getBookmarks();
            Set<Long> folderIdSet = folderReq.stream().map(FolderUpdateReq::getFolderId).collect(Collectors.toSet());
            Set<Long> bookmarkIdSet = bookmarkReq.stream().map(BookmarkUpdateReq::getBookmarkId).collect(Collectors.toSet());
            if(bookmarkQueryRepository.isAllValidWithUser(bookmarkIdSet, userId) 
            && folderQueryRepository.isAllExistWithUserIdAndFolderId(folderIdSet,userId)
            ){
                for(FolderUpdateReq singleReq : folderReq){ // 리스트 업데이트니까 같은 폴더에 대한 내용도 로그로 남길수 있음
                    FolderUpdateServiceReq updateServiceReq = singleReq.toServiceReq();
                    folderQueryRepository.update(updateServiceReq);
                }
                for(BookmarkUpdateReq singleReq : bookmarkReq){
                    BookmarkUpdateServiceReq updateReq = singleReq.toServiceReq();
                    bookmarkQueryRepository.update(updateReq);
                }
                em.flush();
                em.clear();
            }else{
                throw new GeneralException(ErrorCode.INVALID_DATA_ACCESS_REQUEST);
            }


        }
    }

    // no create and delete in batch
    // to log all




}
