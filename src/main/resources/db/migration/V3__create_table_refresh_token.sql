CREATE TABLE refresh_token (
                               id UUID PRIMARY KEY ,
                               token TEXT NOT NULL UNIQUE,
                               expiracao TIMESTAMP NOT NULL,
                               user_id UUID NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_refresh_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users (id)
                                       ON DELETE CASCADE
);


