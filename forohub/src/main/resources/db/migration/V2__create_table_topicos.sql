CREATE TABLE topicos (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         titulo VARCHAR(255) NOT NULL,
                         mensaje TEXT NOT NULL,
                         fecha_creacion DATETIME NOT NULL,
                         estado VARCHAR(50) NOT NULL,
                         autor_id BIGINT NOT NULL,
                         curso VARCHAR(100) NOT NULL,
                         PRIMARY KEY (id),
                         FOREIGN KEY (autor_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;