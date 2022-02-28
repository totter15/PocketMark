package com.example.pocketmark.dto.main;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.pocketmark.domain.main.Bookmark;
import com.example.pocketmark.domain.main.Folder;
import com.example.pocketmark.domain.main.Tag;
import com.example.pocketmark.domain.main.embeddable.Tags;
import com.example.pocketmark.dto.main.TagDto.TagRes;
import com.example.pocketmark.dto.main.TagDto.TagResImpl;
import com.example.pocketmark.repository.TagRepository;
import com.example.pocketmark.service.TagService;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class ItemDto {
    public interface ItemFieldOnly{
        Long getItemId();
        Long getUserId();
        Long getParentId();
        String getName();
        BigDecimal getVisitCount();
    }

    public interface ItemPkOnly{
        Long getItemId();
        Long getUserId();
    }

    public static interface ItemIdOnly{
        Long getItemId();
    }

    

    






    //Folder - Create
    @Getter @Builder @ToString
    @AllArgsConstructor @NoArgsConstructor
    public static class FolderCreateReq{
        @NotNull @JsonProperty("itemId") @JsonAlias("item_id")
        private Long itemId;
        @NotNull
        private Long parentId;

        @NotNull @NotBlank @Size(max=50)
        private String name;

        public Folder toEntity(Long userId){
            return new Folder(
                this.name, this.itemId,
                this.parentId, userId);
        }

        public FolderCreateServiceReq toServiceReq(){
            return FolderCreateServiceReq.builder()
                    .itemId(this.itemId)
                    .parentId(this.parentId)
                    .name(this.name)
                    .build();
        }

        @Getter 
        @AllArgsConstructor
        @Builder
        @NoArgsConstructor
        @ToString
        public static class FolderCreateServiceReq{
            
            private Long itemId;
            private Long parentId;
            private String name;
            // private Long userId;

            public Folder toEntity(Long userId){
                return new Folder(
                    this.name, this.itemId,
                    this.parentId, userId);
            }
        }

    }

    //Folder - Read
    public interface FolderRes{
        Long getItemId();
        Long getParentId();
        String getName();
        // List<TagRes> getTags();
        BigDecimal getVisitCount();
    }

    //Folder - Update
    @Getter @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class FolderUpdateReq{
        @JsonProperty("itemId") @JsonAlias("item_id")
        private Long itemId;
        private Long parentId;
        @Size(max=50)
        private String name;

        private BigDecimal visitCount;

        public FolderUpdateServiceReq toServiceReq(){
            return FolderUpdateServiceReq.builder()
                    .itemId(this.itemId)
                    .parentId(this.parentId)
                    .name(this.name)
                    .visitCount(this.visitCount)
                    .build();
        }

        @Getter @Builder
        @AllArgsConstructor @NoArgsConstructor
        public static class FolderUpdateServiceReq{
            private Long itemId;
            private Long parentId;
            private String name;
            private BigDecimal visitCount;
        }
    }





    //Bookmark - Create
    @AllArgsConstructor @NoArgsConstructor
    @Builder @ToString @Getter
    public static class BookmarkCreateReq{
        @NotNull @JsonProperty("itemId") @JsonAlias("item_id")
        private Long itemId;
        @NotNull
        private Long parentId;
        

        @NotNull @NotBlank @Size(max=50)
        private String name;
        
        @NotNull @NotBlank
        private String url;
        
        @Size(max=50)
        private String comment;

        @AllArgsConstructor @NoArgsConstructor
        @Builder @ToString @Getter
        public static class BookmarkCreateServiceReq{
            private Long itemId;
            private Long parentId;
            private String name;
            private String url;
            private String comment;

            public Bookmark toEntity(Long userId){
                return new Bookmark(
                    name, itemId,
                    parentId, userId,
                    url, comment);
            }       
        }

        public BookmarkCreateServiceReq toServiceReq(){
            return BookmarkCreateServiceReq.builder()
                    .itemId(this.itemId)
                    .parentId(this.parentId)
                    .name(this.name)
                    .url(this.url)
                    .comment(this.comment)
                    .build();
        }
        
        public Bookmark toEntity(Long userId){
            return new Bookmark(
                name, itemId,
                parentId, userId,
                url, comment);
        }
    }

    //Bookmark - Read
    public interface BookmarkRes{
        Long getItemId();
        Long getParentId();
        String getName();
        String getUrl();
        String getComment();
        // List<TagRes> getTags();

        BigDecimal getVisitCount();
    }


    //Bookmark - Update
    @Getter @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class BookmarkUpdateReq{ //UpdateReq 는 Validation이 필요없음, API단에서 BookmarkId Validation만 필요 
        private Long itemId;
        private Long parentId;
        private String name;
        private String url;
        private String comment;
        private BigDecimal visitCount;

        public BookmarkUpdateServiceReq toServiceReq(){
            return BookmarkUpdateServiceReq.builder()
                    .itemId(this.itemId)
                    .parentId(this.parentId)
                    .name(this.name)
                    .url(this.url)
                    .comment(this.comment)
                    .visitCount(this.visitCount)
                    .build();
        }

        @Getter @Builder
        @AllArgsConstructor @NoArgsConstructor
        public static class BookmarkUpdateServiceReq{
            private Long itemId;
            private Long parentId;
            private String name;
            private String url;
            private String comment;
            private BigDecimal visitCount;
        
        }
    }
    

    




    // Query-Projection (QueryDSL)
    @AllArgsConstructor @NoArgsConstructor
    @Builder @ToString @Getter
    public static class FolderResImpl implements FolderRes{
        private Long itemId;
        private Long parentId;
        private String name;
        // private List<TagRes> tags;
        private BigDecimal visitCount; 
        
        @QueryProjection
        public FolderResImpl(
            Long itemId
        ){
            this.itemId=itemId;
        }
    }

    @AllArgsConstructor @NoArgsConstructor
    @Builder @ToString @Getter
    public static class FolderResWithTag{
        private Long itemId;
        private Long parentId;
        private String name;
        private List<TagRes> tags;
        private BigDecimal visitCount; 
    }

    @AllArgsConstructor @NoArgsConstructor
    @Builder @ToString @Getter
    public static class AllFolderResWithTag{
        private List<FolderResWithTag> folders;
    }

    
    @AllArgsConstructor @NoArgsConstructor
    @Builder @ToString @Getter
    public static class BookmarkResImpl implements BookmarkRes{
        private Long itemId;
        private Long parentId;
        private String name;
        private String url;
        private String comment;
        // private List<TagRes> tags;
        private BigDecimal visitCount;
        
    }

    @AllArgsConstructor @NoArgsConstructor
    @Builder @ToString @Getter
    public static class BookmarkResWithTag{
        private Long itemId;
        private Long parentId;
        private String name;
        private String url;
        private String comment;
        private List<TagRes> tags;
        private BigDecimal visitCount; 
    }






    
    

    




}
