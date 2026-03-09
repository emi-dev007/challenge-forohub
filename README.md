# 🔐 ForoHub - Challenge Alura/Oracle ONE

API REST para gestión de foro de discusiones técnicas, desarrollada como proyecto final del programa **Oracle Next Education (ONE)**.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![License](https://img.shields.io/badge/License-Educational-yellow)

## 🎯 Descripción

ForoHub es una API REST que permite la gestión completa de tópicos en un foro de discusiones. Implementa autenticación y autorización mediante JWT (JSON Web Tokens), garantizando que solo usuarios autenticados puedan crear, modificar o eliminar contenido, y que solo los autores puedan editar sus propios tópicos.

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- **JWT** - Autenticación stateless (Auth0 java-jwt 4.4.0)
- **MySQL 8** - Base de datos relacional
- **Maven** - Gestión de dependencias
- **Lombok** - Reducción de código boilerplate
- **BCrypt** - Encriptación de contraseñas

## 📋 Funcionalidades

### 🔐 Autenticación
- ✅ Login con generación de token JWT
- ✅ Validación de credenciales con BCrypt
- ✅ Tokens con expiración de 2 horas
- ✅ Protección de endpoints con Spring Security

### 📝 CRUD de Tópicos
- ✅ **Crear** tópico (requiere autenticación)
- ✅ **Listar** todos los tópicos con paginación
- ✅ **Obtener** detalle de tópico por ID
- ✅ **Actualizar** tópico (solo el autor)
- ✅ **Eliminar** tópico de forma lógica (solo el autor)

### ✔️ Validaciones Implementadas
- ✅ No permite tópicos duplicados (mismo título + mensaje)
- ✅ Solo el autor puede modificar o eliminar su tópico
- ✅ Validación de campos obligatorios (@NotBlank)
- ✅ Validación de formato de email
- ✅ Token JWT requerido en endpoints protegidos

## 🛠️ Instalación y Ejecución

### Requisitos Previos
- Java 17 o superior
- MySQL 8 o superior
- Maven 4+
- Postman o Insomnia (para testing)

### 1️⃣ Configuración de Base de Datos

**Crear base de datos:**
```sql
CREATE DATABASE forohub_db;
USE forohub_db;
```

**Crear tablas:**
```sql
CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(300) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
```

**Insertar usuario de prueba:**
```sql
INSERT INTO usuarios (nombre, email, password, activo) 
VALUES ('Admin', 'admin@forohub.com', 
'$2a$10$Y50UaMFOxteibQEYLrwuAuXcIp0QaELVqTXVqT9J3V8X0b.Hm2V5e', true);
```

### 2️⃣ Configuración del Proyecto

**Clonar repositorio:**
```bash
git clone https://github.com/emi-dev007/challenge-forohub-prueba.git
cd challenge-forohub-prueba
```

**Configurar credenciales en `application.properties`:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forohub_db
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD_AQUI

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

api.security.token.secret=mi-clave-super-secreta-12345
api.security.token.expiration=7200000
```

### 3️⃣ Ejecutar Aplicación
```bash
mvn clean install
mvn spring-boot:run
```

**Aplicación disponible en:** `http://localhost:8080`

## 📡 Endpoints de la API

### 🔓 Autenticación (Público)

#### Login
Genera un token JWT para autenticación.
```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@forohub.com",
  "password": "123456"
}
```

**Respuesta exitosa (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmb3JvaHViIiwic3ViIjoiYWRtaW5AZm9yb2h1Yi5jb20iLCJpZCI6MSwiZXhwIjoxNzA5NzYwMDAwfQ.abc123..."
}
```

---

### 🔒 Tópicos (Requieren Autenticación)

**Todas las siguientes peticiones requieren el header:**
```
Authorization: Bearer {TOKEN_JWT}
```

#### Crear Tópico
```http
POST /topicos
Content-Type: application/json
Authorization: Bearer {TOKEN}

{
  "titulo": "¿Cómo usar Spring Security?",
  "mensaje": "Tengo dudas sobre la configuración de JWT",
  "curso": "Spring Boot"
}
```

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "titulo": "¿Cómo usar Spring Security?",
  "mensaje": "Tengo dudas sobre la configuración de JWT",
  "fechaCreacion": "2026-03-06T15:30:00",
  "estado": "ABIERTO",
  "autor": "Admin",
  "curso": "Spring Boot"
}
```

#### Listar Tópicos
```http
GET /topicos
Authorization: Bearer {TOKEN}
```

**Respuesta (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "titulo": "¿Cómo usar Spring Security?",
      "mensaje": "Tengo dudas sobre...",
      "fechaCreacion": "2026-03-06T15:30:00",
      "estado": "ABIERTO",
      "autor": "Admin",
      "curso": "Spring Boot"
    }
  ],
  "pageable": {...},
  "totalElements": 1
}
```

#### Obtener Detalle de Tópico
```http
GET /topicos/{id}
Authorization: Bearer {TOKEN}
```

#### Actualizar Tópico
Solo el autor puede actualizar su tópico.
```http
PUT /topicos/{id}
Content-Type: application/json
Authorization: Bearer {TOKEN}

{
  "titulo": "Nuevo título actualizado",
  "mensaje": "Mensaje actualizado"
}
```

#### Eliminar Tópico (Lógico)
Solo el autor puede eliminar su tópico. La eliminación es lógica (cambia estado a CERRADO).
```http
DELETE /topicos/{id}
Authorization: Bearer {TOKEN}
```

**Respuesta (204 No Content)**

## 🔒 Seguridad

- **Autenticación:** JWT (JSON Web Token) stateless
- **Encriptación:** BCrypt para contraseñas
- **Autorización:** Spring Security
- **Expiración de tokens:** 2 horas
- **Restricciones:** Solo el autor puede modificar/eliminar sus tópicos

## 👤 Usuario de Prueba

Para testing de la API:
```
Email: admin@forohub.com
Password: 123456
```

## 📂 Estructura del Proyecto
```
src/main/java/com/alura/forohub/
├── controller/          # Controladores REST (AuthController, TopicoController)
├── dto/                 # Data Transfer Objects (LoginDTO, CrearTopicoDTO, etc.)
├── model/               # Entidades JPA (Usuario, Topico, EstadoTopico)
├── repository/          # Repositorios Spring Data (UsuarioRepository, TopicoRepository)
├── security/            # Configuración de seguridad (SecurityConfig, SecurityFilter, TokenService)
└── service/             # Lógica de negocio (UsuarioService, TopicoService)
```

## 🧪 Testing

Se recomienda usar **Postman** o **Insomnia** para probar los endpoints.

**Flujo de prueba:**
1. Hacer login para obtener token
2. Copiar token de la respuesta
3. Agregar header `Authorization: Bearer {TOKEN}` en las siguientes peticiones
4. Probar CRUD completo de tópicos

## 🎓 Sobre el Proyecto

Este proyecto fue desarrollado como requisito final del programa **Oracle Next Education (ONE)** en colaboración con **Alura Latam**.

**Objetivos cumplidos:**
- ✅ Implementación completa de API REST
- ✅ Autenticación y autorización con JWT
- ✅ Operaciones CRUD completas
- ✅ Validaciones de negocio
- ✅ Seguridad con Spring Security
- ✅ Base de datos relacional con MySQL
- ✅ Buenas prácticas de desarrollo

## 👨‍💻 Autor

Desarrollado por **Emiliano** 

- GitHub: [@emi-dev007](https://github.com/emi-dev007)
- Proyecto: [ForoHub](https://github.com/emi-dev007/challenge-forohub-prueba)

## 📄 Licencia

Este proyecto fue desarrollado con fines educativos como parte del programa Oracle Next Education (ONE).

---

⭐ **Challenge completado exitosamente** ⭐
