package com.example.pocketmark.domain.main;

import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.example.pocketmark.domain.base.BaseImmutableEntity;

import org.hibernate.annotations.Where;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @ToString
@Where(clause = "deleted = false")
@Table(indexes = @Index(name="i_tag_name", columnList = "name"))
public class Tag extends BaseImmutableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private Long id;

    @Column(name="item_id", insertable = false, updatable = false)
    @ToString.Exclude
    private Long itemId;
    @Column(name="user_id", insertable = false, updatable=false)
    @ToString.Exclude
    private Long userId;

    @Column(name="name")
    private String name;


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

    @Builder
    public Tag(String name, Item item){
        this.name=name;
        this.item=item;
    }

    /* 생성과 삭제는 Tag 컨트롤러로 */
    /* 업데이트는 프론트 UI에 없음 */
}
