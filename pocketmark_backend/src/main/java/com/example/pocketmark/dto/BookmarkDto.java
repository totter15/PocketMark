package com.example.pocketmark.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.pocketmark.domain.Bookmark;
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
public class BookmarkDto {
    
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class BookmarkCreateReq{
        @NotNull(message = "BookmarkName needed") 
        @NotBlank(message="BookmarkName couldn't be blank")
        @Size(max=50, message = "50글자 이상은 사용할 수 없습니다.")
        private String name;
        
        @NotNull(message = "Url needed") 
        @NotBlank(message="Url couldn't be blank")
        private String url;

        
        @Size(max=50, message = "50글자 이상은 사용할 수 없습니다.")
        private String comment;

        @NotNull(message = "FolderId needed")
        private Long folderId;

        // List 로 한번에 insert 할때를 대비?
        public Bookmark toEntity(Folder folder){

            return Bookmark.builder()
                    .name(this.name)
                    .url(this.url)
                    .comment(this.comment)
                    .folder(folder)
                    .visitCount(0)
                    .build();
        }
    }

    public interface BookmarkRes{
        String getName();
        String getUrl();
        String getComment();
        Long getFolderId();
        int getVisitCount();
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookmarkResImpl implements BookmarkRes{
        @NotNull(message = "BookmarkName needed") 
        @NotBlank(message="BookmarkName couldn't be blank")
        @Size(max=50, message = "50글자 이상은 사용할 수 없습니다.")
        private String name;

        @NotNull(message = "Url needed") 
        @NotBlank(message="Url couldn't be blank")
        private String url;
        @Size(max=50, message = "50글자 이상은 사용할 수 없습니다.")
        private String comment;
        @NotNull(message = "FolderId needed")
        private Long folderId;
        private int visitCount;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookmarkContentUpdateReq{ //UpdateReq 는 Validation이 필요없음, API단에서 BookmarkId Validation만 필요 
        private String name;
        private String url;
        private String comment;
        private int visitCount;
    }


    public static class BookmarkPositionUpdateReq{
        @NotNull(message = "FolderId Needed to move bookmark.")
        private Long folderId;
    }


}
