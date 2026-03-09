CREATE TABLE usuarios (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          nombre VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(300) NOT NULL,
                          activo BOOLEAN NOT NULL DEFAULT TRUE,
                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO usuarios (nombre, email, password, activo)
VALUES ('Admin', 'admin@forohub.com', '$2a$10$Y50UaMFOxteibQEYLrwuAuXcIp0QaELVqTXVqT9J3V8X0b.Hm2V5e', true);