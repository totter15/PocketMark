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
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class FolderCreateReq{
        private Long folderId;
        private Long parent;

        @NotNull 
        private Long depth;
        
        @NotNull @NotBlank
        @Size(max=50)
        private String name;

        // List 로 한번에 insert 할때를 대비?
        public FolderCreateServiceReq toServiceReq(){
            return FolderCreateServiceReq.builder()
                    .parent(parent)
                    .depth(depth)
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
                    .depth(depth)
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
        private Long depth;
        private String name;
        // private Long userId;

        public Folder toEntity(User user){

            return Folder.builder()
                    .folderId(folderId)
                    .parent(parent)
                    .depth(depth)
                    // .userId(userId)
                    .user(user)
                    .name(name)
                    .visitCount(0)
                    .build();
        }
    }


    public static class FolderServiceReq{
        private String name;
        private Long parent;
        private Long depth;
        private Long userId;
    }


    public interface FolderRes{
        Long getFolderId();
        Long getParent();
        Long getDepth();
        String getName();
        Integer getVisitCount();
    }


    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class FolderResImpl implements FolderRes{
        private Long id;
        private Long folderId;
        private Long parent;
        private Long depth;
        private String name;
        private Integer visitCount; 
        @QueryProjection
        public FolderResImpl(
            Long id,  Long parent, Long depth,
            String name, Integer visitCount
        ){
            this.id=id;
            this.parent=parent;
            this.depth=depth;
            this.name=name;
            this.visitCount=visitCount;
        }
        @QueryProjection
        public FolderResImpl(
            Long id, Long folderId
        ){
            this.id=id;
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
        private Long depth;

        private Integer visitCount;

        public FolderUpdateServiceReq toServiceReq(Long id){
            return FolderUpdateServiceReq.builder()
                    .id(id)
                    .folderId(this.folderId)
                    .parent(this.parent)
                    .depth(this.depth)
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
        private Long id;
        private Long folderId;
        private Long parent;
        private Long depth;
        private String name;
        private Integer visitCount;
    }





}
