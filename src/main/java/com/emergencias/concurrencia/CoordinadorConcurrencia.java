package com.emergencias.concurrencia;

import java.util.concurrent.*;
import com.emergencias.comunicacion.CanalComunicacion;
import com.emergencias.monitoreo.MonitorSistema;
import com.emergencias.recursos.GestorRecursos;
import com.emergencias.servicios.*;

public class CoordinadorConcurrencia {
    private final ExecutorService ejecutorDespachadores;
    private final ScheduledExecutorService ejecutorMonitor;

    public CoordinadorConcurrencia(ServicioEmergencias servicio, GestorRecursos recursos,
                                   CanalComunicacion canal) {
        ejecutorDespachadores = Executors.newFixedThreadPool(3);
        ejecutorMonitor = Executors.newSingleThreadScheduledExecutor();

        for (int i = 1; i <= 3; i++) {
            ejecutorDespachadores.submit(new Despachador(servicio, recursos, canal));
        }
        ejecutorMonitor.scheduleAtFixedRate(
                new MonitorSistema(recursos, servicio), 0, 3, TimeUnit.SECONDS);
    }

    public void apagar() {
        ejecutorDespachadores.shutdownNow();
        ejecutorMonitor.shutdownNow();
        try {
            ejecutorDespachadores.awaitTermination(3, TimeUnit.SECONDS);
            ejecutorMonitor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
