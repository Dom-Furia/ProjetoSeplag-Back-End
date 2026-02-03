CREATE TABLE capa_album (
                            id UUID PRIMARY KEY,
                            url_imagem VARCHAR(500) NOT NULL,
                            tipo VARCHAR(30),
                            criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            atualizado_em TIMESTAMP,
                            album_id UUID NOT NULL
);

ALTER TABLE capa_album
    ADD CONSTRAINT fk_capa_album_album
        FOREIGN KEY (album_id)
            REFERENCES album(id)
            ON DELETE CASCADE;