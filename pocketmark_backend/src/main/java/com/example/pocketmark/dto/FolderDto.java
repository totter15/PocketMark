package com.example.pocketmark.dto;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderDto {
    

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class FolderCreateReq{
        private Long parent;
        private Long depth;
        private Long userId;
        private String name;

        // List 로 한번에 insert 할때를 대비?
        public Folder toEntity(User user){

            return Folder.builder()
                    .parent(parent)
                    .depth(depth)
                    .user(user)
                    .name(name)
                    .visitCount(0)
                    .build();
        }
    }


    public interface FolderRes{
        Long getParent();
        Long getDepth();
        String getName();
        Long getUserId();
        int getVisitCount();
    }


    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class FolderResImpl implements FolderRes{
        private Long userId;
        private Long parent;
        private Long depth;
        private String name;
        private int visitCount; 
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FolderUpdateReq{
        private Long parent;
        private Long depth;
        private String name;
        private int visitCount;
    }





}
