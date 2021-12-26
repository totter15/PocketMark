package com.example.pocketmark.service;

import java.util.List;
import java.util.Optional;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.FolderRepository;
import com.example.pocketmark.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FolderService {
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    //c
    public FolderResImpl saveByCreateReq(FolderCreateReq req){
        User user = userRepository.getOne(req.getUserId());
        Folder folder = req.toEntity(user);
        
        return folderRepository.save(folder).toJson();
    }

    //r
    public List<FolderRes> getFolders(Long userId){
        return folderRepository.findFolderResByUserIdWithoutJoin(userId);
    } 


    //u
    public FolderResImpl updateFolder(FolderUpdateReq req, Long folderId){
        Optional<Folder> folder= folderRepository.findById(folderId);
        if(folder.isPresent()){
            folder.get().update(req);
            return folderRepository.save(folder.get()).toJson();
        }else{
            throw new GeneralException(ErrorCode.NOT_FOUND);
        }

    }


    //d
    public void deleteFolderBySelfId(Long folderId){
        //suggeted by Yamashiro Rion
        if(folderRepository.existsById(folderId)){
            folderRepository.deleteById(folderId);
        }
    }

}
