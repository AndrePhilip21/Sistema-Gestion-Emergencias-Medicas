package com.emergencias.recursos;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.emergencias.modelo.Ambulancia;
import com.emergencias.modelo.EquipoMedico;
import com.emergencias.modelo.Hospital;
import com.emergencias.modelo.Medico;

public class GestorRecursos {
    private final List<Ambulancia> ambulancias = new CopyOnWriteArrayList<>();
    private final List<Medico> medicos = new CopyOnWriteArrayList<>();
    private final List<EquipoMedico> equipos = new CopyOnWriteArrayList<>();
    private final List<Hospital> hospitales = new CopyOnWriteArrayList<>();

    public void agregarAmbulancia(Ambulancia recurso) { ambulancias.add(recurso); }
    public void agregarMedico(Medico recurso) { medicos.add(recurso); }
    public void agregarEquipo(EquipoMedico recurso) { equipos.add(recurso); }
    public void agregarHospital(Hospital recurso) { hospitales.add(recurso); }

    /** Selecciona la ambulancia disponible más cercana y la reserva atómicamente. */
    public Ambulancia asignarAmbulancia(double latitud, double longitud) {
        Ambulancia candidata = null;
        double menorDistancia = Double.MAX_VALUE;
        for (Ambulancia ambulancia : ambulancias) {
            if (ambulancia.estaDisponible()) {
                double distancia = ambulancia.calcularDistancia(latitud, longitud);
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    candidata = ambulancia;
                }
            }
        }
        return candidata != null && candidata.ocupar() ? candidata : null;
    }

    public Medico asignarMedico() {
        for (Medico medico : medicos) if (medico.ocupar()) return medico;
        return null;
    }

    public EquipoMedico asignarEquipo() {
        for (EquipoMedico equipo : equipos) if (equipo.ocupar()) return equipo;
        return null;
    }

    public Hospital asignarHospital() {
        for (Hospital hospital : hospitales) if (hospital.recibirPaciente()) return hospital;
        return null;
    }

    public void liberarAmbulancia(Ambulancia r) { if (r != null) r.liberar(); }
    public void liberarMedico(Medico r) { if (r != null) r.liberar(); }
    public void liberarEquipo(EquipoMedico r) { if (r != null) r.liberar(); }
    public void liberarHospital(Hospital r) { if (r != null) r.liberarCupo(); }

    public int contarAmbulanciasDisponibles() {
        return (int) ambulancias.stream().filter(Ambulancia::estaDisponible).count();
    }
    public int contarMedicosDisponibles() {
        return (int) medicos.stream().filter(Medico::estaDisponible).count();
    }
    public int contarEquiposDisponibles() {
        return (int) equipos.stream().filter(EquipoMedico::estaDisponible).count();
    }
    public int contarCuposHospitalarios() {
        return hospitales.stream().mapToInt(Hospital::getCuposDisponibles).sum();
    }
}
