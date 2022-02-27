package com.example.pocketmark.domain.main;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.pocketmark.domain.User;
import com.example.pocketmark.domain.base.BaseEntity;
import com.example.pocketmark.domain.main.Item.ItemPK;
import com.example.pocketmark.domain.main.embeddable.Tags;
import com.example.pocketmark.dto.main.TagDto.TagRes;
import com.example.pocketmark.dto.main.TagDto.TagResImpl;
import com.example.pocketmark.service.TagService;

import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Immutable;
import org.springframework.data.domain.Persistable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="dtype")
@Getter
@AllArgsConstructor
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
// @IdClass(ItemPK.class)
@Slf4j
@Where(clause = "deleted = false")
@NamedNativeQuery(
    name= "Item.getLastItemId",
    query="select i.item_id from item i where i.user_id = :userId order by i.item_id desc limit 1"
)
@Table(indexes = @Index(name="i_item_parent_id", columnList = "parent_id"))
public class Item extends BaseEntity implements Persistable<String>{
    
    /* Table-Field Area */
    @Id
    // 조인속도향상 및 JPQL, DSL IN절 사용가능
    @Column(name="pk")
    private String pk;

    @Transient
    private boolean isNew=true;
    @Override
    public String getId() {
        return this.pk;
    }

    @Override
    public boolean isNew(){
        return this.isNew;
    }

    public void setIsNew(boolean isNew){
        this.isNew = isNew;
    }
    
    
    @Column(name="item_id") 
    private Long itemId; 
    @Column(name = "user_id")
    private Long userId; // FK

    @Column(name="parent_id")
    private Long parentId;
    private String name;
    private BigDecimal visitCount;


    /* Non-Table-Field Area */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = User.class)
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    @ToString.Exclude
    private User user;



    

    @Embedded
    private Tags tags = new Tags();

    public List<Tag> getTags(){
        return this.tags.getTags();
    }

    public static String makePK(Long itemId, Long userId){
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(itemId));
        sb.append(", ");
        sb.append(String.valueOf(userId));
        return sb.toString();
    }
    


    public boolean visitCountUpdate(BigDecimal cnt){
        if(cnt.compareTo(this.visitCount) == 1){
            this.visitCount=cnt;
            return true;
        }else{
            log.error("Unvalid VisitCntUpdate");
            return false;
        }
    }





    public Item(
        Long itemId, Long userId,    
        Long parentId, String name
    ){
        this.pk=makePK(itemId, userId);
        this.itemId=itemId;
        this.userId=userId;
        this.parentId=parentId;
        this.name=name;
        this.visitCount=BigDecimal.valueOf(0L);
    }


    @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode @Getter
    public static class ItemPK implements Serializable{
        private Long itemId;
        private Long userId;
    }


    

}
