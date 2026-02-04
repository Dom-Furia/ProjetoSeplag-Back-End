package com.seplag.api.security;


import com.seplag.api.domain.refreshtoken.RefreshToken;
import com.seplag.api.domain.user.User;
import com.seplag.api.repositories.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public User validate(String token) {

        RefreshToken refresh = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (refresh.getExpiracao().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expirado");
        }

        return refresh.getUser();
    }

    @Transactional
    public String create(User user) {

        repository.deleteByUser(user); // garante 1 refresh token por usuário

        RefreshToken refresh = new RefreshToken();
        refresh.setUser(user);
        refresh.setToken(UUID.randomUUID().toString());
        refresh.setExpiracao(
                Instant.now().plus(7, ChronoUnit.DAYS)
        );

        repository.save(refresh);
        return refresh.getToken();
    }
}

