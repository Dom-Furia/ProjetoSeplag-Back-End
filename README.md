# Projeto Back-End com Docker (Java + Spring-Boot + PostgreSQL + MinIO-S3)

Este projeto utiliza Docker e Docker Compose para  um ambiente completo com:

- **Backend**: Java com Spring-Boot
- **Banco de Dados**: PostgreSQL
-  **Objetc Storage**: MinIO-S3

---

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
                                     
**Backend API:** http://localhost:8080
**MinIO-S3:** http://localhost:9090  
**PostgreSQL:** postgres:5432 (acesso via Pgadmin4)
**Documentação API-Swagger:** http://localhost:8080/swagger-ui/index.html







