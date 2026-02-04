package com.seplag.api.repositories;

import com.seplag.api.domain.refreshtoken.RefreshToken;
import com.seplag.api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve deletar todos os tokens de um usuário")
    void testDeleteByUser() {
        // Criar usuário mínimo
        User user = new User();
        user.setEmail("teste@dominio.com");
        user.setName("Teste");
        user.setPassword("123456");
        userRepository.save(user);

        // Criar um token associado
        RefreshToken token = new RefreshToken();
        token.setToken("token123");
        token.setExpiracao(Instant.now());
        token.setUser(user);
        refreshTokenRepository.save(token);

        // Verificar que o token existe
        assertThat(refreshTokenRepository.findByToken("token123")).isPresent();

        // Chamar o metodo que queremos testar
        refreshTokenRepository.deleteByUser(user);

        // Verificar que o token foi realmente deletado
        assertThat(refreshTokenRepository.findByToken("token123")).isEmpty();
    }
}