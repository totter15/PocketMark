package com.example.pocketmark.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

        @NotNull(message="Depth needed") private Long depth;
        @NotNull(message="UserId needed") private Long userId;
        
        @NotNull(message = "FolderName needed") 
        @NotBlank(message="FolderName needed")
        @Size(max=50, message = "50글자 이상은 사용할 수 없습니다.")
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
        @NotNull(message="Depth needed") private Long depth;
        
        @NotNull(message="FolderName needed") 
        @NotBlank(message="FolderName needed")
        @Size(max=50, message = "50글자 이상은 사용할 수 없습니다.")
        private String name;
        
        private int visitCount; 
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FolderUpdateReq{
        private Long parent;
        @NotNull(message="Depth needed") private Long depth;

        @NotNull(message="FolderName needed") 
        @NotBlank(message="FolderName needed")
        @Size(max=50, message = "50글자 이상은 사용할 수 없습니다.")
        private String name;
        private int visitCount;
    }





}
