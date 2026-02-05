-- ================================
-- ARTISTAS
-- ================================
INSERT INTO artista (id, nome, nacionalidade, tipo)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Serj Tankian', 'Armênio-Americano', 'CANTOR'),
    ('22222222-2222-2222-2222-222222222222', 'Mike Shinoda', 'Americano', 'CANTOR'),
    ('33333333-3333-3333-3333-333333333333', 'Michel Teló', 'Brasileiro', 'CANTOR'),
    ('44444444-4444-4444-4444-444444444444', 'Guns N'' Roses', 'Americano', 'BANDA');

-- ================================
-- ALBUNS
-- ================================
INSERT INTO album (id, nome_album, ano_lancamento)
VALUES
-- Serj Tankian
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Harakiri', '2012'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab', 'Black Blooms', '2012'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac', 'The Rough Dog', '2012'),

-- Mike Shinoda
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'The Rising Tied', '2005'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbc', 'Post Traumatic', '2018'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbd', 'Post Traumatic EP', '2018'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbe', 'Where’d You Go', '2006'),

-- Michel Teló
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Bem Sertanejo', '2014'),
('cccccccc-cccc-cccc-cccc-cccccccccccd', 'Bem Sertanejo - O Show (Ao Vivo)', '2014'),
('cccccccc-cccc-cccc-cccc-ccccccccccce', 'Bem Sertanejo - (1a Temporada) - EP', '2015'),

-- Guns N' Roses
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Use Your Illusion I', '1991'),
('dddddddd-dddd-dddd-dddd-ddddddddddde', 'Use Your Illusion II', '1991'),
('dddddddd-dddd-dddd-dddd-dddddddddddf', 'Greatest Hits', '2004');

-- ================================
-- RELAÇÃO ARTISTA ↔ ALBUM
-- ================================
INSERT INTO artista_album (album_id, artista_id)
VALUES
-- Serj Tankian
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab', '11111111-1111-1111-1111-111111111111'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac', '11111111-1111-1111-1111-111111111111'),

-- Mike Shinoda
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbc', '22222222-2222-2222-2222-222222222222'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbd', '22222222-2222-2222-2222-222222222222'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbe', '22222222-2222-2222-2222-222222222222'),

-- Michel Teló
('cccccccc-cccc-cccc-cccc-cccccccccccc', '33333333-3333-3333-3333-333333333333'),
('cccccccc-cccc-cccc-cccc-cccccccccccd', '33333333-3333-3333-3333-333333333333'),
('cccccccc-cccc-cccc-cccc-ccccccccccce', '33333333-3333-3333-3333-333333333333'),

-- Guns N' Roses
('dddddddd-dddd-dddd-dddd-dddddddddddd', '44444444-4444-4444-4444-444444444444'),
('dddddddd-dddd-dddd-dddd-ddddddddddde', '44444444-4444-4444-4444-444444444444'),
('dddddddd-dddd-dddd-dddd-dddddddddddf', '44444444-4444-4444-4444-444444444444');
