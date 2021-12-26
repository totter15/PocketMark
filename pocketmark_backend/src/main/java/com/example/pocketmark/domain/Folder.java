package com.example.pocketmark.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.pocketmark.dto.FolderDto;
import com.example.pocketmark.dto.FolderDto.FolderRes;
import com.example.pocketmark.dto.FolderDto.FolderResImpl;
import com.example.pocketmark.dto.FolderDto.FolderUpdateReq;


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
public class Folder extends BaseEntity {
    private Long parent;
    private Long depth;
    private String name;

    @ManyToOne(cascade = {CascadeType.DETACH,
                         CascadeType.MERGE,
                         CascadeType.PERSIST
                         },
                fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private User user;    


    private int visitCount;

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
            .userId(this.user.getId())
            .visitCount(this.visitCount)
            .build();
    }

    public void update(FolderUpdateReq req){
        this.parent = req.getParent();
        this.depth = req.getDepth();
        this.name = req.getName();
        this.visitCount = req.getVisitCount();
    }
}
