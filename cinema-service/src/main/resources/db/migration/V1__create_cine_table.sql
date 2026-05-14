CREATE TABLE comunas (
                         id BIGINT AUTO_INCREMENT NOT NULL,
                         nombre VARCHAR(255) NOT NULL,
                         CONSTRAINT pk_comunas PRIMARY KEY (id),
                         CONSTRAINT uc_comunas_nombre UNIQUE (nombre)
);

CREATE TABLE cinemas (
                         id BIGINT AUTO_INCREMENT NOT NULL,
                         cine VARCHAR(255) NOT NULL,
                         direccion VARCHAR(255) NOT NULL,
                         comuna_id BIGINT NOT NULL,
                         CONSTRAINT pk_cinemas PRIMARY KEY (id),
                         CONSTRAINT uc_cinemas_cine UNIQUE (cine),
                         CONSTRAINT uc_cinemas_direccion UNIQUE (direccion),
                         CONSTRAINT FK_CINEMA_ON_COMUNA FOREIGN KEY (comuna_id) REFERENCES comunas (id)
);