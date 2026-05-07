CREATE TABLE pagos (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       monto DOUBLE NOT NULL,
                       moneda VARCHAR(3) NOT NULL,
                       estado VARCHAR(20) NOT NULL,
                       reserva_id BIGINT NOT NULL,
                       timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);