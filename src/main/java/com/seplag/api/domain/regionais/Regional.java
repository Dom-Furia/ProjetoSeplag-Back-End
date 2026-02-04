package com.seplag.api.domain.regionais;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "regional")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Regional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_externo", nullable = false)
    private Integer idExterno;

    @Column(length = 200, nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;


    @Column(name = "data_criacao", updatable = false, insertable = false)
    private Instant dataCriacao;

}
