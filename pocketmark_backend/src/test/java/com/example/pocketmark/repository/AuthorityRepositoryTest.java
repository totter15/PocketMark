// package com.example.pocketmark.repository;

// import com.example.pocketmark.config.JpaConfig;
// import com.example.pocketmark.domain.Authority;
// import com.example.pocketmark.domain.User;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.transaction.annotation.Transactional;

// import javax.persistence.EntityNotFoundException;
// import java.util.List;
// import java.util.Set;

// import static org.assertj.core.api.BDDAssertions.then;
// import static org.junit.jupiter.api.Assertions.*;

// @DataJpaTest
// @Transactional
// @Import(JpaConfig.class)
// class AuthorityRepositoryTest {

//     @Autowired
//     UserRepository userRepository;

//     @Autowired
//     AuthorityRepository authorityRepository;



//     public User createUser(){
//         return new User("test@gmail.com","1234","JyuKa");
//     }
// }