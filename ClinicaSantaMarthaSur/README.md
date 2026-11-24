# Clínica Santa Martha Sur – Diseño de Patrones y SOLID

Sistema de consola para registrar pacientes, calcular consultas (con extras y convenios), emitir comprobantes y cerrar caja.

## Estructura
```
interfaces/   IConsulta, IRepo<T>
modelo/       Paciente, Atencion, Servicio (enum)
decorator/    ConsultaBase, ServicioExtra, ExamenLab, RayosX      (ESTRUCTURAL)
strategy/     IDescuento, DescParticular, DescEPS                  (COMPORTAMIENTO)
factory/      ComprobanteCreator, BoletaCreator, FacturaCreator,
              IComprobantes, Boleta, Factura                       (CREACIONAL)
repositorio/  PacienteRepo
servicio/     AtencionService, Validador, ReporteCaja   (lógica de negocio)
vista/        ConsolaClinica                            (única clase con Scanner/println)
main/         Main                                      (raíz de composición)
```

## Patrones

### Creacional – Factory Method (`docs/uml_factory.png`)
- **Problema:** antes, `Main` hacía `new Factura()` / `new Boleta()` con un ternario; cada nuevo comprobante obligaba a editar el menú y el flujo de cobro.
- **Solución:** `ComprobanteCreator` define el método fábrica `crearComprobante()`; `BoletaCreator` y `FacturaCreator` deciden qué producto instanciar. El servicio solo conoce `IComprobantes`.
- **Mejora:** agregar "Nota de venta" = una clase `Creator` + una clase producto + una línea en `Main`.

### Estructural – Decorator (`docs/uml_decorator.png`)
- **Problema:** la consulta puede combinar extras (laboratorio, rayos X…). Con herencia habría una clase por combinación (`ConsultaConLab`, `ConsultaConLabYRayosX`…).
- **Solución:** `ServicioExtra` envuelve una `IConsulta` y suma descripción y costo. Se apilan en tiempo de ejecución: `new RayosX(new ExamenLab(new ConsultaBase(s)))`.
- **Mejora:** se agregó `RayosX` sin modificar ninguna clase existente.

### Comportamiento – Strategy (`docs/uml_strategy.png`)
- **Problema:** el descuento depende del convenio (Particular 0 %, EPS 20 %). Antes la calculadora decidía con `(opcion == 2) ? new DescEPS() : new DescParticular()`.
- **Solución:** `IDescuento` encapsula el algoritmo; `AtencionService.calcularTotal(subtotal, IDescuento)` recibe la estrategia ya elegida. El menú de convenios se arma con el mapa registrado en `Main`, usando `getNombre()`.
- **Mejora:** un convenio nuevo = una clase + una línea en `Main`.

## SOLID – antes y después

| Principio | Antes | Ahora |
|---|---|---|
| **S** | `Main` hacía menú, validación, cálculo y reporte; `CalculadoraClinica` mezclaba tarifas, decorators, descuentos e IGV; `Paciente` guardaba datos y estado de cobro. | `ConsolaClinica` (solo E/S), `AtencionService` (negocio), `Validador`, `ReporteCaja`, `Servicio` (tarifas), `Atencion` (cobro). `CalculadoraClinica` desapareció. Los comprobantes devuelven `String` en vez de imprimir. |
| **O** | `if (servicio == "Pediatría") … else if …` para precios; ternarios para elegir descuento y comprobante. | Precios en `enum Servicio`; convenios y comprobantes en mapas registrados en `Main`. Extender no modifica código existente. |
| **L** | Ya se cumplía. | Cualquier `IConsulta`, `IDescuento` o `IComprobantes` sustituye a su interfaz sin romper al cliente. `IRepo.buscar(clave)` documenta que busca por DNI (antes el parámetro se llamaba `id`). |
| **I** | Interfaces pequeñas. | Se mantienen: `IConsulta` (2 métodos), `IDescuento` (2), `IComprobantes` (2), `IRepo` (3). Ningún cliente depende de métodos que no usa. |
| **D** | `Main` hacía `new PacienteRepo()` y `new CalculadoraClinica()`; la calculadora hacía `new ExamenLab(...)`, `new DescEPS()`. | `AtencionService` recibe por constructor `IRepo`, `Map<Integer, IDescuento>` y `Map<Integer, ComprobanteCreator>`. `Main` es la única raíz de composición. |

### Ejemplo de código (S + O + D)
**Antes**
```java
// CalculadoraClinica
public double obtenerCostoBase(String servicio) {
    if (servicio.equalsIgnoreCase("Pediatría")) return 100.0;
    else if (servicio.equalsIgnoreCase("Cardiología")) return 150.0;
    return 80.0;
}
IDescuento desc = (opcionConvenio == 2) ? new DescEPS() : new DescParticular();
```
**Después**
```java
// Servicio.java: agregar una especialidad = una constante
PEDIATRIA("Pediatría", 100.0)

// Main.java: agregar un convenio = una línea
convenios.put(2, new DescEPS());
AtencionService service = new AtencionService(repo, convenios, comprobantes);
```

## Otras correcciones
- Entradas numéricas validadas (antes una letra lanzaba `InputMismatchException`).
- Factura mostraba "RUC / Paciente" con el nombre; ahora dice "Cliente".
- IGV calculado en un solo lugar (`Factura`); se eliminó el código duplicado e inutilizado de la calculadora.
- Montos con formato `S/ 0.00`.
- `listar()` devuelve una lista no modificable.
