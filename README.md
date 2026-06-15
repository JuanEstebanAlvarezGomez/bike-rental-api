## Bike Rental API

API REST para la gestión de alquiler de bicicletas. Permite registrar bicicletas, controlar disponibilidad, gestionar alquileres y calcular costos automáticamente aplicando tarifas por tipo de bicicleta y multas por devoluciones tardías.

## API Desplegada

La API está disponible en la nube con Render.

NOTA: La primera vez que se ingresa al dominio puede tardar hasta un minuto de retraso en cargar debido a que el servicio web se inactiva si no hay visitas recientes.

API Base | https://bike-rental-api-cqba.onrender.com
Swagger UI | https://bike-rental-api-cqba.onrender.com/swagger-ui/index.html 

## Tabla de Contenidos

- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Requisitos Previos](#requisitos-previos)
- [Ejecución Local](#ejecución-local)
- [Ejemplos de Peticiones con Postman](#ejemplos-de-peticiones-con-postman)
- [Ejecución de Tests](#ejecución-de-tests)

## Tecnologías

-Java v21 - Lenguaje principal 

-Spring Boot v3.5.15 - Framework base 

-Spring Security v6.x - Autenticación Basic Auth 

-Spring Data JPA - Persistencia de datos

-H2 Database - Base de datos en memoria

-SpringDoc OpenAPI v2.8.8 - Documentación Swagger

-Maven v3.8+ - Gestor de dependencias

-Lombok v1.18.34 - Reducción de código boilerplate

-JUnit 5 + Mockito - Pruebas unitarias

-Render - Plataforma de despliegue

## Arquitectura

### Capas de la Aplicación

-Controller Layer
  (REST endpoints, validación) 
  
-Service Layer
  (Lógica de negocio, transacciones)

-Repository Layer
  (Acceso a datos, JPA)

-Domain Layer
  (Entidades, enums, DTOs)

### Justificación

- Arquitectura por capas: Separa responsabilidades, facilitando mantenimiento y pruebas.
- Inyección de dependencias: Usando `@RequiredArgsConstructor` de Lombok.
- Servicio de precios separado (`PricingService`): Principio de responsabilidad única (SOLID).
- DTOs y Mappers: para aislamiento entre capa de API y dominio.
- Manejo global de excepciones: usando `@RestControllerAdvice` para respuestas consistentes.

## Requisitos Previos

- Java 21 o superior
- Maven 3.8+
- Git

## Ejecución Local

### 1. Clonar el repositorio

Abrir cmd como administrador o git bash y pegar en la ruta deseada el siguiente comando:
   
git clone https://github.com/JuanEstebanAlvarezGomez/bike-rental-api.git

Luego pegar:

cd bike-rental-api

### 2. Compilar el proyecto

Ejecutar el comando:

mvn clean compile

### 3. Ejecutar la aplicación

Ejecutar el comando:

mvn spring-boot:run

La aplicación estará disponible en: http://localhost:8080

### 4. Credenciales de acceso

La API utiliza Basic Authentication:

User: admin

Password: admin1

### 5. Acceder a Swagger UI

http://localhost:8080/swagger-ui/index.html

### 6. Acceder a Consola H2

http://localhost:8080/h2-console

  JDBC URL: jdbc:h2:mem:bikerentaldb

  Username: sa

  Password: (vacío)

## Ejemplos de peticiones con Postman

(Tambien puedes usar swagger para verificar las peticiones)

https://bike-rental-api-cqba.onrender.com/swagger-ui/index.html

-Abre Postman

-Click en Import → Raw text

-Copia y pega el JSON de la colección

{
  "info": {
    "name": "Bike Rental API",
    "description": "Colección para probar la API de alquiler de bicicletas",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    { "key": "baseUrl", "value": "http://localhost:8080" },
    { "key": "username", "value": "admin" },
    { "key": "password", "value": "admin1" }
  ],
  "item": [
    {
      "name": "Bicicletas",
      "item": [
        {
          "name": "Obtener disponibles",
          "request": {
            "method": "GET",
            "url": "{{baseUrl}}/api/bikes/available",
            "auth": { "type": "basic", "basic": [{"key":"username","value":"{{username}}"},{"key":"password","value":"{{password}}"}] }
          }
        },
        {
          "name": "Crear bicicleta",
          "request": {
            "method": "POST",
            "url": "{{baseUrl}}/api/bikes",
            "header": [{ "key": "Content-Type", "value": "application/json" }],
            "body": { "mode": "raw", "raw": "{\n  \"code\": \"BIC-010\",\n  \"type\": \"URBANA\",\n  \"status\": \"DISPONIBLE\"\n}" },
            "auth": { "type": "basic", "basic": [{"key":"username","value":"{{username}}"},{"key":"password","value":"{{password}}"}] }
          }
        },
        {
          "name": "Cambiar estado",
          "request": {
            "method": "PUT",
            "url": "{{baseUrl}}/api/bikes/BIC-001/status",
            "header": [{ "key": "Content-Type", "value": "application/json" }],
            "body": { "mode": "raw", "raw": "{\n  \"status\": \"EN_MANTENIMIENTO\"\n}" },
            "auth": { "type": "basic", "basic": [{"key":"username","value":"{{username}}"},{"key":"password","value":"{{password}}"}] }
          }
        },
        {
          "name": "Eliminar bicicleta",
          "request": {
            "method": "DELETE",
            "url": "{{baseUrl}}/api/bikes/BIC-004",
            "auth": { "type": "basic", "basic": [{"key":"username","value":"{{username}}"},{"key":"password","value":"{{password}}"}] }
          }
        }
      ]
    },
    {
      "name": "Alquileres",
      "item": [
        {
          "name": "Iniciar alquiler",
          "request": {
            "method": "POST",
            "url": "{{baseUrl}}/api/rentals/start",
            "header": [{ "key": "Content-Type", "value": "application/json" }],
            "body": { "mode": "raw", "raw": "{\n  \"bikeCode\": \"BIC-001\",\n  \"clientName\": \"Juan Pérez\",\n  \"estimatedHours\": 2\n}" },
            "auth": { "type": "basic", "basic": [{"key":"username","value":"{{username}}"},{"key":"password","value":"{{password}}"}] }
          }
        },
        {
          "name": "Finalizar alquiler",
          "request": {
            "method": "PUT",
            "url": "{{baseUrl}}/api/rentals/{{rentalId}}/end",
            "header": [{ "key": "Content-Type", "value": "application/json" }],
            "body": { "mode": "raw", "raw": "{\n  \"endTime\": \"2026-06-13T13:50:00\"\n}" },
            "auth": { "type": "basic", "basic": [{"key":"username","value":"{{username}}"},{"key":"password","value":"{{password}}"}] }
          }
        },
        {
          "name": "Historial",
          "request": {
            "method": "GET",
            "url": "{{baseUrl}}/api/rentals/history/BIC-001",
            "auth": { "type": "basic", "basic": [{"key":"username","value":"{{username}}"},{"key":"password","value":"{{password}}"}] }
          }
        }
      ]
    }
  ]
}

-Click en Continue → Import

En authorization completar los campos:

username	-> admin	
password	-> admin1

Los datos de referencia para las pruebas cargados en la base de datos son:

Código  Tipo   Estado inicial

BIC-001 URBANA Disponible

BIC-002 MONTAÑA Disponible

BIC-003 ELÉCTRICA Disponible

BIC-004 MONTAÑA En_mantenimiento

BIC-005 URBANA Disponible

## Ejecución de Tests

Ejecutar el sigiente comando en la raiz del proyecto o en el editor del IDE:

mvn test

Los tests incluyen:

PricingServiceTest	-> Cálculo de costos, redondeo y multas.

RentalServiceTest	-> Inicio/finalización de alquileres, validaciones.

BikeServiceTest	-> CRUD de bicicletas, validaciones de estado.

Total	21
