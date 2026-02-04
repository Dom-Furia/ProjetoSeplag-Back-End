-- ================================
-- ARTISTAS
-- ================================

INSERT INTO artista (id, nome, nacionalidade, tipo, criado_em) VALUES
                                                                   ('11111111-1111-1111-1111-111111111111', 'Serj Tankian', 'Armênio-Americano', 'CANTOR', CURRENT_TIMESTAMP),
                                                                   ('22222222-2222-2222-2222-222222222222', 'Mike Shinoda', 'Americano', 'CANTOR', CURRENT_TIMESTAMP),
                                                                   ('33333333-3333-3333-3333-333333333333', 'Michel Teló', 'Brasileiro', 'CANTOR', CURRENT_TIMESTAMP),
                                                                   ('44444444-4444-4444-4444-444444444444', 'Guns N’ Roses', 'Americano', 'BANDA', CURRENT_TIMESTAMP);

-- ================================
-- ALBUNS
-- ================================

INSERT INTO album (id, nome_album, ano_lancamento, criado_em) VALUES
-- Serj Tankian
('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'Harakiri', '2012', CURRENT_TIMESTAMP),
('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', 'Black Blooms', '2012', CURRENT_TIMESTAMP),
('aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', 'The Rough Dog', '2012', CURRENT_TIMESTAMP),

-- Mike Shinoda
('bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1', 'The Rising Tied', '2005', CURRENT_TIMESTAMP),
('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2', 'Post Traumatic', '2018', CURRENT_TIMESTAMP),
('bbbbbbb3-bbbb-bbbb-bbbb-bbbbbbbbbbb3', 'Post Traumatic EP', '2018', CURRENT_TIMESTAMP),
('bbbbbbb4-bbbb-bbbb-bbbb-bbbbbbbbbbb4', 'Where’d You Go', '2006', CURRENT_TIMESTAMP),

-- Michel Teló
('ccccccc1-cccc-cccc-cccc-ccccccccccc1', 'Bem Sertanejo', '2014', CURRENT_TIMESTAMP),
('ccccccc2-cccc-cccc-cccc-ccccccccccc2', 'Bem Sertanejo - O Show (Ao Vivo)', '2015', CURRENT_TIMESTAMP),
('ccccccc3-cccc-cccc-cccc-ccccccccccc3', 'Bem Sertanejo - (1ª Temporada) - EP', '2014', CURRENT_TIMESTAMP),

-- Guns N’ Roses
('ddddddd1-dddd-dddd-dddd-ddddddddddd1', 'Use Your Illusion I', '1991', CURRENT_TIMESTAMP),
('ddddddd2-dddd-dddd-dddd-ddddddddddd2', 'Use Your Illusion II', '1991', CURRENT_TIMESTAMP),
('ddddddd3-dddd-dddd-dddd-ddddddddddd3', 'Greatest Hits', '2004', CURRENT_TIMESTAMP);

-- ================================
-- RELACIONAMENTOS ARTISTA ↔ ALBUM
-- ================================

-- Serj Tankian
INSERT INTO artista_album (album_id, artista_id) VALUES
                                                     ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1','11111111-1111-1111-1111-111111111111'),
                                                     ('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2','11111111-1111-1111-1111-111111111111'),
                                                     ('aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3','11111111-1111-1111-1111-111111111111');

-- Mike Shinoda
INSERT INTO artista_album (album_id, artista_id) VALUES
                                                     ('bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbb1','22222222-2222-2222-2222-222222222222'),
                                                     ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2','22222222-2222-2222-2222-222222222222'),
                                                     ('bbbbbbb3-bbbb-bbbb-bbbb-bbbbbbbbbbb3','22222222-2222-2222-2222-222222222222'),
                                                     ('bbbbbbb4-bbbb-bbbb-bbbb-bbbbbbbbbbb4','22222222-2222-2222-2222-222222222222');

-- Michel Teló
INSERT INTO artista_album (album_id, artista_id) VALUES
                                                     ('ccccccc1-cccc-cccc-cccc-ccccccccccc1','33333333-3333-3333-3333-333333333333'),
                                                     ('ccccccc2-cccc-cccc-cccc-ccccccccccc2','33333333-3333-3333-3333-333333333333'),
                                                     ('ccccccc3-cccc-cccc-cccc-ccccccccccc3','33333333-3333-3333-3333-333333333333');

-- Guns N’ Roses
INSERT INTO artista_album (album_id, artista_id) VALUES
                                                     ('ddddddd1-dddd-dddd-dddd-ddddddddddd1','44444444-4444-4444-4444-444444444444'),
                                                     ('ddddddd2-dddd-dddd-dddd-ddddddddddd2','44444444-4444-4444-4444-444444444444'),
                                                     ('ddddddd3-dddd-dddd-dddd-ddddddddddd3','44444444-4444-4444-4444-444444444444');
