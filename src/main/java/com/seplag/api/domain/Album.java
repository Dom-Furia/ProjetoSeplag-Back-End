package com.seplag.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Table(name = "album")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Album {
    @Id
    @GeneratedValue
    private UUID id;


    private String nomeAlbum;
    private String anoLancamento;
    private String criadoEm;

    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

}
