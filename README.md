# Projeto Back-End com Docker (Java + Spring-Boot + Postgres + MinIO-S3)

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

3. **Edite o arquivo .env com suas credenciais e configurações:**
```bash 
DB_HOST=db
DB_PORT=3306
DB_DATABASE=meubanco
DB_USERNAME=usuario
DB_PASSWORD=senha
MYSQL_ROOT_PASSWORD=rootpass
```
4. **Faça instalação do docker em sua maquina:**

```bash
link: https://www.youtube.com/watch?v=XbXfWAze-I8
link: https://www.docker.com/
```

5. **Buildar e subir a aplicação com Docker Compose:**

```bash
docker-compose up --build

```
Esse comando irá:
- **Buildar as imagens do backend e frontend**
- **Criar os containers**
- **Subir o banco de dados com volume persistente**


🌐 URLs de acesso

Após os containers estarem rodando, acesse:
                                     
**Backend API:** http://localhost:8080            
**PostgreSQL:** localhost:3306 (acesso via cliente)







