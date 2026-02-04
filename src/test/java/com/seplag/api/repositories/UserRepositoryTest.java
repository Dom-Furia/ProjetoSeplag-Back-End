package com.seplag.api.repositories;


import com.seplag.api.domain.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("Should Get E-mail sucessfully from DB")
    void findByEmailCase1() {

        // Arrange (dado)
        String email = "teste@email.com";
        User user = new User();
        user.setEmail(email);
        user.setName("Usuário Teste");
        user.setPassword("123456");

        entityManager.persist(user);
        entityManager.flush();

        // Act (ação)
        Optional<User> result = userRepository.findByEmail(email);

        // Assert (verificação)
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }



}