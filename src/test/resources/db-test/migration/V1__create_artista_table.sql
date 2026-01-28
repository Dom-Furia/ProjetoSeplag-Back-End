CREATE TABLE artista (
                         id UUID PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         nacionalidade VARCHAR(100) NOT NULL,
                         tipo VARCHAR(50) NOT NULL ,
                         criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP

);
