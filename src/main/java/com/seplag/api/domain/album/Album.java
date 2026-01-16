package com.seplag.api.domain.album;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seplag.api.domain.artista.Artista;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;


@Table(name = "album")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nomeAlbum;
    private String anoLancamento;
    private String imgUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant criadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id", nullable = false)
    @JsonIgnore
    private Artista artista;

}
