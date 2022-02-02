package com.example.pocketmark.dto.main;

import java.util.List;

import com.example.pocketmark.domain.main.Item;
import com.example.pocketmark.domain.main.Tag;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class TagDto {

    
    public interface TagRes{
        String getName();
    }
    public interface TagIdOnly{
        String getId();
    }

    public interface TagResWithItemId{
        String getName();
        Long getItemId();
    }

    @Getter @ToString
    @AllArgsConstructor @NoArgsConstructor
    public static class TagResImpl implements TagRes{
        private String name;
    }

    @Getter @Builder @ToString
    @AllArgsConstructor @NoArgsConstructor
    public static class TagCreateBulkReq{
        List<TagCreateReq> tags;
    }

    @Getter @Builder @ToString
    @AllArgsConstructor @NoArgsConstructor
    public static class TagDeleteBulkReq{
        List<TagDeleteReq> tags;
    }


    @Getter @Builder @ToString
    @AllArgsConstructor @NoArgsConstructor
    public static class TagCreateReq{
        private Long itemId;
        private String name;

        
        public TagCreateServiceReq toServiceReq(){
            return TagCreateServiceReq.builder()
                    .itemId(this.itemId)
                    .name(this.name)
                    .build();
        }
        public Tag toEntity(Long itemId, Long userId, Item item){
            // return Tag.builder()
            //         .itemId(itemId)
            //         .userId(userId)
            //         .name(this.name)
            //         .item(item)
            //         .build();
            return new Tag(this.itemId, userId, this.name, item);
        }

        @Getter @Builder @ToString
        @AllArgsConstructor @NoArgsConstructor
        public static class TagCreateServiceReq{
            private Long itemId;
            private String name;

            // public Tag toEntity(Item item){
            //     // return Tag.builder()
            //     //         .name(this.name)
            //     //         .item(item)
            //     //         .build();
            // }
        }
    }

    @Getter @Builder @ToString
    @AllArgsConstructor @NoArgsConstructor
    public static class TagDeleteReq{
        private Long itemId;
        private String name;
    }


}
