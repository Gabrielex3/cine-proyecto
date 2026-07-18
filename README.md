# 🎬 Sistema de Gestión de Cines - Arquitectura de Microservicios

## 📋 Tabla de Contenidos
- [Descripción General](#descripción-general)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Servicios Disponibles](#servicios-disponibles)
- [Conexiones OpenFeign](#conexiones-openfeign)
- [API Gateway](#api-gateway)
- [Eureka Service Discovery](#eureka-service-discovery)
- [API Docs (Swagger UI)](#api-docs-swagger-ui)
- [HATEOAS](#hateoas)
- [Tecnologías y Métodos HTTP](#tecnologías-y-métodos-http)
- [Endpoints por Servicio](#endpoints-por-servicio)
- [Flujo de Peticiones](#flujo-de-peticiones)
- [Tests Unitarios](#tests-unitarios)
- [Instalación Local](#instalación-local)
- [Ejecución con Docker Compose](#ejecución-con-docker-compose)

---

## 📝 Descripción General

Este proyecto es un **sistema integral de gestión de cines** construido con una arquitectura de **microservicios en Spring Boot**. Permite la gestión completa de operaciones de cines, incluyendo:

- ✅ Gestión de usuarios y autenticación
- ✅ Catálogo de películas
- ✅ Administración de salas y asientos
- ✅ Horarios de funciones
- ✅ Sistema de reservas de asientos
- ✅ Emisión de entradas/tickets
- ✅ Procesamiento de pagos
- ✅ Sistema de notificaciones
- ✅ Administración de sucursales de cine

Cada servicio es **independiente y escalable**, comunicándose a través de **OpenFeign** para llamadas síncronas entre microservicios, con **Eureka** como service registry y un **API Gateway** como punto de entrada único.

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                    SISTEMA DE CINES (Microservicios)            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│              ┌──────────────────────────────┐                   │
│              │   API Gateway  (9000)         │                   │
│              └──────────────┬───────────────┘                   │
│                             │                                    │
│              ┌──────────────▼───────────────┐                   │
│              │   Eureka Server  (8761)       │                   │
│              └──────────────────────────────┘                   │
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │   Users      │  │   Movie      │  │   Cinema     │           │
│  │  (8080)      │  │   (8081)     │  │  (8087)      │           │
│  └──────────────┘  └──────────────┘  └──────────────┘           │
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │   Room       │  │  Seat        │  │  Showtime    │           │
│  │  (8086)      │  │   (8083)     │  │  (8082)      │           │
│  └──────────────┘  └──────────────┘  └──────────────┘           │
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │   Booking    │  │   Ticket     │  │   Payment    │           │
│  │  (8085)      │  │  (8088)      │  │  (8084)      │           │
│  └──────────────┘  └──────────────┘  └──────────────┘           │
│                                                                  │
│  ┌──────────────────────────────────────────────────────┐       │
│  │        Notification (8089)                           │       │
│  └──────────────────────────────────────────────────────┘       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Servicios Disponibles

| # | Servicio | Puerto | BD | Descripción |
|---|----------|--------|-----|-------------|
| 1 | **User Service** | 8080 | MySQL (`cine_users_db`) | Gestión de usuarios, registro y autenticación |
| 2 | **Movie Service** | 8081 | MySQL (`cine_movies_db`) | Catálogo y gestión de películas |
| 3 | **Showtime Service** | 8082 | MySQL (`cine_showtimes_db`) | Horarios de funciones (películas en salas) |
| 4 | **Seat Service** | 8083 | MySQL (`cine_seats_db`) | Gestión de asientos en salas |
| 5 | **Payment Service** | 8084 | MySQL (`cine_payments_db`) | Procesamiento de pagos y transacciones |
| 6 | **Booking Service** | 8085 | MySQL (`cine_bookings_db`) | Gestión de reservas de entradas |
| 7 | **Room Service** | 8086 | MySQL (`cine_rooms_db`) | Administración de salas de cine |
| 8 | **Cinema Service** | 8087 | MySQL (`cine_cines_db` + `cine_comuna_db`) | Gestión de sucursales de cine y comunas |
| 9 | **Ticket Service** | 8088 | MySQL (`cine_tickets_db`) | Emisión y gestión de tickets/entradas |
| 10 | **Notification Service** | 8089 | MySQL (`cine_notifications_db`) | Envío de notificaciones a usuarios |
| 11 | **Eureka Server** | 8761 | — | Service registry y discovery |
| 12 | **API Gateway** | 9000 | — | Punto de entrada único (enruta `/api/v2/...`) |

---

## 🔗 Conexiones OpenFeign

### Mapa de Comunicación entre Microservicios

```
┌─── SHOWTIME SERVICE (8082) ───┐
│  movieClient ─────────────→ MOVIE SERVICE (8081)
│  roomClient ──────────────→ ROOM SERVICE (8086)
└────────────────────────────────┘

┌─── SEAT SERVICE (8083) ────┐
│  SalaClient ─────────────→ ROOM SERVICE (8086)
└─────────────────────────────┘

┌─── BOOKING SERVICE (8085) ────┐
│  userClient ──────────────→ USER SERVICE (8080)
│  showtimeClient ──────────→ SHOWTIME SERVICE (8082)
│  seatClient ──────────────→ SEAT SERVICE (8083)
│  cinemaClient ────────────→ CINEMA SERVICE (8087)
└────────────────────────────────┘

┌─── TICKET SERVICE (8088) ────┐
│  BookingClient ───────────→ BOOKING SERVICE (8085)
│  PaymentClient ───────────→ PAYMENT SERVICE (8084)
└────────────────────────────────┘

┌─── PAYMENT SERVICE (8084) ────┐
│  BookingClient ───────────→ BOOKING SERVICE (8085)
│  TicketClient ────────────→ TICKET SERVICE (8088)
│  showtimeClient ──────────→ SHOWTIME SERVICE (8082)
│  notificationClient ──────→ NOTIFICATION SERVICE (8089)
└────────────────────────────────┘

┌─── NOTIFICATION SERVICE (8089) ────┐
│  PaymentClient ────────────────→ PAYMENT SERVICE (8084)
└──────────────────────────────────────┘
```

### Descripción de Clientes OpenFeign

| Cliente | Servicio Origen | Servicio Destino | Endpoint | Descripción |
|---------|-----------------|------------------|----------|-------------|
| `movieClient` | Showtime (8082) | Movie (8081) | `GET /api/v1/cine/movie/{id}` | Obtiene información de película por ID |
| `roomClient` | Showtime (8082) | Room (8086) | `GET /api/v1/cine/room/{id}` | Obtiene información de sala por ID |
| `userClient` | Booking (8085) | User (8080) | `GET /api/v1/cine/users/{id}` | Obtiene datos del usuario por ID |
| `showtimeClient` | Booking (8085) | Showtime (8082) | `GET /api/v1/cine/showtime/{id}` | Obtiene función/horario por ID |
| `seatClient` | Booking (8085) | Seat (8083) | `GET /api/v1/cine/asiento/{id}` | Obtiene estado del asiento |
| `cinemaClient` | Booking (8085) | Cinema (8087) | `GET /api/v1/cine/cinemas/{id}` | Obtiene información de sucursal |
| `BookingClient` | Ticket (8088) | Booking (8085) | `GET /api/v1/cine/bookings/{id}` | Obtiene información de reserva |
| `PaymentClient` | Ticket (8088) | Payment (8084) | `GET /api/v1/cine/payment/{id}` | Obtiene información de pago |
| `BookingClient` | Payment (8084) | Booking (8085) | `GET/PUT /api/v1/cine/bookings/{id}` | Obtiene/actualiza reserva |
| `TicketClient` | Payment (8084) | Ticket (8088) | `POST /api/v1/cine/tickets` | Crea ticket tras pago aprobado |
| `showtimeClient` | Payment (8084) | Showtime (8082) | `GET /api/v1/cine/showtime/{id}` | Obtiene función por ID |
| `notificationClient` | Payment (8084) | Notification (8089) | `POST /api/v1/cine/notification/send` | Envía notificación |
| `PaymentClient` | Notification (8089) | Payment (8084) | `GET /api/v1/cine/payment/{id}` | Obtiene información de pago |
| `SalaClient` | Seat (8083) | Room (8086) | `GET /api/v1/cine/room/{id}` | Obtiene información de sala |

---

## 🌐 API Gateway

El **API Gateway** corre en el puerto `9000` y actúa como punto de entrada único para todos los microservicios. Las rutas `/api/v2/...` son enrutadas mediante **Spring Cloud Gateway** con balanceo de carga a través de Eureka (`lb://`).

### Rutas configuradas

| Ruta Gateway | Enrutado a | Servicio |
|---|---|---|
| `GET http://localhost:9000/api/v2/cine/users/**` | `lb://USER-SERVICE` | User Service |
| `GET http://localhost:9000/api/v2/cine/cinemas/**` | `lb://CINEMA-SERVICE` | Cinema Service |
| `GET http://localhost:9000/api/v2/cine/comunas/**` | `lb://CINEMA-SERVICE` | Cinema Service (Comunas) |
| `GET http://localhost:9000/api/v2/cine/movie/**` | `lb://MOVIE-SERVICE` | Movie Service |
| `GET http://localhost:9000/api/v2/cine/tickets/**` | `lb://TICKET-SERVICE` | Ticket Service |
| `GET http://localhost:9000/api/v2/cine/showtime/**` | `lb://SHOWTIME-SERVICE` | Showtime Service |
| `GET http://localhost:9000/api/v2/cine/asiento/**` | `lb://SEAT-SERVICE` | Seat Service |
| `GET http://localhost:9000/api/v2/cine/room/**` | `lb://ROOM-SERVICES` | Room Service |
| `GET http://localhost:9000/api/v2/cine/payment/**` | `lb://PAYMENT-SERVICE` | Payment Service |
| `GET http://localhost:9000/api/v2/cine/notification/**` | `lb://NOTIFICATION-SERVICE` | Notification Service |
| `GET http://localhost:9000/api/v2/cine/bookings/**` | `lb://BOOKING-SERVICE` | Booking Service |

> **Nota:** Las rutas directas a cada servicio siguen disponibles en `/api/v1/...` usando el puerto individual de cada microservicio.

---

## 🗺️ Eureka Service Discovery

El **Eureka Server** corre en el puerto `8761` y permite el descubrimiento automático entre microservicios, eliminando la necesidad de URLs hardcodeadas en producción.

Panel de administración: `http://localhost:8761`

Todos los microservicios se registran automáticamente al iniciar. Cada instancia se identifica como `{nombre-servicio}:{puerto}`.

---

## 📄 API Docs (Swagger UI)

Todos los microservicios tienen integrado **springdoc-openapi** (`springdoc-openapi-starter-webmvc-ui`), que genera automáticamente la documentación interactiva de la API.

### Acceso a Swagger UI por servicio

| Servicio | Swagger UI | JSON OpenAPI |
|----------|-----------|--------------|
| User Service | `http://localhost:8080/doc/swagger-ui.html` | `http://localhost:8080/api-docs` |
| Movie Service | `http://localhost:8081/doc/swagger-ui.html` | `http://localhost:8081/api-docs` |
| Showtime Service | `http://localhost:8082/doc/swagger-ui.html` | `http://localhost:8082/api-docs` |
| Seat Service | `http://localhost:8083/doc/swagger-ui.html` | `http://localhost:8083/api-docs` |
| Payment Service | `http://localhost:8084/doc/swagger-ui.html` | `http://localhost:8084/api-docs` |
| Booking Service | `http://localhost:8085/doc/swagger-ui.html` | `http://localhost:8085/api-docs` |
| Room Service | `http://localhost:8086/doc/swagger-ui.html` | `http://localhost:8086/api-docs` |
| Cinema Service | `http://localhost:8087/doc/swagger-ui.html` | `http://localhost:8087/api-docs` |
| Ticket Service | `http://localhost:8088/doc/swagger-ui.html` | `http://localhost:8088/api-docs` |
| Notification Service | `http://localhost:8089/doc/swagger-ui.html` | `http://localhost:8089/api-docs` |

### Configuración springdoc (application.yml)

Cada servicio incluye esta configuración:

```yaml
springdoc:
  api-docs:
    enabled: true
    path: /api-docs
  swagger-ui:
    enabled: true
    path: /doc/swagger-ui.html
  cache:
    disabled: true
```

### Anotaciones Swagger en controladores

Los controladores documentan sus endpoints con anotaciones de OpenAPI 3:

```java
@Tag(name = "USUARIOS", description = "API RELACIONADA A LA CREACION DE USUARIOS")
@RestController
@RequestMapping("/api/v1/cine/users")
public class userController {

    @Operation(summary = "METODO GET FIND ALL USERS", description = "Lista todos los usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = User.class)))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
            content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() { ... }
}
```

---

## 🔗 HATEOAS

Los servicios **User** y **Cinema** implementan **Spring HATEOAS** (nivel REST maduro), exponiendo controladores V2 que devuelven respuestas enriquecidas con hipervínculos a acciones relacionadas. Estos controladores son accesibles a través del API Gateway en las rutas `/api/v2/...`.

### Servicios con HATEOAS

#### User Service — `userControllerV2` (`/api/v2/cine/users`)

Utiliza `UserModelAssembler` para añadir links a cada entidad:

```json
GET http://localhost:8080/api/v2/cine/users/1

{
  "id": 1,
  "rut": "12345678-9",
  "nombre": "Juan",
  "apellido": "Pérez",
  "correo": "juan@example.com",
  "telefono": "+56912345678",
  "_links": {
    "findUser": {
      "href": "http://localhost:8080/api/v2/cine/users/1"
    },
    "updateUser": {
      "href": "http://localhost:8080/api/v2/cine/users/1"
    },
    "deleteUser": {
      "href": "http://localhost:8080/api/v2/cine/users/1"
    },
    "findAllUsers": {
      "href": "http://localhost:8080/api/v2/cine/users"
    }
  }
}
```

Colección (`GET /api/v2/cine/users`):

```json
{
  "_embedded": {
    "userList": [ { ... } ]
  },
  "_links": {
    "self": { "href": "http://localhost:8080/api/v2/cine/users" }
  }
}
```

#### Cinema Service — `cinemaControllerV2` y `ComunasControllerV2`

Rutas disponibles:
- `GET http://localhost:8087/api/v2/cine/cinemas` — lista todos los cines con links
- `GET http://localhost:8087/api/v2/cine/cinemas/{id}` — cine por ID con links
- `POST http://localhost:8087/api/v2/cine/cinemas` — crea cine, responde `201 Created` con `Location` header
- `GET http://localhost:8087/api/v2/cine/comunas` — lista todas las comunas con links
- `GET http://localhost:8087/api/v2/cine/comunas/{id}` — comuna por ID con links

Ejemplo de respuesta de cinema con HATEOAS:

```json
GET http://localhost:8087/api/v2/cine/cinemas/1

{
  "id": 1,
  "cine": "Cinemark Las Condes",
  "direccion": "Av. Las Condes 13000",
  "_links": {
    "findCinemaById": {
      "href": "http://localhost:8087/api/v2/cine/cinemas/1"
    },
    "updateCinema": {
      "href": "http://localhost:8087/api/v2/cine/cinemas/1"
    },
    "deleteCinema": {
      "href": "http://localhost:8087/api/v2/cine/cinemas/1"
    },
    "cinemas": {
      "href": "http://localhost:8087/api/v2/cine/cinemas"
    }
  }
}
```

### Configuración HATEOAS en application.yml

```yaml
springdoc:
  hateoas:
    enabled: false   # Desactiva la integración automática springdoc-hateoas
                     # para que Swagger muestre el modelo plano, no EntityModel<T>
```

> Esto es intencional: se desactiva la integración automática springdoc+hateoas para que Swagger UI muestre los modelos de datos correctamente, mientras que los endpoints V2 sí devuelven links HATEOAS en runtime.

---

## 🛠️ Tecnologías y Métodos HTTP

### Stack Tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Spring Boot | 3.x | Framework base |
| Java | 17+ | Lenguaje |
| Spring Cloud Gateway | — | API Gateway |
| Spring Cloud Netflix Eureka | — | Service Discovery |
| OpenFeign | — | Comunicación inter-servicios |
| Spring HATEOAS | — | Nivel REST maduro (User, Cinema) |
| springdoc-openapi | — | Documentación Swagger UI |
| MySQL | 8.0+ | Base de datos por servicio |
| Flyway | — | Migraciones de base de datos |
| Maven | 3.6+ | Gestión de dependencias |
| Jakarta Validation | — | Validación de DTOs |
| SLF4J + Logback | — | Logging |
| Docker / Docker Compose | — | Contenedores y despliegue |

### Métodos HTTP Utilizados

| Método | Descripción | Uso |
|--------|-------------|-----|
| **GET** | Recupera información | Obtener datos existentes |
| **POST** | Crea nuevo recurso | Crear entidades (usuarios, películas, etc.) |
| **PUT** | Actualiza recurso existente | Modificar datos existentes |
| **DELETE** | Elimina recurso | Borrar entidades |

### Estructura de Respuestas

```
✅ 200 OK          - Solicitud exitosa (GET, PUT, DELETE)
✅ 201 CREATED     - Recurso creado exitosamente (POST)
❌ 400 BAD REQUEST - Datos inválidos
❌ 404 NOT FOUND   - Recurso no encontrado
❌ 500 INTERNAL SERVER ERROR - Error del servidor
```

---

## 📡 Endpoints por Servicio

### 1️⃣ USER SERVICE (Puerto 8080)

**Base URL v1**: `http://localhost:8080/api/v1/cine/users`
**Base URL v2 (HATEOAS)**: `http://localhost:8080/api/v2/cine/users`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar todos los usuarios |
| GET | `/{id}` | Obtener usuario por ID |
| POST | `/` | Crear nuevo usuario |
| PUT | `/{id}` | Actualizar usuario |
| DELETE | `/{id}` | Eliminar usuario |

**Ejemplo de Request (POST)**:
```json
{
  "rut": "12345678-9",
  "nombre": "Juan",
  "apellido": "Pérez",
  "correo": "juan@example.com",
  "telefono": "+56912345678"
}
```

---

### 2️⃣ MOVIE SERVICE (Puerto 8081)

**Base URL**: `http://localhost:8081/api/v1/cine/movie`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar todas las películas |
| GET | `/{id}` | Obtener película por ID |
| POST | `/` | Crear nueva película |
| PUT | `/{id}` | Actualizar película |
| DELETE | `/{id}` | Eliminar película |

**Ejemplo de Request (POST)**:
```json
{
  "titulo": "Avatar 2",
  "genero": "Ciencia Ficción",
  "duracion": 192
}
```

---

### 3️⃣ SHOWTIME SERVICE (Puerto 8082)

**Base URL**: `http://localhost:8082/api/v1/cine/showtime`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar todas las funciones |
| GET | `/{id}` | Obtener función por ID (incluye datos de película y sala) |
| POST | `/` | Crear nueva función |
| PUT | `/{id}` | Actualizar función |
| DELETE | `/{id}` | Eliminar función |

**Ejemplo de Request (POST)**:
```json
{
  "movieId": 1,
  "roomId": 5,
  "fechaFuncion": "2024-06-15T19:00:00",
  "precioTicket": 12000.0
}
```

---

### 4️⃣ SEAT SERVICE (Puerto 8083)

**Base URL**: `http://localhost:8083/api/v1/cine/asiento`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar todos los asientos |
| GET | `/{id}` | Obtener asiento por ID |
| GET | `/sala/{salaId}` | Listar asientos de una sala |
| POST | `/` | Crear nuevo asiento |
| PUT | `/{id}/estado` | Actualizar estado disponibilidad |
| DELETE | `/{id}` | Eliminar asiento |

**Ejemplo de Request (POST)**:
```json
{
  "numeroFila": "A1",
  "salaId": 5,
  "disponible": true
}
```

---

### 5️⃣ PAYMENT SERVICE (Puerto 8084)

**Base URL**: `http://localhost:8084/api/v1/cine/payment`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar todos los pagos |
| GET | `/{id}` | Obtener pago por ID |
| POST | `/` | Procesar nuevo pago |
| DELETE | `/{id}` | Cancelar/Eliminar pago |

**Ejemplo de Request (POST)**:
```json
{
  "reservaId": 1,
  "monto": 24000.00,
  "estado": "CREATED"
}
```

**Estados de Pago**: `CREATED`, `APPROVED`, `REJECTED`, `CANCELLED`

---

### 6️⃣ BOOKING SERVICE (Puerto 8085)

**Base URL**: `http://localhost:8085/api/v1/cine/bookings`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar todas las reservas |
| GET | `/{id}` | Obtener reserva por ID |
| POST | `/` | Crear nueva reserva |
| PUT | `/{id}` | Actualizar reserva |
| PUT | `/cambiarestado/{id}` | Cambiar estado de reserva |
| DELETE | `/{id}` | Eliminar reserva |

**Ejemplo de Request (POST)**:
```json
{
  "userId": 1,
  "showtimeId": 5,
  "seatId": 10,
  "status": "CREATED",
  "cinema": 1
}
```

**Estados de Booking**: `CREATED`, `CONFIRMED`, `CANCELLED`

---

### 7️⃣ ROOM SERVICE (Puerto 8086)

**Base URL**: `http://localhost:8086/api/v1/cine/room`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar todas las salas |
| GET | `/{id}` | Obtener sala por ID |
| POST | `/` | Crear nueva sala |
| PUT | `/desactivar/{id}` | Desactivar sala |

**Ejemplo de Request (POST)**:
```json
{
  "nombre": "Sala Premium IMAX",
  "tipo": "IMAX",
  "activo": true
}
```

---

### 8️⃣ CINEMA SERVICE (Puerto 8087)

**Base URL v1**: `http://localhost:8087/api/v1/cine/cinemas`
**Base URL v2 (HATEOAS)**: `http://localhost:8087/api/v2/cine/cinemas`
**Comunas v2 (HATEOAS)**: `http://localhost:8087/api/v2/cine/comunas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/cinemas` | Listar todas las sucursales |
| GET | `/cinemas/{id}` | Obtener sucursal por ID |
| POST | `/cinemas` | Crear nueva sucursal |
| PUT | `/cinemas/{id}` | Actualizar sucursal |
| DELETE | `/cinemas/{id}` | Eliminar sucursal |

**Ejemplo de Request (POST)**:
```json
{
  "cine": "Cinemark Las Condes",
  "direccion": "Av. Las Condes 13000",
  "comunaId": 1
}
```

---

### 9️⃣ TICKET SERVICE (Puerto 8088)

**Base URL**: `http://localhost:8088/api/v1/cine/tickets`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/` | Generar nuevo ticket |
| GET | `/{id}` | Obtener ticket por ID |

**Ejemplo de Request (POST)**:
```json
{
  "bookingId": 1,
  "paymentId": 5,
  "issueDate": "2024-06-10T14:30:00"
}
```

---

### 🔟 NOTIFICATION SERVICE (Puerto 8089)

**Base URL**: `http://localhost:8089/api/v1/cine/notification`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/send` | Enviar notificación |
| GET | `/historial` | Obtener historial de notificaciones |
| GET | `/{id}` | Obtener notificación por ID |
| GET | `/user/{id}` | Obtener notificaciones de usuario |
| PUT | `/{id}` | Actualizar notificación |
| DELETE | `/{id}` | Eliminar notificación |

**Ejemplo de Request (POST)**:
```json
{
  "idTicket": 1,
  "idUsuario": 1,
  "mensaje": "Tu reserva para Avatar 2 ha sido confirmada",
  "tipo": "CONFIRMACION",
  "estado": "SEEN"
}
```

**Estados de Notificación**: `SEEN`, `NO_VIEW`

---

## 📊 Flujo de Peticiones

### Scenario 1: Crear una Reserva de Entrada

```
1. Cliente → POST /api/v1/cine/bookings
   { "userId": 1, "showtimeId": 5, "seatId": 10, "status": "CREATED", "cinema": 1 }

2. BOOKING SERVICE valida y consulta:
   └─→ USER SERVICE      → userClient.obtenerPorIdUser(1)
   └─→ SHOWTIME SERVICE  → showtimeClient.obtenerPorIdShowtime(5)
   └─→ SEAT SERVICE      → seatClient.obtenerAsientoPorId(10)
   └─→ CINEMA SERVICE    → cinemaClient.obtenerCinemaPorId(1)

3. BOOKING SERVICE guarda la reserva → 201 CREATED
   { "id": 1, "userId": 1, "showtimeId": 5, "seatId": 10, "status": "CREATED", "cinema": 1 }
```

### Scenario 2: Procesar Pago y Generar Ticket

```
1. Cliente → POST /api/v1/cine/payment
   { "reservaId": 1, "monto": 24000.00, "estado": "CREATED" }

2. PAYMENT SERVICE consulta:
   └─→ BOOKING SERVICE   → bookingClient.buscarReservaPorId(1)
   └─→ SHOWTIME SERVICE  → showtimeClient.obtenerPorIdShowtime(...)

3. Pago procesado → estado: APPROVED

4. PAYMENT SERVICE actualiza:
   └─→ BOOKING SERVICE   → bookingClient.actualizarStatus(1, CONFIRMED)
   └─→ TICKET SERVICE    → ticketClient.crearTicket(...)
   └─→ NOTIFICATION      → notificationClient.enviarNotificacion(...)

5. Respuesta final: ticket generado + notificación enviada
```

### Scenario 3: Obtener Función con Datos Enriquecidos

```
1. Cliente → GET /api/v1/cine/showtime/5

2. SHOWTIME SERVICE consulta:
   └─→ MOVIE SERVICE → movieClient.obtenerPeliculaPorId(movieId)
   └─→ ROOM SERVICE  → roomClient.buscarPorId(roomId)

3. Respuesta compuesta:
   {
     "id": 5,
     "movieDetails": { "titulo": "Avatar 2", "genero": "Ciencia Ficción", "duracion": 192 },
     "roomDetails":  { "nombre": "Sala Premium IMAX", "tipo": "IMAX", "activo": true },
     "fechaHora": "2024-06-15T19:00:00",
     "precioTicket": 12000.0
   }
```

---

## 🧪 Tests Unitarios

Todos los servicios tienen pruebas unitarias usando **JUnit 5**, **Mockito** y **Spring Boot Test**. Los tests se ejecutan con perfil `local` (`@ActiveProfiles("local")`) para evitar dependencias con bases de datos reales, usando `@MockitoBean` para simular repositorios y clientes OpenFeign.

### Resumen de cobertura por servicio

| Servicio | Clase de Test | Tests implementados |
|----------|---------------|-------------------|
| User | `userServiceTest` | findAll, findById, save, update, deleteById |
| Movie | `movieServiceTest` | findAll, getById, save, update, delete |
| Showtime | `showtimeServiceTest` | findAll, getById, create, update, delete |
| Seat | `seatServiceTest` | findAll, getById, create, update, delete |
| Payment | `paymentServiceTest` | procesarPago, findById, deleteById |
| Booking | `bookingServiceTest` | getAll, findById, createBooking, deleteById, updateBooking, actualizarEstado |
| Room | `salaServiceTest` | findAll, getById, create, update |
| Cinema | `cinemaServiceTest` + `comunasServiceTest` | CRUD cinemas + CRUD comunas |
| Ticket | `ticketServiceTest` | createTicket, findById |
| Notification | `notificationServiceTest` | send, findAll, findById, update, delete |

### Cómo ejecutar los tests

```bash
# Ejecutar tests de un servicio específico (sin necesitar MySQL)
cd booking-service
./mvnw test -Dspring.profiles.active=local

# Ejecutar tests de todos los servicios desde la raíz
# (requiere ejecutar en cada directorio de servicio)
for dir in user-service movie-service showtime-service seat-service \
           payment-service booking-service room-services cinema-service \
           ticket-service notification-service; do
  echo "Testing $dir..."
  cd $dir && ./mvnw test -Dspring.profiles.active=local && cd ..
done
```

### Ejemplo: estructura de test en Booking Service

```java
@SpringBootTest
@ActiveProfiles("local")
public class bookingServiceTest {

    @Autowired
    private bookingService service;

    @MockitoBean
    private bookingRepository repo;

    // Feign clients mockeados — no hacen llamadas HTTP reales
    @MockitoBean private userClient userClient;
    @MockitoBean private showtimeClient showtimeClient;
    @MockitoBean private seatClient seatClient;
    @MockitoBean private cinemaClient cinemaClient;

    @Test
    public void testCreateBooking() {
        bookingDTO dto = new bookingDTO(10L, 20L, 30L, bookingStatus.CREATED, 1L);

        when(userClient.obtenerPorIdUser(10L)).thenReturn(new userDTO());
        when(showtimeClient.obtenerPorIdShowtime(20L)).thenReturn(new showtimeDTO());
        when(seatClient.obtenerAsientoPorId(30L)).thenReturn(new seatDTO());
        when(cinemaClient.obtenerCinemaPorId(1L)).thenReturn(new cinemaDTO());

        booking reservaGuardada = new booking();
        reservaGuardada.setId(100L);
        reservaGuardada.setStatus(bookingStatus.CREATED);
        when(repo.save(any(booking.class))).thenReturn(reservaGuardada);

        booking resultado = service.createBooking(dto);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals(bookingStatus.CREATED, resultado.getStatus());
        verify(repo, times(1)).save(any(booking.class));
    }
}
```

### Ejemplo: test en Payment Service (flujo completo)

```java
@Test
public void testProcesarPago() {
    PaymentRequestDTO dto = new PaymentRequestDTO(10L, new BigDecimal("5000"), PaymentStatus.CREATED);
    bookingDTO reserva = new bookingDTO(1L, 10L, 1L, 20L, "CREATED", 1L);
    showtimeDTO funcion = new showtimeDTO(1L, 1L, LocalDateTime.now(), new BigDecimal("5000"));
    Payment pagoGuardado = new Payment(1L, new BigDecimal("5000"), PaymentStatus.APPROVED, 10L, LocalDateTime.now());

    when(bookingClient.buscarReservaPorId(10L)).thenReturn(reserva);
    when(showtimeClient.obtenerPorIdShowtime(1L)).thenReturn(funcion);
    when(repo.save(any(Payment.class))).thenReturn(pagoGuardado);
    when(ticketClient.crearTicket(any())).thenReturn(new TicketDTO(...));
    doNothing().when(notificationClient).enviarNotificacion(any());

    Payment resultado = service.procesarPago(dto);

    assertEquals(PaymentStatus.APPROVED, resultado.getEstado());
    verify(bookingClient, times(1)).actualizarStatus(anyLong(), any());
    verify(ticketClient, times(1)).crearTicket(any());
    verify(notificationClient, times(1)).enviarNotificacion(any());
}
```

---

## 💻 Instalación Local

### Requisitos Previos

- **Java 17+** (verificar con `java -version`)
- **Maven 3.6+** (verificar con `mvn -version`) — o usar el Maven Wrapper incluido (`./mvnw`)
- **MySQL 8.0+** corriendo localmente en el puerto `3306`
- **Git**

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/Gabrielex3/cine-proyecto.git
cd cine-proyecto
```

### Paso 2: Crear las Bases de Datos en MySQL

Conectarse a MySQL y ejecutar:

```sql
CREATE DATABASE IF NOT EXISTS cine_users_db;
CREATE DATABASE IF NOT EXISTS cine_movies_db;
CREATE DATABASE IF NOT EXISTS cine_showtimes_db;
CREATE DATABASE IF NOT EXISTS cine_seats_db;
CREATE DATABASE IF NOT EXISTS cine_payments_db;
CREATE DATABASE IF NOT EXISTS cine_bookings_db;
CREATE DATABASE IF NOT EXISTS cine_rooms_db;
CREATE DATABASE IF NOT EXISTS cine_cines_db;
CREATE DATABASE IF NOT EXISTS cine_tickets_db;
CREATE DATABASE IF NOT EXISTS cine_notifications_db;
CREATE DATABASE IF NOT EXISTS cine_comuna_db;
```

O ejecutar el script incluido:

```bash
mysql -u root -p < init.sql
```

### Paso 3: Verificar Credenciales MySQL

Cada servicio usa el perfil `local` con estas credenciales por defecto en su `application.yml`:

```yaml
spring:
  config:
    activate:
      on-profile: local
  datasource:
    url: jdbc:mysql://localhost:3306/{nombre_db}?createDatabaseIfNotExist=true
    username: root
    password: root
```

Si tu MySQL usa credenciales distintas, editar el bloque `spring.profiles: local` en el `application.yml` de cada servicio.

### Paso 4: Iniciar Eureka Server (primero)

```bash
cd eureka-server
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Verificar que está activo en: `http://localhost:8761`

### Paso 5: Iniciar los Servicios Base

Iniciar primero los servicios que no dependen de otros:

```bash
# Terminal 1
cd user-service && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Terminal 2
cd movie-service && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Terminal 3
cd room-services && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Terminal 4
cd cinema-service && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### Paso 6: Iniciar Servicios Dependientes

```bash
# Terminal 5
cd showtime-service && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Terminal 6
cd seat-service && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Terminal 7
cd booking-service && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Terminal 8
cd notification-service && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Terminal 9
cd payment-service && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Terminal 10
cd ticket-service && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### Paso 7: Iniciar el API Gateway (último)

```bash
cd api-gateway && ./mvnw spring-boot:run
```

### Orden de inicio recomendado

```
1. eureka-server
2. user-service, movie-service, room-services, cinema-service  (servicios base)
3. showtime-service, seat-service                               (dependen de movie/room)
4. booking-service                                              (depende de user/showtime/seat/cinema)
5. notification-service                                         (servicio auxiliar)
6. payment-service                                              (depende de booking/showtime/notification)
7. ticket-service                                               (depende de booking/payment)
8. api-gateway                                                  (punto de entrada)
```

### Verificación de servicios activos

Una vez iniciados todos, deberían aparecer registrados en Eureka: `http://localhost:8761`

```
USER-SERVICE        → http://localhost:8080
MOVIE-SERVICE       → http://localhost:8081
SHOWTIME-SERVICE    → http://localhost:8082
SEAT-SERVICE        → http://localhost:8083
PAYMENT-SERVICE     → http://localhost:8084
BOOKING-SERVICE     → http://localhost:8085
ROOM-SERVICES       → http://localhost:8086
CINEMA-SERVICE      → http://localhost:8087
TICKET-SERVICE      → http://localhost:8088
NOTIFICATION-SERVICE→ http://localhost:8089
API-GATEWAY         → http://localhost:9000
```

---

## 🐳 Ejecución con Docker Compose

La forma más simple de levantar todo el sistema es con **Docker Compose**, que gestiona automáticamente el orden de inicio, las redes y la base de datos compartida.

### Requisitos

- **Docker** instalado (`docker --version`)
- **Docker Compose** (`docker compose version`)

### Levantar todo el sistema

```bash
# Desde la raíz del proyecto
docker compose up --build
```

Esto levanta en orden:
1. `cine-db` — MySQL 8.0 en puerto `3307` (mapeado desde `3306` interno), ejecuta `init.sql` automáticamente
2. `eureka-server` — Eureka en puerto `8761`
3. Todos los microservicios — con `restart: on-failure` hasta que Eureka y la DB estén listos
4. `api-gateway` — en puerto `9000`

### Levantar en segundo plano

```bash
docker compose up --build -d
```

### Ver logs de un servicio específico

```bash
docker compose logs -f booking-service
docker compose logs -f cine-db
```

### Detener todos los servicios

```bash
docker compose down
```

### Detener y borrar volúmenes (reset completo de DB)

```bash
docker compose down -v
```

### Notas importantes sobre Docker

- La base de datos MySQL corre en el puerto `3307` en el host (para no conflictuar con un MySQL local en `3306`).
- Los microservicios usan el perfil `docker` internamente, apuntando a `cine-db:3306` dentro de la red Docker `cine-network`.
- El healthcheck de MySQL espera hasta 40 intentos (unos 90 segundos) antes de que los servicios intenten conectarse.
- Si un servicio falla al iniciar por conectividad, `restart: on-failure` lo reintenta automáticamente.

---

## ⚙️ Estructura de Directorios por Servicio

```
{servicio}-service/
├── src/
│   ├── main/
│   │   ├── java/cine/proyect/{servicio}/
│   │   │   ├── Controller/           (REST Endpoints v1)
│   │   │   ├── Controller/           (REST Endpoints v2 — HATEOAS, donde aplica)
│   │   │   ├── Service/              (Lógica de negocio)
│   │   │   ├── Repository/           (Acceso a datos — Spring Data JPA)
│   │   │   ├── Model/                (Entidades JPA)
│   │   │   ├── DTO/                  (Data Transfer Objects)
│   │   │   ├── Client/               (OpenFeign Clients)
│   │   │   ├── assemblers/           (HATEOAS assemblers — User, Cinema)
│   │   │   ├── Exception/            (Excepciones personalizadas)
│   │   │   └── {Servicio}Application.java
│   │   └── resources/
│   │       ├── application.yml       (perfiles: local, docker)
│   │       └── db/migration/         (Scripts Flyway)
│   └── test/
│       └── java/                     (Pruebas unitarias — JUnit 5 + Mockito)
├── Dockerfile
├── pom.xml
├── mvnw
└── mvnw.cmd
```

---

## 📝 Notas Importantes

1. **Orden de inicio**: Iniciar siempre Eureka primero, luego los servicios base (User, Movie, Cinema, Room), y finalmente los servicios que dependen de ellos.

2. **Perfiles Spring**: Cada servicio tiene dos perfiles — `local` (MySQL en `localhost:3306`) y `docker` (MySQL en `cine-db:3306`). Usar siempre `-Dspring-boot.run.profiles=local` al correr manualmente.

3. **Migraciones con Flyway**: Los esquemas de base de datos se crean automáticamente al iniciar cada servicio gracias a Flyway. No es necesario crear tablas manualmente.

4. **HATEOAS v1 vs v2**: Los endpoints `/api/v1/...` retornan JSON plano. Los endpoints `/api/v2/...` (User, Cinema) retornan `EntityModel` con links HATEOAS y son los que se exponen a través del API Gateway.

5. **OpenFeign en producción**: Los clientes están configurados con URLs de Eureka (`lb://SERVICE-NAME`). Para ejecución local sin Eureka, las URLs estarían hardcodeadas a `localhost`.

6. **Transacciones distribuidas**: Las operaciones que involucran múltiples servicios (ej: pago → booking → ticket → notificación) no usan transacciones distribuidas formales. Para producción se recomienda implementar el Saga Pattern.

---

## 📚 Referencias

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Spring HATEOAS](https://spring.io/projects/spring-hateoas)
- [springdoc-openapi](https://springdoc.org/)
- [Netflix Eureka](https://spring.io/projects/spring-cloud-netflix)
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

## 👥 Autores

Proyecto de Sistema de Gestión de Cines — 2026

- **Gabriel Cornejo**
- **Vicente Espinoza**
- **Alexis Rozas**

---

**Última actualización**: Junio 2026
**Versión del Proyecto**: 2.0.0
