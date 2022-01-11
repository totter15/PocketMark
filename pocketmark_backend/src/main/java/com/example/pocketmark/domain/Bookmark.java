package com.example.pocketmark.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.pocketmark.dto.BookmarkDto.BookmarkResImpl;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateReq;
import com.example.pocketmark.dto.BookmarkDto.BookmarkUpdateServiceReq;

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

public class Bookmark extends BaseEntity {
    private String name;
    private String url;
    private String comment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="folder_id")
    @ToString.Exclude
    private Folder folder;

    @Column(name="folder_id", insertable = false, updatable = false)
    private Long folderId;

    private Integer visitCount;


    //non-column field (DB와 동기화 되지 않은 새폴더에 하위로 생기는 북마크들을 위한 필드)
    @Transient
    private Long tempFolderId;









    public boolean visitCountUpdate(int cnt){
        if(cnt>this.visitCount){
            this.visitCount=cnt;
            return true;
        }else{
            log.error("Unvalid VisitCntUpdate");
            return false;
        }
    } 

    

    public BookmarkResImpl toJson(){
        return BookmarkResImpl.builder()
            .name(this.name)
            .url(this.url)
            .comment(this.comment)
            .folderId(this.folder.getId())
            .visitCount(this.visitCount)
            .build();
    }

    public void update(BookmarkUpdateServiceReq req){
        Folder dummy = new Folder();
        dummy.setId(req.getFolderId());

        this.name = req.getName();
        this.url = req.getUrl();
        this.comment = req.getComment();
        this.folder = dummy;
        this.visitCount = req.getVisitCount();
    }

    
    
}
