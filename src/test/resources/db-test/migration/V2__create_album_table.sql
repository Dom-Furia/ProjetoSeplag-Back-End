
CREATE TABLE album (
                       id UUID PRIMARY KEY,
                       nome_album VARCHAR(255) NOT NULL,
                       ano_lancamento VARCHAR(4) NOT NULL,
                       img_url VARCHAR(255) NOT NULL,
                       criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP

);
