package com.example.pocketmark.dto;

import java.util.List;

import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.dto.BookmarkDto.BookmarkCreateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;
import com.example.pocketmark.dto.BookmarkDto.BookmarkResImpl;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.FolderDto.FolderCreateReq;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;

import org.springframework.data.domain.Slice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DataDto {
    public static class DataReq{
        Long depth;
    }


    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor // for objectMapper
    public static class DataRes{
        Long depth;
        List<FolderRes> folders;
        List<BookmarkRes> bookmarks;
    }
    
    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor // for objectMapper
    public static class DataUpdateReq{
        List<FolderUpdateReq> folders;
        List<BookmarkUpdateReq> bookmarks;
        
        public DataUpdateServiceReq toServcieReq(){
            return DataUpdateServiceReq.builder()
                    .folders(this.folders)
                    .bookmarks(this.bookmarks)
                    .build();
        }

    }
    
    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor // for objectMapper
    public static class DataUpdateServiceReq{
        
        List<FolderUpdateReq> folders;
        List<BookmarkUpdateReq> bookmarks;
    }


    @Builder
    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class DataCreateReq{
        List<FolderCreateReq> folders;
        List<BookmarkCreateReq> bookmarks;

        public DataCreateServiceReq toServiceReq(){
            return DataCreateServiceReq.builder()
                    .folders(this.folders)
                    .bookmarks(this.bookmarks)
                    .build();
        }


        @Getter
        @AllArgsConstructor
        @Builder
        @NoArgsConstructor 
        public static class DataCreateServiceReq{
            List<FolderCreateReq> folders;
            List<BookmarkCreateReq> bookmarks;
        }
    }


    


    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor 
    public static class DataDeleteReq{
        List<Long> folderIdList;
        List<Long> bookmarkIdList;
        public DataDeleteServiceReq toServiceReq(){
            return DataDeleteServiceReq.builder()
                    .folderIdList(this.folderIdList)
                    .bookmarkIdList(this.bookmarkIdList)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor 
    public static class DataDeleteServiceReq{
        List<Long> folderIdList;
        List<Long> bookmarkIdList;
    }


}
