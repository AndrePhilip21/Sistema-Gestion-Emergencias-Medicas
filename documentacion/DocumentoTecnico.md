# Documento técnico — Sistema de Gestión de Emergencias Médicas

## 1. Objetivo y alcance

El sistema simula la recepción, clasificación y atención concurrente de solicitudes de emergencia médica. Su objetivo académico es demostrar coordinación de tareas, exclusión mutua sobre recursos compartidos, comunicación entre roles y supervisión del estado de ejecución. La aplicación funciona localmente y utiliza datos ficticios; no constituye un sistema clínico ni una plataforma de despacho real.

La simulación registra cuatro incidentes de distinta gravedad. Tres trabajadores pueden procesarlos al mismo tiempo. Cada atención reserva una ambulancia, un médico, un equipo médico y un cupo hospitalario. Cuando termina la atención simulada, los recursos se liberan para que puedan ser utilizados por otra emergencia.

## 2. Arquitectura

La solución se organiza en paquetes:

- **modelo:** `Emergencia`, `Ambulancia`, `Medico`, `EquipoMedico`, `Hospital`, `Gravedad` y `EstadoEmergencia`.
- **servicios:** `ServicioEmergencias` mantiene la cola priorizada; `Despachador` consume emergencias y coordina la atención.
- **recursos:** `GestorRecursos` registra, asigna y libera los recursos compartidos.
- **concurrencia:** `CoordinadorConcurrencia` administra el grupo de despachadores y el hilo periódico de monitoreo.
- **comunicacion:** `CanalComunicacion` centraliza los mensajes entre operador, despachador, ambulancia, médico y hospital.
- **monitoreo:** `MonitorSistema` informa periódicamente las emergencias pendientes y la disponibilidad de recursos.

Flujo general: el operador registra la emergencia; la cola la ordena; un despachador la retira; el gestor reserva los recursos; los roles intercambian mensajes; se simulan las etapas de atención y traslado; finalmente se cambia el estado a finalizada y se liberan los recursos.

## 3. Concurrencia y sincronización

Se aplican tres mecanismos principales:

1. **Productor-consumidor:** `PriorityBlockingQueue` permite que el hilo principal registre emergencias y varios despachadores las consuman de forma segura. `take()` bloquea al trabajador cuando no hay elementos.
2. **Thread Pool:** `ExecutorService` administra tres despachadores sin crear manualmente un hilo por emergencia. Un `ScheduledExecutorService` ejecuta el monitor cada tres segundos.
3. **Operaciones atómicas:** `AtomicBoolean.compareAndSet` evita que dos despachadores reserven simultáneamente la misma ambulancia, médico o equipo. `Hospital` utiliza `AtomicInteger` y compare-and-set para no superar su capacidad.

Las listas de recursos utilizan `CopyOnWriteArrayList`, adecuada para una colección que se consulta con frecuencia y cambia poco durante la simulación. La liberación de recursos se realiza en un bloque `finally`, para que ocurra incluso si se interrumpe el trabajador.

## 4. Priorización y asignación

La cola ordena las emergencias por nivel de gravedad descendente: crítico, grave, moderado y leve. Cuando dos incidentes tienen la misma gravedad, se atiende primero el que fue recibido antes, incorporando el tiempo de espera como criterio de desempate.

La ubicación se utiliza al asignar la ambulancia: el gestor calcula la distancia geográfica aproximada entre cada ambulancia disponible y el incidente mediante la fórmula Haversine, y reserva la más cercana. Por tanto, la gravedad y la antigüedad determinan el orden de atención, mientras que la distancia determina qué ambulancia se envía. No se utiliza una puntuación ponderada única de los tres factores.

## 5. Comunicación, monitoreo y tolerancia a fallos

`CanalComunicacion` proporciona un punto común para emitir mensajes sincronizados entre los distintos roles. Esto evita que varias salidas de comunicación se mezclen en consola. El monitor informa cada tres segundos el número de emergencias pendientes, ambulancias, médicos y equipos disponibles, además de los cupos hospitalarios.

Si al retirar una emergencia no existe alguno de los recursos necesarios, los recursos que sí se habían reservado se liberan, la emergencia vuelve a la cola y el despachador espera brevemente antes de reintentar. Esto representa una indisponibilidad temporal. La aplicación también captura interrupciones y detiene los ejecutores ordenadamente. Para un sistema de producción se necesitarían persistencia, reintentos con límites, registro duradero, alertas y recuperación ante caída del proceso.

## 6. Pruebas y resultados esperados

La compilación se realiza con `mvn clean compile`. La ejecución se inicia con:

```bash
mvn exec:java -Dexec.mainClass="com.emergencias.Main" -Dexec.cleanupDaemonThreads=false
```

En una ejecución normal se espera observar cuatro emergencias registradas, hasta tres atenciones simultáneas, asignación de recursos, mensajes entre roles, cambios de estado y liberación final de recursos. Al terminar la simulación, el monitor debe reflejar los recursos disponibles nuevamente y cero emergencias pendientes.

La compilación del código actualizado se verificó con el compilador Java usando `--release 21`. La prueba de ejecución local también completó las cuatro atenciones simuladas. Para la entrega académica se recomienda adjuntar una captura de `BUILD SUCCESS` y una captura del monitor al final de la ejecución.

## 7. Conclusiones

El proyecto demuestra coordinación concurrente mediante una cola priorizada, un grupo fijo de trabajadores y recursos compartidos protegidos con operaciones atómicas. También separa la lógica de negocio, la gestión de recursos, la comunicación y el monitoreo en componentes independientes. La simulación es una base académica funcional y extensible; no reemplaza los protocolos, la infraestructura ni las validaciones de un sistema real de emergencias.
