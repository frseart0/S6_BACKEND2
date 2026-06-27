# Minimarket Plus - Backend

Sistema backend REST para la gestion de un minimarket. Desarrollado con Spring Boot, incluye autenticacion, control de acceso por roles y pruebas unitarias con JUnit y Mockito.

## Requisitos

- Java 17 o superior
- Maven 3.9+ (incluye wrapper `./mvnw`)

## Ejecutar la aplicacion

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

Consola H2: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, usuario: `sa`, sin password).

## Roles y permisos


| Rol               | Permisos principales                                                                               |
| ----------------- | -------------------------------------------------------------------------------------------------- |
| **ADMINISTRADOR** | Crear, modificar y eliminar productos. Gestionar inventario. Consultar ventas. Gestionar usuarios. |
| **CAJERO**        | Registrar ventas. Registrar movimientos de inventario. Consultar ventas.                           |
| **CLIENTE**       | Consultar productos y demas recursos de lectura permitidos.                                        |




### Endpoints protegidos


| Recurso           | GET            | POST           | PUT            | DELETE         |
| ----------------- | -------------- | -------------- | -------------- | -------------- |
| `/api/productos`  | Autenticado    | ADMINISTRADOR  | ADMINISTRADOR  | ADMINISTRADOR  |
| `/api/inventario` | Autenticado    | ADMIN o CAJERO | ADMIN o CAJERO | ADMIN o CAJERO |
| `/api/ventas`     | ADMIN o CAJERO | CAJERO         | -              | -              |
| `/api/usuarios`   | Autenticado    | ADMINISTRADOR  | ADMINISTRADOR  | ADMINISTRADOR  |
| `/public/**`      | Publico        | -              | -              | -              |




## Pruebas unitarias

Ejecutar todas las pruebas:

```bash
./mvnw clean test
```

Generar reporte de cobertura JaCoCo:

```bash
./mvnw clean test jacoco:report
```

Reporte HTML: `target/site/jacoco/index.html`

Reportes Surefire: `target/surefire-reports/`

### Estructura de pruebas

```
src/test/java/com/minimarket/
├── security/
│   ├── ProductoSecurityTest.java      # Autorizacion en productos
│   ├── InventarioSecurityTest.java    # Permisos de inventario
│   ├── VentaSecurityTest.java         # Solo cajeros crean ventas
│   └── UsuarioAuthenticationTest.java # Autenticacion valida/invalida
├── service/
│   ├── InventarioServiceTest.java     # Movimientos con Mockito
│   └── VentaServiceTest.java          # Detalle de ventas con Mockito
├── UsuarioTest.java                   # Entidad Usuario
└── MinimarketApplicationTests.java    # Contexto Spring
```



## Estructura del proyecto

```
src/main/java/com/minimarket/
├── controller/     # REST API
├── entity/         # Modelo JPA
├── repository/     # Spring Data JPA
├── service/        # Logica de negocio
└── security/       # Spring Security (auth, roles)
```



## Autenticacion

La aplicacion usa Spring Security con login por formulario y contrasenas BCrypt. Los roles se almacenan en la entidad `Rol` y se mapean a authorities con prefijo `ROLE_`.

