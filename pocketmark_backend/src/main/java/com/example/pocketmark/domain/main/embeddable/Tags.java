package com.example.pocketmark.domain.main.embeddable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import com.example.pocketmark.domain.main.Tag;
import com.example.pocketmark.dto.main.TagDto.TagRes;
import com.example.pocketmark.dto.main.TagDto.TagResImpl;

import org.hibernate.annotations.BatchSize;

import lombok.Getter;


@Embeddable
@Getter
public class Tags {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item"
    ,targetEntity = Tag.class)
    @BatchSize(size=100)
    private List<Tag> tags = new ArrayList<>();
    
    

}
