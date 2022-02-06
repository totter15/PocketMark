package com.example.pocketmark.domain.main;

import java.math.BigDecimal;
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

import com.example.pocketmark.domain.User;
import com.example.pocketmark.domain.base.BaseEntity;
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
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder extends Item {
      
    public Folder(
        String name, Long itemId, Long parentId,
        Long userId
    ){
        super(itemId, userId, parentId, name);
    }

     

    // public FolderResImpl toJson(){

    //     // return new FolderResImpl(this.depth,this.parent,this.user.getId(),this.name);
    //     return FolderResImpl.builder()
    //         .name(this.name)
    //         .parent(this.parent)
    //         .visitCount(this.visitCount)
    //         .build();
    // }

    // public void update(FolderUpdateServiceReq req){
    //     this.parent = req.getParent();
    //     this.name = req.getName();
    //     this.visitCount = req.getVisitCount();
    // }


    
}
