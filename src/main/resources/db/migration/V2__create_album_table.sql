CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE album (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       nomeAlbum VARCHAR(255) NOT NULL,
                       anoLancamento VARCHAR(4) NOT NULL,
                       imgUrl VARCHAR(255) NOT NULL,
                       criadoEm TIMESTAMP NOT NULL
);
