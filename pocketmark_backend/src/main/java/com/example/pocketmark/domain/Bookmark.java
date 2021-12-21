package com.example.pocketmark.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.pocketmark.domain.base.BaseEntity;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class Bookmark extends BaseEntity{
    private String name;
    private String url;
    private String comment;
    
    @ManyToOne
    @JoinColumn(name="folder_id")
    private Folder folder;

    private int visitCount;
    
    
}
