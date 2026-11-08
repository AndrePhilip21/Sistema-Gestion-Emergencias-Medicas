package com.emergencias.monitoreo;

import com.emergencias.recursos.GestorRecursos;
import com.emergencias.servicios.ServicioEmergencias;

public class MonitorSistema implements Runnable {
    private final GestorRecursos recursos;
    private final ServicioEmergencias servicio;

    public MonitorSistema(GestorRecursos recursos, ServicioEmergencias servicio) {
        this.recursos = recursos;
        this.servicio = servicio;
    }

    @Override
    public void run() {
        System.out.println("\n========== MONITOREO DEL SISTEMA ==========");
        System.out.println("Emergencias pendientes: " + servicio.cantidadPendientes());
        System.out.println("Ambulancias disponibles: " + recursos.contarAmbulanciasDisponibles());
        System.out.println("Médicos disponibles: " + recursos.contarMedicosDisponibles());
        System.out.println("Equipos disponibles: " + recursos.contarEquiposDisponibles());
        System.out.println("Cupos hospitalarios: " + recursos.contarCuposHospitalarios());
        System.out.println("===========================================\n");
    }
}
