package com.example.pocketmark.domain;

import java.text.Format;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsUser extends BaseEntity {
    
    private String oauth2UserId;
    private Long userId;

    private String name;
    private String email;
    private Provider provider;

    // public static User toUser(){
    //     return User.builder().build();
    // }

    public static enum Provider{
        google{
            public SnsUser convert(OAuth2User user){
                return SnsUser.builder()
                    .oauth2UserId(String.format("%s_%s", name(), user.getAttribute("sub")))
                    .email(user.getAttribute("email"))
                    .name(user.getAttribute("name"))
                    .provider(google)
                    .build();
            }
        },
        naver{
            public SnsUser convert(OAuth2User user){
                Map<String,Object> resp = user.getAttribute("response");
                return SnsUser.builder()
                    .oauth2UserId(String.format("%s_%s", name(), resp.get("id")))
                    .email(""+resp.get("email"))
                    .name(""+resp.get("name"))
                    .provider(naver)
                    .build();
            }
        };

        public abstract SnsUser convert(OAuth2User userInfo);
    }
}
