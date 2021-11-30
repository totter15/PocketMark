package com.bookmarkmanager.bookmarkmanager.db.login;

import javax.persistence.Entity;

import com.bookmarkmanager.bookmarkmanager.db.entity.DbEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends DbEntity{
    private String userId;
    private String userPw;
    private String email;
}
