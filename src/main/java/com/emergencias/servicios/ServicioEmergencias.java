package com.emergencias.servicios;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import com.emergencias.modelo.Emergencia;

/**
 * Productor-consumidor: la cola prioriza gravedad (mayor primero) y,
 * en igualdad de gravedad, atiende primero la emergencia más antigua.
 */
public class ServicioEmergencias {
    private final PriorityBlockingQueue<Emergencia> colaEmergencias =
        new PriorityBlockingQueue<>(20,
            Comparator.comparingInt((Emergencia e) -> e.getGravedad().getNivelPrioridad())
                      .reversed()
                      .thenComparing(Emergencia::getFechaRecepcion));

    public void registrarEmergencia(Emergencia emergencia) {
        colaEmergencias.put(emergencia);
        System.out.println("Emergencia registrada: " + emergencia.getId()
                + " | Gravedad: " + emergencia.getGravedad());
    }

    public Emergencia obtenerSiguienteEmergencia() throws InterruptedException {
        return colaEmergencias.take();
    }
    public int cantidadPendientes() { return colaEmergencias.size(); }
    public boolean hayEmergenciasPendientes() { return !colaEmergencias.isEmpty(); }
}
