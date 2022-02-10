// package com.example.pocketmark.domain;

// import com.example.pocketmark.repository.AuthorityRepository;
// import com.example.pocketmark.repository.UserRepository;
// import com.example.pocketmark.util.Encryptor;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.transaction.annotation.Transactional;


// import javax.persistence.EntityManager;
// import javax.persistence.EntityNotFoundException;
// import javax.persistence.PersistenceContext;

// import java.time.LocalDateTime;
// import java.util.Set;

// import static org.assertj.core.api.BDDAssertions.then;
// import static org.assertj.core.api.Assertions.assertThat;


// @SpringBootTest
// @Transactional
// class UserTest {
//     @Autowired
//     UserRepository userRepository;

//     @Autowired
//     AuthorityRepository authorityRepository;

//     @Autowired
//     Encryptor encryptor;

//     @PersistenceContext
//     EntityManager em;

//     @AfterEach
//     void deleteAll(){
//         userRepository.deleteAll();
//         authorityRepository.deleteAll();
//     }

//     @DisplayName("createUser 테스트")
//     @Test
//     public void saveUser(){
//         User newUser = createUser();
//         userRepository.save(newUser);

//         em.flush();
//         em.clear();

//         User user = userRepository.findById(newUser.getId())
//                 .orElseThrow(EntityNotFoundException::new);


// //        then(user.getId()).isEqualTo(1L);
//         then(user.getEmail()).isEqualTo("test@gmail.com");
//         then(user.getPw()).isEqualTo("1234");
//         then(user.getCreatedAt()).isBefore(LocalDateTime.now());
//         then(user.getUpdatedAt()).isBefore(LocalDateTime.now());
//         then(user.isDeleted()).isFalse();

//     }

//     @DisplayName("비밀번호 암호화 테스트")
//     @Test
//     public void encryptorTest(){
//         User user = new User(
//                 "test@gmail.com",
//                 encryptor.encrypt("1234"),
//                 "JyuKa");

//         System.out.println(user.getPw());
//         then(user.isMatch(encryptor,"1234")).isTrue();
//     }

//     @DisplayName("비밀번호 변경")
//     @Test
//     public void changePassword(){
//         //Given
//         User user = createUser();
//         String newPw = "4321";

//         //When
//         user.changePassword(newPw);

//         //Then
//         then(user.getPw()).isEqualTo(newPw);

//     }

//     @DisplayName("비밀번호 변경")
//     @Test
//     public void changeNickName(){
//         //Given
//         User user = createUser();
//         String newNickName = "test";

//         //When
//         user.changeNickName(newNickName);

//         //Then
//         then(user.getNickName()).isEqualTo(newNickName);

//     }

//     @DisplayName("비밀번호 변경")
//     @Test
//     public void deleteUser(){
//         //Given
//         User user = createUser();
//         boolean modifyDeleted = true;

//         //When
//         user.deleteUser(modifyDeleted);

//         //Then
//         then(user.isDeleted()).isEqualTo(modifyDeleted);

//     }


//     @DisplayName("Authority 추가")
//     @Test
//     public void addAuthority(){

//         User user = createUser();
//         user.setAuthorities(Set.of(Authority.USER_AUTHORITY));
//         userRepository.save(user);

//         User savedUser = userRepository.findByEmail(user.getEmail())
//                 .orElseThrow(EntityNotFoundException::new);

//         then(savedUser.getAuthorities().size()).isEqualTo(1);
//     }


    

//     public User createUser(){
//         return new User("test@gmail.com","1234","JyuKa");
//     }




// }