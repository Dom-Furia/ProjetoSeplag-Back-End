# Projeto Back-End com Docker (Java + Spring-Boot + PostgreSQL + MinIO-S3)

**Projeto Prático - Processo Seletivo SEPLAG/MT 2026 (PSS 001/2026)**


---
## 📋 Identificação
- **Candidato:** Julio Cesar de Souza
- **Inscrição:** (informar no ato da inscrição no SIES)
- **Vaga:** Analista de TI - Engenheiro de Computação Back-End Sênior
- **Cargo:** Analista de Tecnologia da Computação
- **Email:** juliocesar.st.2013@gmail.com
- **GitHub:** https://github.com/Dom-Furia

## 🎯 Visão Geral do Projeto

Este projeto utiliza Docker e Docker Compose para  um ambiente completo com:

- **Backend**: Java com Spring-Boot
- **Banco de Dados**: PostgreSQL
-  **Objetc Storage**: MinIO-S3


## 🚀 Como buildar e executar a aplicação

1. **Clone o repositório:**

```bash
git clone https://github.com/Dom-Furia/ProjetoSeplag-Back-End.git
```

2. **Crie o arquivo .env na raiz do projeto**

3. **Crie o arquivo .env com suas credenciais e configurações:**
```bash 
# ===============================
# DATABASE - POSTGRES
# ===============================
DB_HOST=
DB_PORT=
DB_NAME=
DB_USER=
DB_PASSWORD=

# ===============================
# MINIO (S3)
# ===============================

MINIO_ENDPOINT=
MINIO_ROOT_USER=
MINIO_ROOT_PASSWORD=
MINIO_BUCKET=
MINIO_REGION=us-east-1

TOKEN_SECRET=
```
4. **Faça instalação do docker em sua maquina:**

```bash
link: https://www.youtube.com/watch?v=XbXfWAze-I8
link: https://www.docker.com/
```

5. **Buildar e subir a aplicação com Docker Compose:**

```bash
docker-compose up -d

```
Esse comando irá:
- **Buildar a imagem do Back-end**
- **Criar os containers**
- **Subir o banco de dados (PostgreSQL)**
- **Subir o armazenamento de objetos (MinIO-S3)**


🌐 URLs de acesso

Após os containers estarem rodando, acesse:

| Serviço | URL | Credenciais |
|---------|-----|-------------|
| **Backend API** | http://localhost:8080 | - |
| **Documentação API-Swagger** | http://localhost:8080/swagger-ui.html | - |
| **MinIO Console** | http://localhost:9001 | admin / admin123 |
| **PostgreSQL** | localhost:5432 | Acesso via Pgadmin4 |

### Verificação de Saúde
```bash
# Backend
curl http://localhost:8080/actuator/health

# Frontend
curl http://localhost:3000

# MinIO
curl http://localhost:9000/minio/health/live
```

---

## 📊 Modelo de Dados

### Entidades Principais
```sql
artistas (Artistas)
├─ id: UUID PRIMARY KEY
├─ nome: VARCHAR(200) NOT NULL
├─ nacionalidade: TEXT
├─ tipo: 
├─ created_at: TIMESTAMP
└─ updated_at: TIMESTAMP


albuns (Álbuns)
├─ id: UUID PRIMARY KEY
├─ titulo: VARCHAR(200) NOT NULL
├─ ano_lancamento: INTEGER
├─ descricao: TEXT
├─ created_at: TIMESTAMP
└─ updated_at: TIMESTAMP


artista_album (Relacionamento N:N)
├─ artista_id: BIGINT FK → artistas(id)
├─ album_id: BIGINT FK → albuns(id)
└─ created_at: TIMESTAMP
    PRIMARY KEY (artista_id, album_id)

imagens_capa (Capas dos Álbuns)
├─ id: UUID PRIMARY KEY
├─ album_id: UUID FK → albuns(id)
├─ url: VARCHAR(255) NOT NULL
└─ uploaded_at: TIMESTAMP

usuarios (Usuários)
├─ id: UUID PRIMARY KEY
├─ nome: VARCHAR(50) UNIQUE NOT NULL
├─ email: VARCHAR(200) UNIQUE
├─ password: VARCHAR(255) NOT NULL (BCrypt)
└─ created_at: TIMESTAMP

regionais (Sincronização Externa)
├─ id: BIGSERIAL PRIMARY KEY
├─ codigo_externo: INTEGER UNIQUE NOT NULL
├─ nome: VARCHAR(200) NOT NULL
├─ ativa: BOOLEAN
├─ external_hash: VARCHAR(64) (MD5 para detectar mudanças)
├─ ultima_sincronizacao: TIMESTAMP
├─ created_at: TIMESTAMP
└─ updated_at: TIMESTAMP
```
### Relacionamentos
- `artista_album.artista_id → artistas(id)` (ON DELETE CASCADE)
- `artista_album.album_id → albuns(id)` (ON DELETE CASCADE)
- `imagens_capa.album_id → albuns(id)` (ON DELETE CASCADE)

---
## ✅ Checklist de Requisitos

### Backend

#### CRUD, JWT e MinIO 
- [x] **CRUD completo** para Artistas e Álbuns
- [x] **Relacionamento N:N** (artista_album)
- [x] **JWT** com expiração de 5 minutos
- [x] **Renovação** automática de token
- [x] **Upload** de múltiplas imagens para MinIO
- [x] **Presigned URLs** com expiração de 30 minutos
- [x] **CORS** configurado para frontend

#### Paginação e Filtros
- [x] **Paginação** em listagem de artistas
- [x] **Paginação** em listagem de álbuns
- [x] **Filtro por nome** com ordenação ASC/DESC
- [x] **Parâmetros** customizáveis (page, size, sort)

#### Rate Limit e Sincronização
- [x] **Rate limiting** 10 req/min por usuário (Bucket4j)
- [x] **Sincronização** de regionais **O(n)**
- [x] **Detecção de mudanças** via hash MD5
- [x] **Inativação** de registros removidos (SCD Type 2)

#### Swagger, Migrations e Health Check (0-3)
- [x] **OpenAPI/Swagger** com documentação completa
- [x] **Flyway migrations** versionadas
- [x] **Seed data** com artistas do edital
- [x] **Health checks** (liveness/readiness)
- [x] **Actuator** endpoints expostos

#### WebSocket e Notificações 
- [x] **WebSocket** configurado (STOMP + SockJS)
- [x] **Notificações** ao criar álbum
- [x] **Broadcasting** para todos os clientes
- [x] **Integração** com frontend

---
                                     







