CREATE TABLE regional (
                          id BIGSERIAL NOT NULL,
                          id_externo INTEGER NOT NULL,
                          nome VARCHAR(200) NOT NULL,
                          ativo BOOLEAN NOT NULL DEFAULT TRUE,
                          data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT pk_regional PRIMARY KEY (id)
);

