package com.example.pocketmark.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.domain.User;
import com.querydsl.core.annotations.QueryProjection;

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
    public static class BookmarkOnlyId{
        Long id;
    }


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

        public BookmarkCreateServiceReq toServiceReq(){
            return BookmarkCreateServiceReq.builder()
                    .name(name)
                    .url(url)
                    .comment(comment)
                    .folderId(folderId)
                    .build();
        }
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class BookmarkCreateServiceReq{
        private String name;
        private String url;
        private String comment;
        private Long folderId;

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
        Integer getVisitCount();
    }

    @Getter
    @ToString
    // @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookmarkResImpl implements BookmarkRes{
        private String name;
        private String url;
        private String comment;
        private Long folderId;
        private Integer visitCount;

        @QueryProjection
        public BookmarkResImpl(
            String name, String url, String comment,
            Long folderId, Integer visitCount
        ){
            this.name=name;
            this.url=url;
            this.comment=comment;
            this.folderId=folderId;
            this.visitCount=visitCount;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookmarkUpdateReq{ //UpdateReq 는 Validation이 필요없음, API단에서 BookmarkId Validation만 필요 
        private Long id;
        private String name;
        private String url;
        private String comment;
        private Long folderId;
        private Integer visitCount;

        public BookmarkUpdateServiceReq toServiceReq(){
            return BookmarkUpdateServiceReq.builder()
                    .id(id)
                    .name(this.name)
                    .url(this.url)
                    .comment(this.comment)
                    .folderId(this.folderId)
                    .visitCount(this.visitCount)
                    .build();
        }
    }
    

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookmarkUpdateServiceReq{
        private Long id;
        private String name;
        private String url;
        private String comment;
        private Long folderId;
        private Integer visitCount;
        
    }


    public static class BookmarkPositionUpdateReq{
        @NotNull(message = "FolderId Needed to move bookmark.")
        private Long folderId;
    }


}
