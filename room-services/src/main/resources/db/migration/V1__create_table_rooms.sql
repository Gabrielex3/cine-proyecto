CREATE TABLE rooms (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(255) NOT NULL UNIQUE,
                       tipo VARCHAR(50),
                       activo BOOLEAN NOT NULL DEFAULT TRUE
);