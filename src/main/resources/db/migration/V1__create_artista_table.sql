CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE artista (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         nome VARCHAR(255) NOT NULL,
                         nacionalidade VARCHAR(100) NOT NULL,
                         criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP

);
