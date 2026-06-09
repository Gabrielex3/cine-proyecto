# 🎬 Sistema de Gestión de Cines - Arquitectura de Microservicios

## 📋 Tabla de Contenidos
- [Descripción General](#descripción-general)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Servicios Disponibles](#servicios-disponibles)
- [Conexiones OpenFeign](#conexiones-openfeign)
- [Tecnologías y Métodos HTTP](#tecnologías-y-métodos-http)
- [Endpoints por Servicio](#endpoints-por-servicio)
- [Flujo de Peticiones](#flujo-de-peticiones)
- [Instalación](#instalación)
- [Ejecución](#ejecución)
- [Configuración](#configuración)

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

Cada servicio es **independiente y escalable**, comunicándose a través de **OpenFeign** para llamadas síncronas entre microservicios.

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                    SISTEMA DE CINES (Microservicios)            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐            │
│  │   Users      │  │   Movie      │  │   Cinema     │            │
│  │  (8080)      │  │   (8081)     │  │  (8087)      │            │
│  └──────────────┘  └──────────────┘  └──────────────┘            │
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐            │
│  │   Room       │  │  Seat        │  │  Showtime    │            │
│  │  (8086)      │  │   (8083)     │  │  (8082)      │            │
│  └──────────────┘  └──────────────┘  └──────────────┘            │
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐            │
│  │   booking    │  │   Ticket     │  │   Payment    │            │
│  │  (8085)      │  │  (8088)      │  │  (8084)      │            │
│  └──────────────┘  └──────────────┘  └──────────────┘            │
│                                                                   │
│  ┌──────────────────────────────────────────────────────┐        │
│  │        Notificación (8089)                           │        │
│  └──────────────────────────────────────────────────────┘        │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Servicios Disponibles

| # | Servicio | Puerto | BD | Descripción |
|---|----------|--------|-----|-------------|
| 1 | **User Service** | 8080 | MySQL | Gestión de usuarios, registro y autenticación |
| 2 | **Movie Service** | 8081 | MySQL | Catálogo y gestión de películas |
| 3 | **Showtime Service** | 8082 | MySQL | Horarios de funciones (películas en salas) |
| 4 | **Seat Service** | 8083 | MySQL | Gestión de asientos en salas |
| 5 | **Payment Service** | 8084 | MySQL | Procesamiento de pagos y transacciones |
| 6 | **Booking Service** | 8085 | MySQL | Gestión de reservas de entradas |
| 7 | **Room Service** | 8086 | MySQL | Administración de salas de cine |
| 8 | **Cinema Service** | 8087 | MySQL | Gestión de sucursales de cine |
| 9 | **Ticket Service** | 8088 | MySQL | Emisión y gestión de tickets/entradas |
| 10 | **Notification Service** | 8089 | MySQL | Envío de notificaciones a usuarios |

---

## 🔗 Conexiones OpenFeign

### Mapa de Comunicación entre Microservicios

```
┌─────────────────────────────────────────────────────────────────┐
│                    FLUJO DE CONEXIONES OPENFEIGN                 │
├─────────────────────────────────────────────────────────────────┤

┌─── SHOWTIME SERVICE (8082) ───┐
│                                │
│  movieClient ────────────→ MOVIE SERVICE (8081)
│  roomClient ─────────────→ ROOM SERVICE (8086)
└────────────────────────────────┘

┌─── BOOKING SERVICE (8085) ────┐
│                                │
│  userClient ──────────────→ USER SERVICE (8080)
│  showtimeClient ──────────→ SHOWTIME SERVICE (8082)
│  seatClient ──────────────→ SEAT SERVICE (8083)
│  cinemaClient ────────────→ CINEMA SERVICE (8087)
└────────────────────────────────┘
        │
        └──────────────┐
                       │
                       ▼
┌─── TICKET SERVICE (8088) ────┐
│                                │
│  BookingClient ───────────→ BOOKING SERVICE (8085)
│  PaymentClient ───────────→ PAYMENT SERVICE (8084)
└────────────────────────────────┘

┌─── PAYMENT SERVICE (8084) ────┐
│                                │
│  BookingClient ───────────→ BOOKING SERVICE (8085)
│  TicketClient ────────────→ TICKET SERVICE (8088)
│  showtimeClient ──────────→ SHOWTIME SERVICE (8082)
│  notificationClient ──────→ NOTIFICATION SERVICE (8089)
└────────────────────────────────┘

┌─── NOTIFICATION SERVICE (8089) ────┐
│                                      │
│  PaymentClient ────────────────→ PAYMENT SERVICE (8084)
└──────────────────────────────────────┘

┌─── SEAT SERVICE (8083) ────┐
│                             │
│  SalaClient ─────────────→ ROOM SERVICE (8086)
└─────────────────────────────┘
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
| `TicketClient` | Payment (8084) | Ticket (8088) | `GET /api/v1/cine/tickets/{id}` | Obtiene información de ticket |
| `showtimeClient` | Payment (8084) | Showtime (8082) | `GET /api/v1/cine/showtime/{id}` | Obtiene función por ID |
| `notificationClient` | Payment (8084) | Notification (8089) | `POST /api/v1/cine/notification/send` | Envía notificación |
| `PaymentClient` | Notification (8089) | Payment (8084) | `GET /api/v1/cine/payment/{id}` | Obtiene información de pago |
| `SalaClient` | Seat (8083) | Room (8086) | `GET /api/v1/cine/room/{id}` | Obtiene información de sala |

---

## 🛠️ Tecnologías y Métodos HTTP

### Stack Tecnológico
- **Framework**: Spring Boot 3.x
- **Lenguaje**: Java 17+
- **Comunicación Inter-Servicios**: OpenFeign (Netflix OSS)
- **Base de Datos**: MySQL 8.0+
- **Gestión de Dependencias**: Maven
- **Validación**: Jakarta Validation (Bean Validation)
- **Logging**: SLF4J + Logback
- **Build Tool**: Maven Wrapper

### Métodos HTTP Utilizados

| Método | Descripción | Uso |
|--------|-------------|-----|
| **GET** | Recupera información | Obtener datos existentes |
| **POST** | Crea nuevo recurso | Crear entidades (usuarios, películas, etc.) |
| **PUT** | Actualiza recurso existente | Modificar datos existentes |
| **DELETE** | Elimina recurso | Borrar entidades |

### Estructura de Respuestas

Todas las respuestas siguen el patrón REST con códigos HTTP estándar:

```
✅ 200 OK - Solicitud exitosa (GET, PUT, DELETE)
✅ 201 CREATED - Recurso creado exitosamente (POST)
❌ 400 BAD REQUEST - Datos inválidos
❌ 404 NOT FOUND - Recurso no encontrado
❌ 500 INTERNAL SERVER ERROR - Error del servidor
```

---

## 📡 Endpoints por Servicio

### 1️⃣ USER SERVICE (Puerto 8080)

**Base URL**: `http://localhost:8080/api/v1/cine/users`

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

**Campos del Modelo User**:
- `id` (Long, autonumérico)
- `rut` (String, único, no nulo)
- `nombre` (String)
- `apellido` (String)
- `correo` (String, único, no nulo)
- `telefono` (String, único, no nulo)

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

**Campos del Modelo Movie**:
- `id` (Long, autonumérico)
- `titulo` (String, único, no nulo)
- `genero` (String, no nulo)
- `duracion` (Integer, no nulo)

---

### 3️⃣ SHOWTIME SERVICE (Puerto 8082)

**Base URL**: `http://localhost:8082/api/v1/cine/showtime`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar todas las funciones |
| GET | `/{id}` | Obtener función por ID |
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

**Campos del Modelo Showtime**:
- `id` (Long, autonumérico)
- `movieId` (Long, no nulo)
- `roomId` (Long, no nulo)
- `fechaHora` (LocalDateTime, no nulo)
- `precioTicket` (Double)

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

**Campos del Modelo Asiento**:
- `id` (Long, autonumérico)
- `numeroFila` (String, no nulo, máx 10 caracteres)
- `disponible` (boolean, no nulo)
- `salaId` (Long, no nulo)

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
  "estado": "PENDIENTE"
}
```

**Campos del Modelo Payment**:
- `id` (Long, autonumérico)
- `monto` (BigDecimal, no nulo)
- `estado` (PaymentStatus, no nulo)
- `reservaId` (Long, no nulo, único)
- `timestamp` (LocalDateTime, no nulo)

**Estados de Pago**: PENDIENTE, APROBADO, RECHAZADO, CANCELADO

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
  "status": "PENDIENTE",
  "cinema": 1
}
```

**Campos del Modelo Booking**:
- `id` (Long, autonumérico)
- `userId` (Long, no nulo)
- `showtimeId` (Long, no nulo)
- `seatId` (Long, no nulo)
- `status` (bookingStatus)
- `cinema` (Long, no nulo)

**Estados de Booking**: PENDIENTE, CONFIRMADA, CANCELADA

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

**Campos del Modelo Sala**:
- `id` (Long, autonumérico)
- `nombre` (String, único, no nulo)
- `tipo` (String, máx 50 caracteres)
- `activo` (Boolean, no nulo, por defecto true)

---

### 8️⃣ CINEMA SERVICE (Puerto 8087)

**Base URL**: `http://localhost:8087/api/v1/cine/cinemas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Listar todas las sucursales |
| GET | `/{id}` | Obtener sucursal por ID |
| POST | `/` | Crear nueva sucursal |
| PUT | `/{id}` | Actualizar sucursal |
| DELETE | `/{id}` | Eliminar sucursal |

**Ejemplo de Request (POST)**:
```json
{
  "cine": "Cinemark Las Condes",
  "direccion": "Av. Las Condes 13000",
  "comunaId": 1
}
```

**Campos del Modelo Cinema**:
- `id` (Long, autonumérico)
- `cine` (String, único, no nulo)
- `direccion` (String, única, no nulo)
- `comuna` (Comuna - relación ManyToOne, no nulo)
  - contiene `comunaId`

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

**Campos del Modelo Ticket**:
- `id` (Long, autonumérico)
- `bookingId` (Long, único, no nulo)
- `paymentId` (Long, único, no nulo)
- `issueDate` (LocalDateTime, no nulo)

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
  "estado": "ENVIADA"
}
```

**Campos del Modelo Notification**:
- `id` (Long, autonumérico)
- `idTicket` (Long, no nulo)
- `idUsuario` (Long, no nulo)
- `mensaje` (String, no nulo)
- `tipo` (String, no nulo)
- `fechaEnvio` (LocalDateTime, no nulo)
- `estado` (notificationStatus, no nulo)

**Estados de Notificación**: ENVIADA, LEIDA, NO_LEIDA

---

## 📊 Flujo de Peticiones

### Scenario 1: Crear una Reserva de Entrada

```
┌─────────────────────────────────────────────────────────────────┐
│                    FLUJO: CREAR RESERVA                          │
└─────────────────────────────────────────────────────────────────┘

1. Cliente envía POST a BOOKING SERVICE (8085)
   POST /api/v1/cine/bookings
   {
     "userId": 1,
     "showtimeId": 5,
     "seatId": 10,
     "status": "PENDIENTE",
     "cinema": 1
   }

2. BOOKING SERVICE valida datos ✓
   └─→ Llama a BOOKING SERVICE → USER SERVICE (8085 → 8080)
       userClient.obtenerPorIdUser(1)
   
3. BOOKING SERVICE obtiene datos de SHOWTIME
   └─→ Llama a BOOKING SERVICE → SHOWTIME SERVICE (8085 → 8082)
       showtimeClient.obtenerPorIdShowtime(5)
   
4. BOOKING SERVICE obtiene datos de ASIENTO
   └─→ Llama a BOOKING SERVICE → SEAT SERVICE (8085 → 8083)
       seatClient.findAsientoById(10)

5. BOOKING SERVICE obtiene datos de CINEMA
   └─→ Llama a BOOKING SERVICE → CINEMA SERVICE (8085 → 8087)
       cinemaClient.getCinemaById(1)

6. BOOKING SERVICE crea la reserva en DB
   └─→ Devuelve respuesta 201 CREATED al Cliente

7. Respuesta:
   {
     "id": 1,
     "userId": 1,
     "showtimeId": 5,
     "seatId": 10,
     "status": "PENDIENTE",
     "cinema": 1
   }
```

### Scenario 2: Procesar Pago y Generar Ticket

```
┌─────────────────────────────────────────────────────────────────┐
│              FLUJO: PROCESAR PAGO Y GENERAR TICKET               │
└─────────────────────────────────────────────────────────────────┘

1. Cliente envía POST a PAYMENT SERVICE (8084)
   POST /api/v1/cine/payment
   {
     "reservaId": 1,
     "monto": 24000.00,
     "estado": "PENDIENTE"
   }

2. PAYMENT SERVICE valida datos ✓

3. PAYMENT SERVICE obtiene datos de BOOKING
   └─→ BookingClient.buscarReservaPorId(1)
       De: PAYMENT SERVICE (8084) → BOOKING SERVICE (8085)

4. PAYMENT SERVICE procesa el pago en DB ✓
   └─→ Estado: APROBADO
   └─→ Guarda timestamp actual

5. PAYMENT SERVICE actualiza estado en BOOKING
   └─→ BookingClient.actualizarStatus(1, CONFIRMADA)
       De: PAYMENT SERVICE (8084) → BOOKING SERVICE (8085)

6. PAYMENT SERVICE notifica al NOTIFICATION SERVICE
   └─→ notificationClient.enviarNotificacion(...)
       De: PAYMENT SERVICE (8084) → NOTIFICATION SERVICE (8089)

7. TICKET SERVICE se activa automáticamente
   └─→ POST /api/v1/cine/tickets
   
8. TICKET SERVICE llama a BOOKING para obtener detalles
   └─→ BookingClient.buscarReservaPorId(1)
       De: TICKET SERVICE (8088) → BOOKING SERVICE (8085)

9. TICKET SERVICE llama a PAYMENT para obtener detalles
   └─→ PaymentClient.obtenerPago(1)
       De: TICKET SERVICE (8088) → PAYMENT SERVICE (8084)

10. TICKET SERVICE genera ticket y devuelve:
    {
      "id": 1,
      "bookingId": 1,
      "paymentId": 1,
      "issueDate": "2024-06-10T14:30:00"
    }
```

### Scenario 3: Obtener Función con Película y Sala

```
┌─────────────────────────────────────────────────────────────────┐
│          FLUJO: OBTENER FUNCIÓN (Con datos relacionados)         │
└─────────────────────────────────────────────────────────────────┘

1. Cliente envía GET a SHOWTIME SERVICE (8082)
   GET /api/v1/cine/showtime/5

2. SHOWTIME SERVICE obtiene función de DB ✓

3. SHOWTIME SERVICE obtiene película relacionada
   └─→ movieClient.obtenerPeliculaPorId(1)
       De: SHOWTIME SERVICE (8082) → MOVIE SERVICE (8081)

4. SHOWTIME SERVICE obtiene sala relacionada
   └─→ roomClient.buscarPorId(1)
       De: SHOWTIME SERVICE (8082) → ROOM SERVICE (8086)

5. SHOWTIME SERVICE arma respuesta completa:
   {
     "id": 5,
     "movieId": 1,
     "movieDetails": {
       "id": 1,
       "titulo": "Avatar 2",
       "genero": "Ciencia Ficción",
       "duracion": 192
     },
     "roomId": 1,
     "roomDetails": {
       "id": 1,
       "nombre": "Sala Premium IMAX",
       "tipo": "IMAX",
       "activo": true
     },
     "fechaHora": "2024-06-15T19:00:00",
     "precioTicket": 12000.0
   }
```

---

## 💻 Instalación

### Requisitos Previos
- **Java 17+** 
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

### Pasos de Instalación

#### 1. Clonar el Repositorio
```bash
git clone https://github.com/Gabrielex3/cine-proyecto.git
cd cine-proyecto
```

#### 2. Crear Bases de Datos MySQL
```sql
CREATE DATABASE user_db;
CREATE DATABASE movie_db;
CREATE DATABASE showtime_db;
CREATE DATABASE seat_db;
CREATE DATABASE payment_db;
CREATE DATABASE booking_db;
CREATE DATABASE room_db;
CREATE DATABASE cinema_db;
CREATE DATABASE ticket_db;
CREATE DATABASE notification_db;
```

#### 3. Configurar Archivo de Propiedades
Editar `application.properties` en cada servicio:

```properties
# Configuración Base de Datos (cambiar para cada servicio)
spring.datasource.url=jdbc:mysql://localhost:3306/[nombre_db]?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Logging
logging.level.root=INFO
logging.level.cine.proyect=DEBUG

# Puerto de la aplicación
server.port=8080
```

#### 4. Compilar los Proyectos
```bash
# En la carpeta raíz del proyecto
mvn clean install -DskipTests
```

---

## 🚀 Ejecución

### Ejecutar cada servicio individualmente

---

## ⚙️ Configuración

### Estructura de Directorios por Servicio

```
servicio-service/
├── src/
│   ├── main/
│   │   ├── java/cine/proyect/servicio/
│   │   │   ├── Controller/           (REST Endpoints)
│   │   │   ├── Service/              (Lógica de negocio)
│   │   │   ├── Repository/           (Acceso a datos)
│   │   │   ├── Model/                (Entidades JPA)
│   │   │   ├── DTO/                  (Data Transfer Objects)
│   │   │   ├── Client/               (OpenFeign Clients)
│   │   │   ├── Exception/            (Excepciones personalizadas)
│   │   │   └── {Servicio}Application.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/         (Scripts SQL)
│   └── test/
│       └── java/                     (Pruebas unitarias)
├── pom.xml
├── mvnw
└── mvnw.cmd
```

### Configuración de OpenFeign

Cada servicio que consume otros servicios debe tener la anotación `@EnableFeignClients`:

```java
@SpringBootApplication
@EnableFeignClients
public class ServicioApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServicioApplication.class, args);
    }
}
```

### Dependencias Maven Requeridas

```xml
<!-- Spring Cloud OpenFeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- Spring Boot Starter Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Boot Starter Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- MySQL Connector -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.33</version>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

---

## 📝 Notas Importantes

1. **Order de Inicio**: Es recomendable iniciar los servicios base primero (User, Movie, Cinema, Room) antes de los servicios que dependen de ellos.

2. **OpenFeign**: Los clientes OpenFeign están configurados con URLs hardcodeadas en localhost. Para producción, considera usar Eureka Service Discovery.

3. **Base de Datos**: Cada servicio tiene su propia base de datos independiente (Database per Service Pattern).

4. **Transacciones Distribuidas**: Las transacciones entre microservicios pueden requerir implementación de Saga Pattern o compensating transactions.

5. **Logging**: Todos los servicios utilizan SLF4J para logging. Los logs se muestran en consola.

6. **Validación**: Se utiliza Jakarta Validation para validación de datos en los DTOs.

---

## 📚 Referencias

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [REST API Best Practices](https://restfulapi.net/)
- [Microservices Architecture](https://microservices.io/)

---

## 👥 Autor/es

Proyecto de Sistema de Gestión de Cines - 2026
--
Gabriel Cornejo
--
Vicente Espinoza 
--
Alexis Rozas
--
---


**Última actualización**: Mayo 2026
**Versión del Proyecto**: 1.0.0

