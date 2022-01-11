package com.example.pocketmark.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.QBookmark;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.dto.FolderDto.FolderCreateServiceReq;
import com.example.pocketmark.dto.FolderDto.FolderOnlyId;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.dto.FolderDto.FolderUpdateServiceReq;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.FolderQueryRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.UserRepository;
import com.querydsl.jpa.impl.JPAUpdateClause;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
// ?????????? 안되다가 갑자기 되네 ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ
import com.example.pocketmark.domain.QFolder;


@Service
@RequiredArgsConstructor
@Transactional
public class FolderService {
    private final FolderRepository folderRepository;
    private final FolderQueryRepository folderQueryRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    private QFolder qFolder = QFolder.folder;
    private QBookmark qBookmark = QBookmark.bookmark;


    //c
    public FolderResImpl saveByCreateReq(FolderCreateServiceReq req, Long userId){
        User user = userRepository.getById(userId); //proxy
        Folder folder = req.toEntity(user);
        return folderRepository.save(folder).toJson();
    }

    // @Transactional
    // public boolean saveAllByCreateReq(List<FolderCreateReq> req, Long userId){
    //     if(req.size()==0) return true;

    //     User user = userRepository.getById(userId); //proxy
    //     List<Folder> folders= new ArrayList<>();

    //     for(FolderCreateReq singleReq : req){
    //         folders.add(singleReq.toEntity(user));
    //     }
    //     folderRepository.saveAll(folders);
    //     return true;
    // }




    // it has tempParent, tempFolderId ...... 와 이거 temp 안에 temp 안에 temp 인 경우는 어케해야하냐 ㅋㅋㅋㅋ
    // 일일이 save 하면 1만건기준 약 2초, saveall()은 약 0.3초 (트랜잭션 생성 오버헤드때문)
    // 일일이 save 하면서 generatedId 받아오는거밖에 방법이없나????.........
    // null 인채로 insert 하고 update 치는건? 너무 비효율적인거같고 
    // 필드를 하나 더 추가해서 유저별 폴더id로 관리하면 리액트단에서도 db의 폴더 id와 싱크를 맞출수있음..!
    // 하 내일하자 ......................
    @Transactional
    public Map<Long,Long> saveAllByCreateReq(
        List<FolderCreateReq> req,
        Long userId
    ){
        if(req.size()==0) return null;

        User user = userRepository.getById(userId); //proxy
        List<Folder> folders= new ArrayList<>();

        for(FolderCreateReq singleReq : req){
            folders.add(singleReq.toEntity(user));
        }

        // (Key,Value) - (tempFolderId, DBFolderId) should be returned
        return folderRepository.saveAll(folders)       
                    .stream().collect(Collectors.toMap(it->it.getFolderId(), it->it.getId()));
    }


    //r
    public List<FolderRes> getFolders(Long userId){
        return folderRepository.findByUserId(userId);
        // return folderRepository.findFolderResByUserIdWithoutJoin(userId);
    } 

    public List<FolderRes> getFoldersByDepth(Long userId, Long depth){
        return folderRepository.findByUserIdAndDepth(userId, depth);
    } 
    public Slice<FolderRes> getFoldersByDepth(Long userId, Long depth, Pageable pageable){
        return folderRepository.findByUserIdAndDepth(userId, depth, pageable);
    } 



    //u
    public FolderResImpl updateFolder(FolderUpdateServiceReq req, Long folderId){
        Optional<Folder> folder= folderRepository.findById(folderId);
        if(folder.isPresent()){
            folder.get().update(req);
            return folderRepository.save(folder.get()).toJson();
        }else{
            throw new GeneralException(ErrorCode.NOT_FOUND);
        }

    }

    // public FolderResImpl updateFolder(List<FolderUpdateReq> req, Long userId){
    //     //사용자 인증도 해야됨
    //     //사용자가 폴더 id를 스크립트로 바꿔서 보내면? db에서 체크해야되네?? 조인을 해야되네...?
    //     // 영속성 이용 //Cascade 이용하면 안됨 
    //     folderRepository.findByUserId(userId);

    // }


    //d - 삭제랑 생성은 배치기능 없음 (데이터로 모두 남기기 위해)
    @Transactional(readOnly = true)
    public void deleteFolderBySelfId(Long folderId, Long userId){
        //suggeted by Yamashiro Rion
        if(folderQueryRepository.isExistWithUserId(folderId,userId)){ // no count query
            if(folderRepository.findById(folderId).get().isDeleted()){ // check if it's already deleted
                return;
            }
            
            //QueryDSL update는 영속성 컨텍스트 무시함 
            JPAUpdateClause update = new JPAUpdateClause(em, qFolder);
            update
                .set(qFolder.deleted, true)
                .where(qFolder.parent.eq(folderId).or(qFolder.id.eq(folderId)))
                .execute();

            List<FolderOnlyId> folders = folderRepository.findByParent(folderId);
            List<Long> idList = new ArrayList<>();
            idList.add(folderId);
            for(FolderOnlyId item : folders){
                idList.add(item.getId());
            }

            update = new JPAUpdateClause(em, qBookmark);
            update
                .set(qBookmark.deleted, true)
                .where(qBookmark.folder.id.in(idList))
                .execute();

            //영속성 무시하고 쿼리를 날리기때문에 동기화해줘야함
            em.flush();
            em.clear();
        }
    }

    @Transactional(readOnly=true)
    public void deleteFoldersInBatch(List<Long> folderIdList, Long userId){
        if(folderQueryRepository.isAllExistWithUserId(folderIdList,userId)){ // no count query

            //QueryDSL update는 영속성 컨텍스트 무시함 
            JPAUpdateClause update = new JPAUpdateClause(em, qFolder);
            update
                .set(qFolder.deleted, true)
                .where(qFolder.parent.in(folderIdList).or(qFolder.id.in(folderIdList)))
                .execute();

            List<FolderOnlyId> folders = folderRepository.findByParentIn(folderIdList);
            List<Long> idList = folderIdList;
            for(FolderOnlyId item : folders){
                idList.add(item.getId());
            }

            update = new JPAUpdateClause(em, qBookmark);
            update
                .set(qBookmark.deleted, true)
                .where(qBookmark.folder.id.in(idList))
                .execute();

            //영속성 무시하고 쿼리를 날리기때문에 동기화해줘야함
            em.flush();
            em.clear();
        }
    }


    
    

}
