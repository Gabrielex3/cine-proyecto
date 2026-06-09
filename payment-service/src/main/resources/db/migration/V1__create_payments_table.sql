CREATE TABLE pagos (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       monto DECIMAL(19,2) NOT NULL,
                       estado VARCHAR(20) NOT NULL,
                       reserva_id BIGINT NOT NULL UNIQUE,
                       timestamp DATETIME NOT NULL
);