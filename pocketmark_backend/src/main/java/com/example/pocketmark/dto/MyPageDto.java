package com.example.pocketmark.dto;

import com.example.pocketmark.domain.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MyPageDto {
    private String email;
    private String nickName;

    public static MyPageDto fromUser(User user){
        return MyPageDto.builder()
                .email(user.getEmail())
                .nickName(user.getNickName())
                .build()
                ;
    }

}
