# 📚 LiterAlura - Challenge Alura

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.8-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Status](https://img.shields.io/badge/Status-Completed-green)

Un catálogo de libros desarrollado en Java que consume la API de Gutendx para buscar libros, guardarlos en PostgreSQL y realizar consultas avanzadas sobre libros y autores.

## 📋 Descripción

Este proyecto forma parte del **Challenge Back End - Java** de **Alura LATAM** en colaboración con **Oracle Next Education (ONE)**.

LiterAlura permite a los usuarios buscar libros del Proyecto Gutenberg, almacenarlos en una base de datos PostgreSQL y realizar consultas estadísticas avanzadas, incluyendo análisis por idiomas y consultas temporales de autores.

## ✨ Características

- 🔍 **Búsqueda de libros** en tiempo real usando API de Gutendx
- 🗄️ **Persistencia completa** en PostgreSQL con JPA/Hibernate
- 📊 **Estadísticas por idioma** usando Java Streams y derived queries
- 👥 **Consultas de autores vivos** en años específicos
- 🌍 **Análisis multiidioma** con comparaciones y distribuciones
- ⚡ **Interfaz de consola interactiva** con validaciones robustas
- 🔗 **Relaciones bidireccionales** entre libros y autores
- 📈 **Reportes estadísticos** detallados

## 🛠️ Tecnologías Utilizadas

- **Java 17** - Lenguaje de programación
- **Spring Boot 3.4.8** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL 16** - Base de datos relacional
- **Hibernate** - ORM para mapeo objeto-relacional
- **Jackson** - Procesamiento JSON
- **Maven** - Gestión de dependencias
- **Gutendx API** - Fuente de datos de libros

## 🚀 Instalación y Configuración

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- PostgreSQL 12+ instalado y ejecutándose
- Conexión a internet para API de Gutendx

### Pasos de instalación

1. **Clonar el repositorio**

```bash
git clone https://github.com/TuUsuario/literalura.git
cd literalura
```

2. **Configurar PostgreSQL**

```bash
-- Crear base de datos
CREATE DATABASE literalura;

-- Crear usuario (opcional)
CREATE USER alura WITH PASSWORD 'alura123';
GRANT ALL PRIVILEGES ON DATABASE literalura TO alura;
```

3. **Configurar application.properties**

```bash
# Configuración de PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=alura
spring.datasource.password=alura123
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Configuración del servidor
server.port=8080
```

4. **Compilar el Proyecto**

```bash
mvn clean compile
```

5. **Ejecutar la aplicación**

```bash
mvn spring-boot:run
```

## 💻 Uso

## Menú Principal

### Al ejecutar la aplicación, verás un menú interactivo:

```bash

═══════════════════════════════════════════
📚 LITERALURA - CATÁLOGO DE LIBROS 📚
═══════════════════════════════════════════

1️⃣  - Buscar libro por título
2️⃣  - Listar libros registrados
3️⃣  - Listar autores registrados
4️⃣  - Listar autores vivos en un año determinado
5️⃣  - Listar libros por idioma
6️⃣  - Estadísticas por idioma específico
7️⃣  - Estadísticas completas de idiomas
8️⃣  - Comparar dos idiomas
9️⃣  - Distribución de libros por idioma
🔟  - Menú avanzado de autores
0️⃣  - Salir

Seleccione una opción:
```

## Ejemplos de Uso

### 1. Buscar y guardar un libro

```bash
Opción: 1
📖 Ingrese el título del libro: Don Quixote

🔄 Buscando en API y guardando en PostgreSQL...
✅ ¡LIBRO GUARDADO EN BASE DE DATOS!

═══════════════════════════════════════
📖 ID: 996
📚 Título: Don Quixote
👤 Autor: Cervantes Saavedra, Miguel de
🌍 Idioma: Inglés
📥 Descargas: 7,369
═══════════════════════════════════════
```

### 2. Consultar autores vivos en un año

```bash
Opción: 4
📆 Ingrese el año (ej: 1850, 1900, 1950): 1605

👥 AUTORES VIVOS EN 1605
═══════════════════════════════════════════════════════════════════════════════

1. 👤 Cervantes Saavedra, Miguel de
   📅 Nacimiento: 1547
   ⚰️ Fallecimiento: 1616
   🎂 Edad en 1605: 58 años
   📚 Libros registrados: 3
      📖 Don Quixote

2. 👤 Shakespeare, William
   📅 Nacimiento: 1564
   ⚰️ Fallecimiento: 1616
   🎂 Edad en 1605: 41 años
   📚 Libros registrados: 5
```

## 👨‍💻 Autor

### Luis Alfredo Rios Huanca

#### GitHub: RiosLuis10245

#### LinkedIn: https://www.linkedin.com/in/luis-ri0s/

#### Email: rioshuancaluisalfredo@gmail.com
