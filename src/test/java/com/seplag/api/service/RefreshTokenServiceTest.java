package com.seplag.api.service;

import com.seplag.api.domain.refreshtoken.RefreshToken;
import com.seplag.api.domain.user.User;
import com.seplag.api.repositories.RefreshTokenRepository;
import com.seplag.api.security.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository repository;

    @InjectMocks
    private RefreshTokenService service;

    @Test
    @DisplayName("Deve criar o refreshtoken com sucesso")
    void deveCriarRefreshTokenComSucesso() {
        User user = new User();

        String token = service.create(user);

        assertNotNull(token);
        verify(repository).deleteByUser(user);
        verify(repository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("O RefreshToken deve expirar em 7 dias")
    void refreshTokenDeveExpirarEm7Dias() {
        User user = new User();

        ArgumentCaptor<RefreshToken> captor =
                ArgumentCaptor.forClass(RefreshToken.class);

        service.create(user);

        verify(repository).save(captor.capture());

        RefreshToken saved = captor.getValue();

        assertNotNull(saved.getExpiracao());
        assertTrue(saved.getExpiracao().isAfter(Instant.now()));
    }
    //----------------Teste de Exceções------------------------------//

    @Test
    @DisplayName("Deve lançar uma exceção quando token for inexistente")
    void deveLancarExcecaoQuandoTokenNaoExiste() {
        when(repository.findByToken("token-invalido"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.validate("token-invalido")
        );

        assertEquals("Refresh token inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lança uma exceção quando o token tiver expirado")
    void deveLancarExcecaoQuandoTokenExpirado() {
        User user = new User();

        RefreshToken refresh = new RefreshToken();
        refresh.setUser(user);
        refresh.setToken("token-expirado");
        refresh.setExpiracao(Instant.now().minusSeconds(10));

        when(repository.findByToken("token-expirado"))
                .thenReturn(Optional.of(refresh));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.validate("token-expirado")
        );

        assertEquals("Refresh token expirado", exception.getMessage());
    }


}