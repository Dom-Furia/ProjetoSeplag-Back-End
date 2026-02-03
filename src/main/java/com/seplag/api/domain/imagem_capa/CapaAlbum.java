package com.seplag.api.domain.imagem_capa;


import com.seplag.api.domain.album.Album;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.UUID;

@Table(name = "capa_album")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CapaAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "object_name", nullable = false)
    private String objectName;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private Instant criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private Instant atualizadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

}
