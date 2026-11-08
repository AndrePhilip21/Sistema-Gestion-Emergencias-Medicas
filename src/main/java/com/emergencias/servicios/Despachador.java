package com.emergencias.servicios;

import com.emergencias.comunicacion.CanalComunicacion;
import com.emergencias.modelo.*;
import com.emergencias.recursos.GestorRecursos;

/** Trabajador concurrente que coordina la atención de una emergencia. */
public class Despachador implements Runnable {
    private final ServicioEmergencias servicio;
    private final GestorRecursos recursos;
    private final CanalComunicacion canal;

    public Despachador(ServicioEmergencias servicio, GestorRecursos recursos,
                       CanalComunicacion canal) {
        this.servicio = servicio;
        this.recursos = recursos;
        this.canal = canal;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Ambulancia ambulancia = null;
            Medico medico = null;
            EquipoMedico equipo = null;
            Hospital hospital = null;
            Emergencia emergencia = null;
            boolean completada = false;

            try {
                emergencia = servicio.obtenerSiguienteEmergencia();
                canal.publicar("Operador", "Despachador",
                        "Se recibe emergencia " + emergencia.getId()
                        + " (" + emergencia.getGravedad() + ")");

                ambulancia = recursos.asignarAmbulancia(
                        emergencia.getLatitud(), emergencia.getLongitud());
                medico = recursos.asignarMedico();
                equipo = recursos.asignarEquipo();
                hospital = recursos.asignarHospital();

                if (ambulancia == null || medico == null || equipo == null || hospital == null) {
                    canal.publicar("Despachador", "Operador",
                            "Recursos insuficientes; emergencia vuelve a la cola.");
                    servicio.registrarEmergencia(emergencia);
                    Thread.sleep(1500);
                    continue;
                }

                emergencia.cambiarEstado(EstadoEmergencia.PENDIENTE, EstadoEmergencia.ASIGNADA);
                canal.publicar("Despachador", "Ambulancia " + ambulancia.getId(),
                        "Unidad asignada; diríjase a " + emergencia.getUbicacion());
                canal.publicar("Despachador", "Médico " + medico.getNombre(),
                        "Personal asignado a " + emergencia.getId());
                canal.publicar("Ambulancia", "Hospital " + hospital.getNombre(),
                        "Se traslada paciente; equipo " + equipo.getId());

                System.out.println("\nEMERGENCIA ASIGNADA: " + emergencia.getId());
                System.out.println("Ambulancia: " + ambulancia.getId()
                        + " | Médico: " + medico.getNombre()
                        + " | Equipo: " + equipo.getId()
                        + " | Hospital: " + hospital.getNombre());

                Thread.sleep(2000);
                emergencia.cambiarEstado(EstadoEmergencia.ASIGNADA, EstadoEmergencia.EN_ATENCION);
                canal.publicar("Ambulancia", "Hospital " + hospital.getNombre(),
                        "Paciente en atención y traslado simulado.");
                System.out.println("Emergencia en atención: " + emergencia.getId());

                Thread.sleep(2000);
                emergencia.cambiarEstado(EstadoEmergencia.EN_ATENCION, EstadoEmergencia.FINALIZADA);
                canal.publicar("Hospital " + hospital.getNombre(), "Despachador",
                        "Recepción confirmada; atención finalizada para " + emergencia.getId());
                System.out.println("Emergencia finalizada: " + emergencia.getId());
                completada = true;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (RuntimeException e) {
                canal.publicar("Sistema", "Monitor",
                        "Fallo controlado en despacho: " + e.getMessage());
                if (emergencia != null && !completada
                        && emergencia.getEstado() == EstadoEmergencia.PENDIENTE) {
                    servicio.registrarEmergencia(emergencia);
                }
            } finally {
                recursos.liberarAmbulancia(ambulancia);
                recursos.liberarMedico(medico);
                recursos.liberarEquipo(equipo);
                recursos.liberarHospital(hospital);
            }
        }
    }
}
