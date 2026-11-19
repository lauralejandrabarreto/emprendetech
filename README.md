# EmprendeTech – PMV de Pedidos y Pagos

Producto Mínimo Viable del **Reto 4 – Arquitectura de E-commerce para Emprendedores Locales de Bogotá**.

## Propósito

El PMV implementa el componente crítico de la arquitectura descrita en el SAD: los **módulos de Pedidos y Pagos** del monolito modular, organizados con *Clean Architecture* y comunicados mediante eventos internos.

Cubre dos requerimientos funcionales:

- **RF5 – Pagos con varias pasarelas:** Nequi, Daviplata, tarjeta y efectivo contraentrega.
- **RF6 – Estados del pedido:** pendiente → pagado → enviado → entregado.

> Las pasarelas de pago son **simuladas**: no se conectan a los servicios reales de Nequi, Daviplata ni a un procesador de tarjetas. Cada una imita una API distinta para evidenciar el patrón Adapter.

## Tecnología

- Java 21
- Spring Boot 4.1.2 (Spring Web, Spring Data JPA, Validation)
- H2 en memoria (en producción, según el SAD, se usaría PostgreSQL)

## Cómo ejecutar

Requisitos: JDK 21. No es necesario instalar Maven, el proyecto incluye `mvnw`.

```bash
# Ejecutar la aplicación (Windows)
.\mvnw spring-boot:run

# Ejecutar la aplicación (Linux / macOS)
./mvnw spring-boot:run

# Ejecutar las pruebas
.\mvnw test
```

La API queda disponible en `http://localhost:8080`.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/pedidos` | Crea un pedido en estado PENDIENTE |
| GET | `/api/pedidos/{id}` | Consulta un pedido |
| PATCH | `/api/pedidos/{id}/enviar` | Cambia el pedido a ENVIADO |
| PATCH | `/api/pedidos/{id}/entregar` | Cambia el pedido a ENTREGADO |
| POST | `/api/pagos` | Procesa un pago con el método elegido |
| POST | `/api/pagos/webhook` | Simula la confirmación de la pasarela |

## Flujo de prueba

El archivo `demo-peticiones.http` contiene el flujo completo, listo para ejecutar con REST Client (VS Code) o IntelliJ:

1. Crear un pedido → `PENDIENTE`
2. Pagar con Nequi → el pago queda `PENDIENTE_CONFIRMACION` y devuelve una referencia
3. Intentar enviar el pedido sin pagar → **409 Conflict** (lo bloquea el patrón State)
4. Confirmar el pago por webhook con la referencia del paso 2 → pago `APROBADO`
5. Consultar el pedido → `PAGADO` (lo actualizó el módulo Pedidos al recibir el evento)
6. Enviar → `ENVIADO`
7. Entregar → `ENTREGADO`

## Estructura del proyecto

```
src/main/java/com/emprendetech/emprendetech
├── pedidos
│   ├── domain           Pedido y sus estados (sin dependencias de Spring ni JPA)
│   ├── application      ServicioPedidos, puerto PedidoRepositorio, PagoConfirmadoSuscriptor
│   ├── infrastructure   Persistencia con JPA
│   └── presentation     PedidoController (REST)
├── pagos
│   ├── domain           Pago, MetodoPago, EstadoPago
│   ├── application      ServicioPagos, EstrategiaPago, puerto PagoRepositorio
│   ├── infrastructure   Adaptadores de pasarelas, pasarelas simuladas y persistencia JPA
│   └── presentation     PagoController (REST)
└── shared
    ├── eventos          PublicadorEventos, SuscriptorEvento, eventos de dominio
    ├── errores          Excepciones de negocio compartidas
    └── web              Manejo de errores HTTP
```

Las dependencias apuntan hacia adentro: `presentation → application → domain`, e `infrastructure` implementa los puertos definidos en `application`.

## Patrones evidenciados

| Patrón | Dónde | Qué resuelve |
|---|---|---|
| **State** | `pedidos/domain/estados/` (`EstadoPedido`, `Pendiente`, `Pagado`, `Enviado`, `Entregado`) | Cada estado define qué transiciones permite; las inválidas lanzan `TransicionInvalidaException` (RF6) |
| **Strategy** | `pagos/application/EstrategiaPago` y `ServicioPagos` | Selecciona la forma de pago según `MetodoPago` sin condicionales por pasarela (RF5, RNF5) |
| **Adapter** | `pagos/infrastructure/NequiAdapter`, `DaviplataAdapter`, `TarjetaAdapter` | Traduce la API propia de cada pasarela al contrato común `EstrategiaPago` (RNF5, RNF6) |
| **Observer** | `shared/eventos/PublicadorEventos` y `pedidos/application/PagoConfirmadoSuscriptor` | Pagos publica `PagoConfirmadoEvento` y Pedidos reacciona sin que los módulos se conozcan (RNF7) |

## Relación con el SAD

| Elemento del SAD | Cómo se refleja en el código |
|---|---|
| Estilo: monolito modular + Clean Architecture + eventos internos (sección 3.2) | Una sola aplicación, módulos `pedidos` y `pagos` con capas separadas y comunicación por `PublicadorEventos` |
| Diagrama de componentes C4 nivel 3 (vista de implementación) | Los módulos Pedidos, Pagos y Eventos internos corresponden a los paquetes del proyecto |
| Diagrama de clases C4 nivel 4 (vista lógica) | Las clases del diagrama son las mismas del código |
| Diagrama de estados del pedido (vista de procesos) | Implementado en `pedidos/domain/estados/` |
| Diagrama de secuencia del pago (vista de procesos) | Flujo `POST /api/pagos` → webhook → evento → pedido `PAGADO` |
| RNF6: no almacenar datos de tarjeta | `Pago` solo guarda la referencia de la pasarela; la tarjeta llega como token |

## Pruebas

- `EstadoPedidoTest`: valida las transiciones del patrón State **sin levantar Spring**, lo que demuestra que el dominio es independiente del framework.
- `FlujoCompraIntegracionTest`: recorre el flujo completo por la API REST (Strategy, Adapter, Observer y State juntos).

## Autores

- Laura Alejandra Barreto Niño
- Jesús Alberto Zepeda Zelaya

Fundación Universitaria Konrad Lorenz – 2026
