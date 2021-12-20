package com.bookmarkmanager.bookmarkmanager.db.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import com.bookmarkmanager.bookmarkmanager.db.entity.base.DbEntity;


@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class Bookmark extends DbEntity {
    private String name;
    private String url;
    private int visitCount;


    @ManyToOne(cascade =
            {CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH})
    // @ManyToOne
    @ToString.Exclude
    @JoinColumn(name="folder_no")
    private Folder folder;

}
