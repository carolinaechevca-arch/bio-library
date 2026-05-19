# Bio Library

Sistema de gestión de biblioteca universitaria basado en microservicios con arquitectura hexagonal.

## Servicios

| Servicio | Puerto | Descripción |
|---|---|---|
| api-gateway | 8090 | Punto de entrada único — enruta y valida JWT |
| user | 8080 | Autenticación y gestión de estudiantes |
| university-mock | 8081 | Mock del sistema universitario |
| catalog | 8082 | Catálogo de libros (MongoDB) |
| loans | 8083 | Gestión de préstamos |
| notification | 8084 | Notificaciones SMS vía Twilio (consume RabbitMQ) |

**Infraestructura incluida:** PostgreSQL · MongoDB · RabbitMQ · Prometheus · Grafana · Jaeger

---

## Requisitos

- Docker >= 24
- Docker Compose >= 2.20

---

## Configuración inicial

Copia el archivo de entorno y completa los valores:

```bash
cp .env.example .env
```

Variables obligatorias a configurar en `.env`:

| Variable | Descripción |
|---|---|
| `POSTGRES_PASSWORD` | Contraseña de PostgreSQL |
| `RABBITMQ_PASS` | Contraseña de RabbitMQ |
| `JWT_SECRET` | Clave HMAC para firmar tokens |
| `TWILIO_ACCOUNT_SID` | Account SID de Twilio (para SMS) |
| `TWILIO_AUTH_TOKEN` | Auth Token de Twilio |
| `TWILIO_PHONE_NUMBER` | Número Twilio en formato E.164 (`+1234567890`) |

> Si no tienes credenciales de Twilio, las notificaciones SMS se omiten pero el sistema funciona correctamente.

---

## Comandos

### Levantar todo (primera vez o tras cambios de código)

```bash
docker compose up --build
```

### Levantar en segundo plano

```bash
docker compose up --build -d
```

### Levantar solo la infraestructura (BD + RabbitMQ)

```bash
docker compose up postgres mongodb rabbitmq -d
```

### Ver logs de un servicio

```bash
docker compose logs -f api-gateway
docker compose logs -f loans
```

### Ver logs de todos los servicios

```bash
docker compose logs -f
```

### Apagar todo (conserva volúmenes)

```bash
docker compose down
```

### Apagar y borrar volúmenes (reset total de BD)

```bash
docker compose down -v
```

### Reconstruir un solo servicio

```bash
docker compose build loans
docker compose up -d loans
```

---

## URLs una vez levantado

### Aplicación

| Recurso | URL |
|---|---|
| API Gateway (entrada principal) | http://localhost:8090 |
| RabbitMQ Management UI | http://localhost:15672 (guest/guest) |
| Swagger user | http://localhost:8080/swagger-ui.html |
| Swagger catalog | http://localhost:8082/swagger-ui.html |
| Swagger loans | http://localhost:8083/swagger-ui.html |
| Swagger university-mock | http://localhost:8081/swagger-ui.html |
| Health gateway | http://localhost:8090/actuator/health |

### Observabilidad

| Recurso | URL | Credenciales |
|---|---|---|
| Prometheus | http://localhost:9090 | — |
| Grafana | http://localhost:3001 | admin / admin |
| Jaeger (trazas) | http://localhost:16686 | — |

---

## Flujo de prueba

1. **Registrar estudiante** (requiere token ADMIN)
   ```
   POST http://localhost:8090/api/v1/students/create
   ```
   Body (nombre y apellido se toman automáticamente del sistema universitario):
   ```json
   {
     "carnet": "2021-0001",
     "dni": "123456789",
     "email": "estudiante@itm.edu.co",
     "password": "Password1!",
     "phoneNumber": "+573001234567",
     "university": "ITM"
   }
   ```

2. **Login — obtener JWT**
   ```
   POST http://localhost:8090/api/v1/auth/login
   ```

3. **Usar el JWT en el header para las demás rutas**
   ```
   Authorization: Bearer <token>
   ```

4. **Ver libros disponibles**
   ```
   GET http://localhost:8090/api/v1/books
   ```

5. **Crear préstamo**
   ```
   POST http://localhost:8090/api/v1/loans
   ```
   > Se bloquea si el estudiante tiene sanción activa (403) o GPA < 3.2 con préstamo activo (422).

---

## Arquitectura

```
Cliente
  │
  ▼
api-gateway :8090  (JWT auth + routing + CORS)
  ├──▶ user :8080  ──▶ university-mock :8081
  ├──▶ catalog :8082  (MongoDB)
  └──▶ loans :8083
         ├──▶ catalog :8082  (Feign — licencias)
         ├──▶ user :8080     (Feign — email/teléfono/sanción)
         └──▶ RabbitMQ :5672 ──▶ notification :8084 ──▶ Twilio SMS

Infraestructura:
  PostgreSQL :5432  (schemas: users · loans · university)
  MongoDB    :27017 (db: biolibrary)
  RabbitMQ   :5672  / UI :15672

Observabilidad:
  Prometheus :9090  (scrape de todos los servicios)
  Grafana    :3001  (dashboards — datasource Prometheus pre-configurado)
  Jaeger     :16686 (trazas distribuidas OTLP)
```

---

## Seed de MongoDB

Al primer arranque, MongoDB carga automáticamente `mongo-init/01-seed.sh` → `seed.js` con 18 libros de ejemplo.

Para verificar:
```bash
docker exec bio-mongodb mongosh biolibrary --eval "db.books.find().pretty()"
```

Para forzar un re-seed (borra todos los datos de Mongo):
```bash
docker compose down
docker volume rm bio-library_mongo_data
docker compose up --build
```

---

## Observabilidad

### Grafana

Abre http://localhost:3001 (admin/admin). El datasource de Prometheus ya esta pre-configurado.

**Importar dashboard de JVM (metricas de cada servicio):**
- Dashboards → New → Import
- Ingresa el ID `4701` → Load
- En "Prometheus" selecciona el datasource → Import

**Importar dashboard de Spring Boot:**
- Mismo proceso con ID `12685`

### Prometheus

Abre http://localhost:9090 → **Status → Targets** para ver el estado de scraping de los 6 servicios.

**Metricas utiles:**
```
http_server_requests_seconds_count
http_server_requests_seconds_sum
jvm_memory_used_bytes
jvm_threads_live_threads
process_cpu_usage
```

### Jaeger

Abre http://localhost:16686 para ver trazas distribuidas entre servicios.

1. En "Service" selecciona el servicio (ej: `loans`)
2. Clic en "Find Traces"
3. Clic en una traza para ver el flujo completo entre microservicios

### RabbitMQ

Abre http://localhost:15672. Credenciales definidas en `.env`: `RABBITMQ_USER` / `RABBITMQ_PASS` (por defecto `guest` / `guest`).

- **Queues:** ver mensajes pendientes y procesados
- **Exchanges:** ver el exchange configurado para notificaciones
- **Connections:** ver que servicios estan conectados

### Twilio

Abre https://console.twilio.com con las credenciales de la cuenta configuradas en `.env`.

- **Monitor → Logs → Messaging:** ver SMS enviados y su estado de entrega
- **Monitor → Errors:** ver errores de envio si los hay
