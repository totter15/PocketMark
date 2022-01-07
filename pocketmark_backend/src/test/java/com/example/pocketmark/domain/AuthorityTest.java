package com.example.pocketmark.domain;

import com.example.pocketmark.repository.AuthorityRepository;
import com.example.pocketmark.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@Transactional
class AuthorityTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void createROLE_USER(){
        User user = createUser();
        user.setAuthorities(Set.of(Authority.USER_AUTHORITY));
        userRepository.saveAndFlush(user);


        List<Authority> authorities = userRepository.findByEmail(user.getEmail())
                .get()
                .getAuthorities()
                .stream().collect(Collectors.toList());

        then(authorities.get(0).getAuthority()).isEqualTo("ROLE_USER");
    }


    public User createUser(){
        return new User("test@gmail.com","1234","JyuKa");
    }
}