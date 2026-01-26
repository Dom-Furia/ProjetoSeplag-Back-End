CREATE TABLE artista_album (
                       album_id UUID NOT NULL,
                       artista_id UUID NOT NULL,

                       CONSTRAINT pk_artista_album
                           PRIMARY KEY (album_id, artista_id),

                       CONSTRAINT fk_artista_album_album
                           FOREIGN KEY (album_id) REFERENCES album(id),

                       CONSTRAINT fk_artista_album_artista
                           FOREIGN KEY (artista_id) REFERENCES artista(id)
);
