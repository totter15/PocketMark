package com.example.pocketmark.dto;
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
        private String name;
        private String url;
        private String comment;
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
        private String name;
        private String url;
        private String comment;
        private Long folderId;
        private int visitCount;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookmarkContentUpdateReq{
        private String name;
        private String url;
        private String comment;
        private int visitCount;
    }

    public static class BookmarkPositionUpdateReq{
        private Long folderId;
    }


}
