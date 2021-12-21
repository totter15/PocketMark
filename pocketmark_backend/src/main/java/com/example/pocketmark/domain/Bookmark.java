package com.example.pocketmark.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@Slf4j
public class Bookmark extends BaseEntity{
    private String name;
    private String url;
    private String comment;
    
    @ManyToOne( cascade = {CascadeType.DETACH,
                        CascadeType.MERGE,
                        CascadeType.PERSIST
                        },
                fetch = FetchType.LAZY)
    @JoinColumn(name="folder_id")
    private Folder folder;

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
    
    
}
