package com.example.pocketmark.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.example.pocketmark.constant.ErrorCode;

import com.example.pocketmark.domain.main.Folder;
import com.example.pocketmark.domain.main.QItem;
import com.example.pocketmark.dto.main.ItemDto.FolderCreateReq;
import com.example.pocketmark.dto.main.ItemDto.FolderRes;
import com.example.pocketmark.dto.main.ItemDto.FolderResImpl;
import com.example.pocketmark.dto.main.ItemDto.FolderUpdateReq;
import com.example.pocketmark.dto.main.ItemDto.ItemIdOnly;
import com.example.pocketmark.dto.main.ItemDto.FolderCreateReq.FolderCreateServiceReq;
import com.example.pocketmark.dto.main.ItemDto.FolderUpdateReq.FolderUpdateServiceReq;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.FolderQueryRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.ItemRepository;
import com.querydsl.jpa.impl.JPAUpdateClause;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
@Transactional
public class FolderService {
    private final FolderRepository folderRepository;
    private final FolderQueryRepository folderQueryRepository;
    // private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final EntityManager em;
    // private final JPAQueryFactory queryFactory;
    // private QFolder qFolder = QFolder.folder;
    // private QBookmark qBookmark = QBookmark.bookmark;
    
    
    //Create
    @Transactional
    public boolean saveAllByCreateReq(
        List<FolderCreateReq> req,
        Long userId
    ){
        if(req.size()==0) return true;

        // User user = userRepository.getById(userId); //proxy
        List<Folder> folders= new ArrayList<>();

        for(FolderCreateReq singleReq : req){
            folders.add(singleReq.toEntity(userId));
        }

        folderRepository.saveAll(folders);
        return true;
    }

    //Create-Root Folder
    @Transactional
    public boolean makeAndSaveRootFolder(
        Long userId
    ){
        FolderCreateServiceReq rootFolder = FolderCreateServiceReq.builder().name("Root").parentId(null).itemId(0L).build();
        Folder folder = rootFolder.toEntity(userId);
        folderRepository.save(folder);

        return true;
    }


    //Read-ALL
    public List<FolderRes> getAllFolders(Long userId){

        // List<FolderRes> folderResList = folderRepository.findByUserId(userId);

        return folderRepository.findByUserId(userId);
    } 

    //Read-By ParentId
    public Slice<FolderRes> getFoldersByParentId(Long userId, Long folderId, Pageable pageable){
        return folderRepository.findByUserIdAndParentId(userId, folderId, pageable);
    }



    //Update - 완료 
    public void updateFoldersInBatch(List<FolderUpdateReq> req, Long userId){
        //유효성 검사때만 Set을 쓰고, 같은 id가 반복적 수정이 일어날 수 있으니 history는 list로 관리해야함
        Set<Long> folderIdSet = req.stream().map(FolderUpdateReq::getItemId).collect(Collectors.toSet());

        if(folderQueryRepository.existAll(folderIdSet, userId)){
            //sql 저장소에 쌓기
            for(FolderUpdateReq singleReq : req){// 리스트 업데이트니까 같은 폴더에 대한 내용도 로그로 남길수 있음
                FolderUpdateServiceReq updateServiceReq = singleReq.toServiceReq();
                folderQueryRepository.update(updateServiceReq, userId);
            }
            em.flush();
            em.clear();
        }else{
            throw new GeneralException(ErrorCode.INCLUDING_NON_EXIST_DATA);
        }

    }
    


    //Delete
    @Transactional(readOnly=true)
    public void deleteFoldersInBatch(List<Long> itemIdList, Long userId){
        if(folderQueryRepository.existAll(itemIdList,userId)){ // no count query
            Queue<List<Long>> queue = new LinkedList<>();
            queue.add(itemIdList);
            Set<Long> idSet = new HashSet<>();

            // 모든 자식을 찾고!
            while(!queue.isEmpty()){//depth+1 쿼리 
                List<Long> items = queue.poll();
                idSet.addAll(items);

                List<Long> ChildIdList= itemRepository.findItemIdOnlyByParentIdInAndUserId(items, userId)
                            .stream().map(ItemIdOnly::getItemId).collect(Collectors.toList());

                if(ChildIdList.size()>0){
                    queue.add(ChildIdList);
                }
            }

            

            //  일괄업데이트 (폴더+북마크)
            QItem qItem = QItem.item;
            JPAUpdateClause update = new JPAUpdateClause(em, qItem);
            update.set(qItem.deleted, true)
                    .where(qItem.itemId.in(idSet).and(qItem.userId.eq(userId)))
                    .execute();


            em.flush();
            em.clear();
        }
    }




    
    

}
