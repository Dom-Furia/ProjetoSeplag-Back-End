package com.seplag.api.domain.refreshtoken;

import com.seplag.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiracao;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
