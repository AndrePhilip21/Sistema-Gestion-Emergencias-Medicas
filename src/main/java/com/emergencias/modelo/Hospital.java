package com.emergencias.modelo;

import java.util.concurrent.atomic.AtomicInteger;

/** Hospital con capacidad limitada y reserva atómica de camas. */
public class Hospital {
    private final String nombre;
    private final int capacidad;
    private final AtomicInteger pacientesActuales = new AtomicInteger(0);

    public Hospital(String nombre, int capacidad) {
        if (nombre == null || nombre.isBlank() || capacidad <= 0) {
            throw new IllegalArgumentException("Hospital y capacidad deben ser válidos.");
        }
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public String getNombre() { return nombre; }
    public int getCapacidad() { return capacidad; }
    public int getPacientesActuales() { return pacientesActuales.get(); }
    public int getCuposDisponibles() { return capacidad - pacientesActuales.get(); }

    public boolean recibirPaciente() {
        while (true) {
            int actual = pacientesActuales.get();
            if (actual >= capacidad) return false;
            if (pacientesActuales.compareAndSet(actual, actual + 1)) return true;
        }
    }

    public void liberarCupo() {
        pacientesActuales.updateAndGet(actual -> Math.max(0, actual - 1));
    }
}
