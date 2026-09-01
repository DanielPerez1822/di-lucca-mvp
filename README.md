# Di Lucca MVP

Di Lucca se ejecuta como tres servicios Docker: el frontend Angular servido por
Nginx, la API Spring Boot y PostgreSQL.

## Requisitos

- Docker Desktop (incluye Docker Compose).

## Inicio rápido

1. Copia el archivo de variables y completa los secretos locales:

   ```powershell
   Copy-Item .env.example .env
   ```

2. Inicia todo el sistema:

   ```powershell
   docker compose up --build
   ```

Abre `http://localhost:8080`. La API queda disponible en
`http://localhost:9000` y Swagger en `http://localhost:9000/swagger-ui.html`.

## Servicios y comunicación

| Servicio | Puerto host | Función |
| --- | --- | --- |
| `frontend` | `8080` | Angular 20 compilado y servido por Nginx |
| `backend` | `9000` | API Spring Boot 3.4.5 / Java 21 |
| `postgres` | No expuesto | PostgreSQL 16, accesible internamente como `postgres:5432` |

El navegador pide `/api/...` al mismo origen. Nginx lo reenvía por la red
interna `dilucca-network` a `backend:9000`, que se conecta a
`postgres:5432`. Así no se exponen ni la base de datos ni nombres internos al
navegador. Para desarrollo sin Docker, `npm start` usa `proxy.conf.json` y
mantiene la conexión a la API local en `localhost:9000`.

PostgreSQL guarda sus datos en el volumen nombrado `postgres_data`. El backend
conserva el esquema y los datos entre reinicios (`JPA_DDL_AUTO=update`), y los
scripts existentes `schema.sql` y `data.sql` continúan inicializando datos de
referencia de manera idempotente.

## Variables

`.env` no se versiona. Consulta `.env.example` para estas variables:

- `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`
- `JWT_SECRET` (mínimo 32 bytes)
- `FRONTEND_PORT`, `BACKEND_PORT`, `CORS_ORIGINS`
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD` (opcionales)

## Operación

```powershell
# Detener servicios conservando los datos
docker compose down

# Eliminar servicios y también los datos persistidos
docker compose down -v
```
