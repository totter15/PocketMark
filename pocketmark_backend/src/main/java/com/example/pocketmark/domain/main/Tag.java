package com.example.pocketmark.domain.main;

import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="item_id", insertable = false, updatable = false)
    private Long itemId;
    @Column(name="user_id", insertable = false, updatable=false)
    private Long userId;

    private String tag;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    //2개이상인데 referencedColumn 없어도 잘매핑되네 
    @JoinColumns({
        @JoinColumn(name= "item_id"),
        @JoinColumn(name="user_id")
    })
    @ToString.Exclude
    Item item;

    public void setItem(Item item){
        this.item = item;
    }
}
