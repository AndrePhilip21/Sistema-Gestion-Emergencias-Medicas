# Sistema de Gestión de Emergencias Médicas

Proyecto académico de simulación concurrente desarrollado en Java 21 y Maven.

## Requisitos
- JDK 21 o superior.
- Maven 3.8 o superior.

## Ejecución
Desde la carpeta que contiene `pom.xml`:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.emergencias.Main" -Dexec.cleanupDaemonThreads=false
```

La simulación registra cuatro emergencias, activa tres despachadores concurrentes y ejecuta el monitor durante 30 segundos.

## Funcionalidades implementadas
- Cola `PriorityBlockingQueue`: primero se atiende la mayor gravedad; en empate, la emergencia más antigua.
- Tres despachadores en un `ExecutorService` (patrón Thread Pool).
- Selección de ambulancia disponible más cercana mediante distancia Haversine.
- Reserva atómica de ambulancias, médicos y equipos con `AtomicBoolean`.
- Reserva concurrente de cupos hospitalarios con `AtomicInteger` y compare-and-set.
- Canal de mensajes sincronizado entre operador, despachador, ambulancia, médico y hospital.
- Monitor periódico de emergencias y recursos disponibles.
- Reintento cuando temporalmente no hay recursos: la emergencia vuelve a la cola.

## Estructura
- `modelo`: entidades y estados del dominio.
- `recursos`: reserva y liberación sincronizada de recursos.
- `servicios`: cola priorizada y trabajadores despachadores.
- `concurrencia`: coordinación de hilos y monitor.
- `comunicacion`: canal de mensajes entre roles.
- `monitoreo`: estado periódico del sistema.

## Alcance académico
Es una simulación local. Las coordenadas, tiempos de atención, hospitales y comunicaciones son datos ficticios; no conecta con servicios médicos reales ni debe utilizarse para gestionar emergencias reales.

La prioridad de la cola combina gravedad y antigüedad (espera). La distancia se utiliza para seleccionar la ambulancia más cercana disponible al momento del despacho. No se presenta como un algoritmo de puntuación ponderada de los tres factores.
