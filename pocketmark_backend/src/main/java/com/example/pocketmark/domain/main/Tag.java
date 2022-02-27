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
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.pocketmark.domain.base.BaseImmutableEntity;

import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Persistable;

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
public class Tag extends BaseImmutableEntity implements Persistable<String>{
    
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    //"itemId, name, userId"
    private String id;

    @Transient
    private boolean isNew=true;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isNew(){
        return this.isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew(){
        this.isNew=false;
    }

    public void setIsNew(boolean isNew){
        this.isNew = isNew;
    }



    //데이터분석 때 itemId, userId 랑 태그를 같이 xml로 만들어야할텐데
    //한 아이템을 두고 다른별칭을 어떻게 지었는가를 볼 수 있는 지표 (자연어 동의어 학습데이터)
    //이 또한 유사태그추천 같은 서비스의 일부분이 될꺼니, 빈번히 호출될 듯 함
    //마찬가지로 itemPK를 concat 가능하다면 굳이 필드로 안두어도 되긴함..  
    //생각정리 될 떄까지 일단 두자...

    //itemId, userId를 필드에 둘때 단점
    //1. 칼럼이 2개 더 증가
    //-> 부모하나에 자식이 2개라면 tag가 제일 양이 많을껀데
    //-> 거기에 칼럼을 2개 더 추가하는게 과연 옳은가
    //itemId, userId를 필드에 둘때 장점
    //1(x). 태그검색때 join 이 발생하지않는다.
    //-> 없으면 태그가 있는 item 1개당 tag 조회 join 쿼리 1회
    //-> itemPk(insertable=false) 필드로 해결가능

    //공유게시판 태그검색 때 빈번히 호출됨
    //findByItemPkStartWith 이런거로 해봄직함. 가능하면 삭제할 필드
    @Column(name="item_id")
    @ToString.Exclude
    private Long itemId; 

    //본인 게시물 태그검색 때 빈번히 호출됨
    @Column(name="user_id")
    @ToString.Exclude
    private Long userId;

    @Column(name="name")
    private String name;

    @Column(name="item_pk",insertable = false, updatable = false)
    private String itemPk; //FK


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="item_pk", referencedColumnName = "pk")
    @ToString.Exclude
    Item item;

    // public void setItem(Item item){
    //     this.item = item;
    // }

    
    public Tag(Long itemId, Long userId, String name, Item item){
        this.id=makePK(itemId, userId, name);
        this.itemId=itemId;
        this.userId=userId;
        this.name=name;
        this.item=item;
    }

    public static String makePK(Long itemId, Long userId, String name){
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(itemId));
        sb.append(", ");
        sb.append(String.valueOf(userId));
        sb.append(", ");
        sb.append(name);
        return sb.toString();
    }



    /* 생성과 삭제는 Tag 컨트롤러로 */
    /* 업데이트는 프론트 UI에 없음 */
}
