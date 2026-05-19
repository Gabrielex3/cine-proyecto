CREATE TABLE notificaciones (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                id_ticket BIGINT NOT NULL,
                                usuarios BIGINT NOT NULL,
                                mensaje VARCHAR(255) NOT NULL,
                                tipo_mensaje VARCHAR(100) NOT NULL,
                                fecha_envio DATETIME NOT NULL,
                                status VARCHAR(50) NOT NULL
);