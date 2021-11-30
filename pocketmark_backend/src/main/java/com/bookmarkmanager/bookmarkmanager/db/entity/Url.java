package com.bookmarkmanager.bookmarkmanager.db.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class Url extends DbEntity {
    private String name;
    private String url;

    @ManyToOne(cascade =
            {CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH})
    private Bookmark bookmark;
}
