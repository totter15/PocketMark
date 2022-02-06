package com.example.pocketmark.domain.main;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.pocketmark.domain.base.BaseEntity;
import com.example.pocketmark.domain.base.BaseEntityWithId;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.Where;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@Slf4j

public class Bookmark extends Item {
    
    private String url;
    private String comment;





    public Bookmark(
        String name, Long itemId, Long parentId,
        Long userId, String url, String comment
    ){
        super(itemId, userId, parentId, name);
        this.url = url;
        this.comment = comment;
    }




    

    // public BookmarkResImpl toJson(){
    //     return BookmarkResImpl.builder()
    //         .name(this.name)
    //         .url(this.url)
    //         .comment(this.comment)
    //         .folderId(this.folder.getId())
    //         .visitCount(this.visitCount)
    //         .build();
    // }

    // public void update(BookmarkUpdateServiceReq req){
    //     Folder dummy = new Folder();
    //     dummy.setId(req.getFolderId());

    //     this.name = req.getName();
    //     this.url = req.getUrl();
    //     this.comment = req.getComment();
    //     this.folder = dummy;
    //     this.visitCount = req.getVisitCount();
    // }

    
    
}
