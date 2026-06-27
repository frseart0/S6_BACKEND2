# Informe tecnico - Exp2 Semana 6 (contenido para Formato de respuesta)

Completar el PDF oficial del AVA con este contenido y adjuntar capturas de pantalla de la consola (`mvn test`) y del reporte JaCoCo (`target/site/jacoco/index.html`).

---

## Resumen tecnico del avance

En las semanas 4 y 5 se construyo la base del backend Minimarket Plus: entidades JPA (`Producto`, `Inventario`, `Venta`, `Usuario`, `Rol`), repositorios Spring Data, servicios y controladores REST. Esta semana se agrego Spring Security con autenticacion y autorizacion por roles (`ADMINISTRADOR`, `CAJERO`, `CLIENTE`), protegiendo operaciones criticas como la edicion de productos, movimientos de inventario y registro de ventas. Las pruebas unitarias validan que cada rol solo acceda a las operaciones permitidas.

---

## 1. Diseno y justificacion de las pruebas unitarias

| Entidad | Escenario | Rol / condicion | Resultado esperado | Justificacion |
|---------|-----------|-----------------|-------------------|---------------|
| Producto | Modificar producto | ADMINISTRADOR | 200 OK | Solo administradores deben gestionar catalogo |
| Producto | Modificar producto | CLIENTE | 403 Forbidden | Clientes no deben alterar precios o stock |
| Producto | Listar productos | CLIENTE autenticado | 200 OK | Consulta permitida para todos los autenticados |
| Inventario | Registrar movimiento | CAJERO / ADMINISTRADOR | 200 OK | Personal autorizado gestiona stock |
| Inventario | Registrar movimiento | CLIENTE | 403 Forbidden | Clientes no registran entradas/salidas |
| Venta | Crear venta | CAJERO | 200 OK | Solo cajeros procesan ventas en caja |
| Venta | Crear venta | CLIENTE | 403 Forbidden | Clientes no generan ventas directamente |
| Usuario | Credenciales validas | BCrypt correcto | Usuario cargado | Autenticacion segura |
| Usuario | Usuario inexistente | - | UsernameNotFoundException | Bloqueo de accesos invalidos |
| Usuario | Contrasena incorrecta | - | No coincide hash | Proteccion contra fuerza bruta |

---

## 2. Configuracion del entorno de pruebas

1. **Maven**: proyecto con `pom.xml`, wrapper `mvnw` incluido.
2. **Dependencias de test**: `spring-boot-starter-test` (JUnit 5 + Mockito), `spring-security-test`.
3. **JaCoCo**: plugin `jacoco-maven-plugin` 0.8.12 para reportes HTML/XML de cobertura.
4. **Estructura**:
   - `src/main/java` - codigo de produccion
   - `src/test/java` - pruebas unitarias
   - `src/test/resources/application-test.properties` - perfil H2 para tests
5. **Herramientas**: `@WebMvcTest` + MockMvc para seguridad HTTP; `@ExtendWith(MockitoExtension.class)` para servicios.

---

## 3. Resultados obtenidos y analisis de cobertura

**Comando ejecutado:** `./mvnw clean test`

**Resultado:** 22 pruebas ejecutadas, 0 fallos, BUILD SUCCESS.

**Cobertura:** revisar `target/site/jacoco/index.html` e incluir captura en el informe. Los paquetes `controller`, `security` y `service.impl` concentran la logica validada por las pruebas de esta actividad.

### Endpoints protegidos correctamente

- `PUT/POST/DELETE /api/productos` - solo ADMINISTRADOR
- `POST/PUT/DELETE /api/inventario` - ADMINISTRADOR o CAJERO
- `POST /api/ventas` - solo CAJERO
- `POST/PUT/DELETE /api/usuarios` - solo ADMINISTRADOR

### Ajustes realizados durante las pruebas

- Se agrego prefijo `ROLE_` en `CustomUserDetails` para compatibilidad con `hasRole()`.
- Se habilito `@EnableMethodSecurity` y anotaciones `@PreAuthorize` en controladores.
- Se implemento hash BCrypt en `UsuarioServiceImpl.save()`.
- Se agrego constructor vacio en entidad `Rol` para compatibilidad JPA.

---

## 4. Contribucion de las pruebas a la calidad del sistema

Las pruebas garantizan que las reglas de negocio de seguridad no se degraden con cambios futuros. Cada despliegue puede verificarse con `mvn test` antes de publicar. Mockito aísla servicios para probar logica de ventas e inventario sin base de datos. JaCoCo identifica codigo sin cobertura para priorizar nuevas pruebas.

---

## 5. Recomendaciones de mejora

1. **JWT**: implementar `JwtUtil` para APIs REST stateless en lugar de solo form login.
2. **Validacion**: usar `@Valid` y DTOs para no exponer entidades JPA directamente.
3. **Stock sincronizado**: actualizar `Producto.stock` automaticamente al registrar movimientos de inventario.
4. **Tests de integracion**: flujo completo carrito -> venta con `@SpringBootTest`.
5. **Ocultar contrasenas**: no devolver el campo password en respuestas JSON de `/api/usuarios`.
6. **Seed data**: script inicial con usuarios de prueba por rol para demostracion manual.

---

## Evidencias a adjuntar en el PDF

1. Captura de consola con `Tests run: 22, Failures: 0` y `BUILD SUCCESS`
2. Captura de `target/site/jacoco/index.html`
3. Enlace al repositorio GitHub en la seccion Entrega del AVA
