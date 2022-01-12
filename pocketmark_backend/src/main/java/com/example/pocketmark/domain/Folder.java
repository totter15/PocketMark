package com.example.pocketmark.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.pocketmark.dto.FolderDto;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;
import com.example.pocketmark.dto.FolderDto.FolderUpdateServiceReq;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Where;
import org.springframework.boot.jackson.JsonObjectSerializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@Slf4j
@Table(indexes = @Index(name="i_folder_id", columnList = "folder_id"))
public class Folder extends BaseEntity {
    private Long parent;
    private Long depth;
    private String name;

    @ManyToOne(cascade = {CascadeType.DETACH,
                         CascadeType.MERGE,
                         CascadeType.PERSIST,
                         },
                fetch = FetchType.LAZY,
                targetEntity = User.class)
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private User user;    

    @Column(name="user_id", insertable = false, updatable = false)
    private Long userId;

    private Integer visitCount;

    // //non-column field (DB와 동기화 되지 않은 새폴더에 하위로 생기는 폴더들을 위한 필드)
    // @Transient
    // private Long tempParent;

    // BatchInsert 를 위한 필드
    @Column(name="folder_id")
    private Long folderId;














    public boolean visitCountUpdate(int cnt){
        if(cnt>this.visitCount){
            this.visitCount=cnt;
            return true;
        }else{
            log.error("Unvalid VisitCntUpdate");
            return false;
        }
    } 

    public FolderResImpl toJson(){

        // return new FolderResImpl(this.depth,this.parent,this.user.getId(),this.name);

        return FolderResImpl.builder()
            .name(this.name)
            .depth(this.depth)
            .parent(this.parent)
            .visitCount(this.visitCount)
            .build();
    }

    public void update(FolderUpdateServiceReq req){
        this.parent = req.getParent();
        this.depth = req.getDepth();
        this.name = req.getName();
        this.visitCount = req.getVisitCount();
    }
}
