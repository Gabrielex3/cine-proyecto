CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       RUT VARCHAR(12) NOT NULL UNIQUE,
                       NOMBRE VARCHAR(50),
                       APELLIDO VARCHAR(50),
                       CORREO VARCHAR(100) NOT NULL UNIQUE,
                       CONTRASENA VARCHAR(30) NOT NULL,
                       TELEFONO VARCHAR(20) NOT NULL UNIQUE
);