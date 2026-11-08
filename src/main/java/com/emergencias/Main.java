package com.emergencias;

import com.emergencias.comunicacion.CanalComunicacion;
import com.emergencias.concurrencia.CoordinadorConcurrencia;
import com.emergencias.modelo.*;
import com.emergencias.recursos.GestorRecursos;
import com.emergencias.servicios.ServicioEmergencias;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("     SISTEMA DE GESTION DE EMERGENCIAS");
        System.out.println("==============================================");

        GestorRecursos recursos = new GestorRecursos();
        recursos.agregarAmbulancia(new Ambulancia("AMB-001", "Centro", 6.337, -75.558));
        recursos.agregarAmbulancia(new Ambulancia("AMB-002", "Norte", 6.360, -75.560));
        recursos.agregarAmbulancia(new Ambulancia("AMB-003", "Sur", 6.310, -75.570));

        recursos.agregarMedico(new Medico("MED-001", "Carlos", "Urgencias"));
        recursos.agregarMedico(new Medico("MED-002", "Laura", "Traumatologia"));
        recursos.agregarMedico(new Medico("MED-003", "Andres", "Emergencias"));

        recursos.agregarEquipo(new EquipoMedico("EQ-001", "Botiquín y desfibrilador"));
        recursos.agregarEquipo(new EquipoMedico("EQ-002", "Equipo de trauma"));
        recursos.agregarEquipo(new EquipoMedico("EQ-003", "Equipo de soporte vital"));

        recursos.agregarHospital(new Hospital("Hospital Central", 2));
        recursos.agregarHospital(new Hospital("Hospital Norte", 2));

        ServicioEmergencias servicio = new ServicioEmergencias();
        servicio.registrarEmergencia(new Emergencia(
                "Accidente de transito", Gravedad.CRITICO,
                "Avenida principal", 6.337, -75.558));
        servicio.registrarEmergencia(new Emergencia(
                "Dolor fuerte en el pecho", Gravedad.GRAVE,
                "Barrio Norte", 6.360, -75.560));
        servicio.registrarEmergencia(new Emergencia(
                "Caida de una persona", Gravedad.MODERADO,
                "Barrio Sur", 6.310, -75.570));
        servicio.registrarEmergencia(new Emergencia(
                "Herida leve", Gravedad.LEVE,
                "Centro", 6.340, -75.550));

        CanalComunicacion canal = new CanalComunicacion();
        CoordinadorConcurrencia coordinador =
                new CoordinadorConcurrencia(servicio, recursos, canal);

        System.out.println("\nSistema iniciado. Despachadores concurrentes: 3");
        System.out.println("Duración de la simulación: 30 segundos.\n");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            coordinador.apagar();
            System.out.println("\nSistema de emergencias detenido.");
        }
    }
}
