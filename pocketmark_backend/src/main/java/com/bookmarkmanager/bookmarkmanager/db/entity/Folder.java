package com.bookmarkmanager.bookmarkmanager.db.entity;

import lombok.*;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.bookmarkmanager.bookmarkmanager.db.entity.base.DbEntity;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class Folder extends DbEntity {

    private Long parent; // if parent ==null => 최상위 디렉토리
    private String name;
    private int visitCount;

    @ManyToOne(cascade =
            {CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH})
    @JoinColumn(name="user_no")
    private User user;

    // @ManyToOne
    // private long userId;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name="folder_no")
    @ToString.Exclude
    @Builder.Default
    private List<Bookmark> bookmarks = new ArrayList<>();

}
