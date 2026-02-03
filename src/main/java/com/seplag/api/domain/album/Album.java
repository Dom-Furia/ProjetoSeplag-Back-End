package com.seplag.api.domain.album;

import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.imagem_capa.CapaAlbum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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

    @Column(name = "nome_album", nullable = false)
    private String nomeAlbum;

    @Column(name = "ano_lancamento")
    private String anoLancamento;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private Instant criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private Instant atualizadoEm;

    // Album -> Artistas
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "artista_album",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artista_id")
    )
    private Set<Artista> artistas = new HashSet<>();

    // Album -> Capas
    @OneToMany(
            mappedBy = "album",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<CapaAlbum> capas = new HashSet<>();
}
