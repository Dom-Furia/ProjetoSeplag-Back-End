
CREATE TABLE album (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       nome_album VARCHAR(255) NOT NULL,
                       ano_lancamento VARCHAR(4) NOT NULL,
                       img_url VARCHAR(255) NOT NULL,
                       criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       artista_id UUID NOT NULL,

                        CONSTRAINT fk_album_artista
                        FOREIGN KEY (artista_id)
                        REFERENCES artista (id)
                        ON DELETE CASCADE
);
