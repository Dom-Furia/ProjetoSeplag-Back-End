package com.seplag.api.domain.artista;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seplag.api.domain.album.Album;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "artista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private String nacionalidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoArtista tipo;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant criadoEm;

    @JsonIgnore
    @ManyToMany(mappedBy = "artistas", fetch = FetchType.LAZY)
    private Set<Album> albums = new HashSet<>();

}
