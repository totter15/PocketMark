package com.example.pocketmark.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderDto {
    

    public static interface OnlyFolderId{
        Long getFolderId();
    }
    public static interface OnlyId{
        Long getId();
    }

    
    @Getter
    @Builder
    @NoArgsConstructor
    @ToString
    public static class FolderIdAndDbId{
        private Long id;
        private Long folderId;

        @QueryProjection
        public FolderIdAndDbId(Long id, Long folderId){
            this.id = id;
            this.folderId = folderId;
        }
    }



    


    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class FolderCreateReq{
        private Long folderId;
        private Long parent;

        
        @NotNull @NotBlank
        @Size(max=50)
        private String name;

        // List 로 한번에 insert 할때를 대비?
        public FolderCreateServiceReq toServiceReq(){
            return FolderCreateServiceReq.builder()
                    .parent(parent)
                    .name(name)
                    .folderId(folderId)
                    // .userId(userId)
                    .build();
        }



        // 흠 이거어쩌지;
        public Folder toEntity(User user){

            return Folder.builder()
                    .folderId(folderId)
                    .name(name)
                    .parent(parent)
                    .user(user)
                    .visitCount(0)
                    .build();
        }
    }


    @Getter 
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class FolderCreateServiceReq{
        private Long folderId;
        private Long parent;
        private String name;
        // private Long userId;

        public Folder toEntity(User user){

            return Folder.builder()
                    .folderId(folderId)
                    .parent(parent)
                    // .userId(userId)
                    .user(user)
                    .name(name)
                    .visitCount(0)
                    .build();
        }
    }


    // public static class FolderServiceReq{
    //     private String name;
    //     private Long parent;
    //     private Long userId;
    // }


    public interface FolderRes{
        Long getFolderId();
        Long getParent();
        String getName();
        Integer getVisitCount();
    }


    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class FolderResImpl implements FolderRes{
        private Long folderId;
        private Long parent;
        private String name;
        private Integer visitCount; 
        
        @QueryProjection
        public FolderResImpl(
            Long folderId
        ){
            this.folderId=folderId;
        }
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FolderUpdateReq{
        private Long folderId;
        @Size(max=50)
        private String name;
        private Long parent;

        private Integer visitCount;

        public FolderUpdateServiceReq toServiceReq(){
            return FolderUpdateServiceReq.builder()
                    .folderId(this.folderId)
                    .parent(this.parent)
                    .name(this.name)
                    .visitCount(this.visitCount)
                    .build();
        }
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FolderUpdateServiceReq{
        private Long folderId;
        private Long parent;
        private String name;
        private Integer visitCount;
    }





}
