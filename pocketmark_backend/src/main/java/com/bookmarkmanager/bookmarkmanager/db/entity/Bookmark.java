package com.bookmarkmanager.bookmarkmanager.db.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class Bookmark extends DbEntity {
    private String name;
    private int visitCount;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name="bookmark_id")
    @ToString.Exclude
    private List<Url> urls = new ArrayList<>(); //url.get(0) - main


    @ManyToOne(cascade =
            {CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH})
    private Folder folder;

}
