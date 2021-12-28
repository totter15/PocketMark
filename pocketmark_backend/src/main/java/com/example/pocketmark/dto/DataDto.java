package com.example.pocketmark.dto;

import java.util.List;

import com.example.pocketmark.domain.Bookmark;
import com.example.pocketmark.domain.Folder;
import com.example.pocketmark.dto.BookmarkDto.BookmarkRes;
import com.example.pocketmark.dto.BookmarkDto.BookmarkResImpl;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DataDto {

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor // for objectMapper
    public static class DataRes{
        Long depth;
        List<FolderRes> folders;
        List<BookmarkRes> bookmarks;
    }
}
