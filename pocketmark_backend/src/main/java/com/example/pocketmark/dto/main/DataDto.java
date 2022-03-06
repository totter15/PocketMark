package com.example.pocketmark.dto.main;

import java.util.List;

import com.example.pocketmark.dto.main.ItemDto.BookmarkCreateReq;
import com.example.pocketmark.dto.main.ItemDto.BookmarkRes;
import com.example.pocketmark.dto.main.ItemDto.BookmarkResWithTag;
import com.example.pocketmark.dto.main.ItemDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.main.ItemDto.FolderCreateReq;
import com.example.pocketmark.dto.main.ItemDto.FolderRes;
import com.example.pocketmark.dto.main.ItemDto.FolderResWithTag;
import com.example.pocketmark.dto.main.ItemDto.FolderUpdateReq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DataDto {
    


    
    @AllArgsConstructor @NoArgsConstructor // for objectMapper
    @Builder @Getter
    public static class DataRes{
        Long targetId;
        List<FolderResWithTag> folders;
        List<BookmarkResWithTag> bookmarks;
    }
    
    //Data - Update
    @AllArgsConstructor @NoArgsConstructor // for objectMapper
    @Builder @Getter
    public static class DataUpdateReq{
        List<FolderUpdateReq> folders;
        List<BookmarkUpdateReq> bookmarks;
        
        public DataUpdateServiceReq toServcieReq(){
            return DataUpdateServiceReq.builder()
                    .folders(this.folders)
                    .bookmarks(this.bookmarks)
                    .build();
        }

        @AllArgsConstructor @NoArgsConstructor
        @Builder @Getter
        public static class DataUpdateServiceReq{
            List<FolderUpdateReq> folders;
            List<BookmarkUpdateReq> bookmarks;

            public boolean isFoldersExist(){
                if(folders.size()==0) return false;
                return true;
            }
            public boolean isBookmarksExist(){
                if(bookmarks.size()==0) return false;
                return true;
            }
        }
    }
    
    


    @AllArgsConstructor @NoArgsConstructor
    @Builder @Getter
    public static class DataCreateReq{

        List<FolderCreateReq> folders;
        List<BookmarkCreateReq> bookmarks;

        public DataCreateServiceReq toServiceReq(){
            return DataCreateServiceReq.builder()
                    .folders(this.folders)
                    .bookmarks(this.bookmarks)
                    .build();
        }


        @AllArgsConstructor @NoArgsConstructor
        @Builder @Getter 
        public static class DataCreateServiceReq{
            List<FolderCreateReq> folders;
            List<BookmarkCreateReq> bookmarks;
        }
    }


    


    @AllArgsConstructor @NoArgsConstructor
    @Builder @Getter
    public static class DataDeleteReq{
        List<Long> folderIdList;
        List<Long> bookmarkIdList;
        public DataDeleteServiceReq toServiceReq(){
            return DataDeleteServiceReq.builder()
                    .folderIdList(this.folderIdList)
                    .bookmarkIdList(this.bookmarkIdList)
                    .build();
        }

        @AllArgsConstructor @NoArgsConstructor
        @Builder @Getter
        public static class DataDeleteServiceReq{
            List<Long> folderIdList;
            List<Long> bookmarkIdList;
        }
    }


}
