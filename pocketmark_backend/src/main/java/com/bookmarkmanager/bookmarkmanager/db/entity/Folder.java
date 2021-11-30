package com.bookmarkmanager.bookmarkmanager.db.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class Folder extends DbEntity {

    private Long parent; // if parent ==null => 최상위 디렉토리
    private String name;
    private int totalVisitCount;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name="folder_id")
    @ToString.Exclude
    private List<Bookmark> bookmarks;

}
