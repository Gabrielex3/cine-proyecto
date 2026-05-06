CREATE TABLE movies (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        titulo VARCHAR(255) NOT NULL UNIQUE,
                        genero VARCHAR(100) NOT NULL,
                        duracion INT NOT NULL
);
