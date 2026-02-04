-- ================================
-- TABELA ARTISTA
-- ================================

CREATE TABLE artista (
                         id UUID PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         nacionalidade VARCHAR(255),
                         tipo VARCHAR(30) NOT NULL,
                         criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         atualizado_em TIMESTAMP

);

CREATE INDEX idx_artista_nome ON artista(nome);

-- ================================
-- TABELA ALBUM
-- ================================

CREATE TABLE album (
                       id UUID PRIMARY KEY,
                       nome_album VARCHAR(255) NOT NULL,
                       ano_lancamento VARCHAR(4),
                       criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       atualizado_em TIMESTAMP

);

CREATE INDEX idx_album_nome ON album(nome_album);

-- ================================
-- TABELA CAPA_ALBUM
-- ================================

CREATE TABLE capa_album (
                            id UUID PRIMARY KEY,
                            object_name VARCHAR(500) NOT NULL,
                            album_id UUID NOT NULL,
                            criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            atualizado_em TIMESTAMP

);

ALTER TABLE capa_album
    ADD CONSTRAINT fk_capa_album_album
        FOREIGN KEY (album_id)
            REFERENCES album(id)
            ON DELETE CASCADE;

-- ================================
-- TABELA ARTISTA_ALBUM (N:N)
-- ================================

CREATE TABLE artista_album (
                               album_id UUID NOT NULL,
                               artista_id UUID NOT NULL,
                               PRIMARY KEY (album_id, artista_id)
);

ALTER TABLE artista_album
    ADD CONSTRAINT fk_artista_album_album
        FOREIGN KEY (album_id)
            REFERENCES album(id)
            ON DELETE CASCADE;

ALTER TABLE artista_album
    ADD CONSTRAINT fk_artista_album_artista
        FOREIGN KEY (artista_id)
            REFERENCES artista(id)
            ON DELETE CASCADE;
