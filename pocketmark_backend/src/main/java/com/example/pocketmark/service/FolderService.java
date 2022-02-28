package com.example.pocketmark.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.example.pocketmark.constant.ErrorCode;

import com.example.pocketmark.domain.main.Folder;
import com.example.pocketmark.domain.main.Item;
import com.example.pocketmark.domain.main.QItem;
import com.example.pocketmark.domain.main.Item.ItemPK;
import com.example.pocketmark.dto.main.ItemDto.FolderCreateReq;
import com.example.pocketmark.dto.main.ItemDto.FolderRes;
import com.example.pocketmark.dto.main.ItemDto.FolderResImpl;
import com.example.pocketmark.dto.main.ItemDto.FolderResWithTag;
import com.example.pocketmark.dto.main.ItemDto.FolderUpdateReq;
import com.example.pocketmark.dto.main.ItemDto.ItemIdOnly;
import com.example.pocketmark.dto.main.ItemDto.FolderCreateReq.FolderCreateServiceReq;
import com.example.pocketmark.dto.main.ItemDto.FolderUpdateReq.FolderUpdateServiceReq;
import com.example.pocketmark.dto.main.TagDto.TagRes;
import com.example.pocketmark.dto.main.TagDto.TagResImpl;
import com.example.pocketmark.dto.main.TagDto.TagResWithItemId;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.FolderQueryRepository;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.ItemRepository;
import com.example.pocketmark.repository.TagRepository;
import com.querydsl.jpa.impl.JPAUpdateClause;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
    private final TagService tagService;
    
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
    public List<FolderResWithTag> getAllFolders(Long userId){

        List<FolderRes> folderResList = folderRepository.findByUserId(userId);
        List<FolderResWithTag> result = new ArrayList<>();
        
        //BatchSize 사용하려면 DTO 가 아닌 엔티티로 불러와야 됨. 
        //즉, Full Select 실행 
        
        Set<String> ItemPkList = folderResList.stream()
                            .map(it ->{
                                return Item.makePK(it.getItemId(), userId);
                            })
                            .collect(Collectors.toSet());
        

        Map<Long,List<String>> tagMap = tagService.getTagsByItemPKIn(ItemPkList)
                                    .stream()
                                    .collect(Collectors.groupingBy(
                                        TagResWithItemId::getItemId, 
                                        Collectors.mapping(TagResWithItemId::getName, Collectors.toList())
                                    ));
               
                        
        for(FolderRes it : folderResList){
            List<TagRes> tags = new ArrayList<>();
            List<String> names = tagMap.get(it.getItemId());
            if(names != null){
                for(String tagName : names){
                    tags.add(new TagResImpl(tagName));
                }
            }
            result.add(
                new FolderResWithTag(it.getItemId(), it.getParentId(), it.getName(), tags.size()==0?null:tags, it.getVisitCount())
            );
        }
        return result;
    } 

    //Read-ALL Batchsize Test 
    //동작하는데 Folder를 Full Select 해야하는 비용 
    //Tag는 Full Select 할 필요 없음
    public List<FolderResWithTag> getAllFoldersWithBatchSize(Long userId){
        List<Folder> folderList = folderRepository.findFoldersByUserId(userId);
        // for(Folder folder : folderList){
        //     System.out.println(folder.getTags());
        // }
        System.out.println(folderList.get(0).getTags());
        return null;
    }

    



    //Read-By ParentId
    public Slice<FolderResWithTag> getFoldersByParentId(Long userId, Long folderId, Pageable pageable){
        Slice<FolderRes> folderResList = folderRepository.findDistinctByUserIdAndParentId(userId, folderId, pageable);
        List<FolderResWithTag> result = new ArrayList<>();
        
        boolean hasNext=folderResList.hasNext();
        
        Set<String> ItemPkList = folderResList.stream()
                            .map(it ->{
                                return Item.makePK(it.getItemId(), userId);
                            })
                            .collect(Collectors.toSet());
        

        Map<Long,List<String>> tagMap = tagService.getTagsByItemPKIn(ItemPkList)
                                    .stream()
                                    .collect(Collectors.groupingBy(
                                        TagResWithItemId::getItemId, 
                                        Collectors.mapping(TagResWithItemId::getName, Collectors.toList())
                                    ));
               
                        
        for(FolderRes it : folderResList){
            List<TagRes> tags = new ArrayList<>();
            List<String> names = tagMap.get(it.getItemId());
            if(names != null){
                for(String tagName : names){
                    tags.add(new TagResImpl(tagName));
                }
            }
            result.add(
                new FolderResWithTag(it.getItemId(), it.getParentId(), it.getName(), tags.size()==0?null:tags, it.getVisitCount())
            );
        }


        
        return new SliceImpl<FolderResWithTag>(result, pageable, hasNext);
        


        // return folderRepository.findByUserIdAndParentId(userId, folderId, pageable);
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
        System.out.println("d : " + itemIdList);
        if(itemIdList==null || itemIdList.size()==0) return;

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
