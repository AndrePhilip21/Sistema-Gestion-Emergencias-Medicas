package com.emergencias.modelo;

import java.util.concurrent.atomic.AtomicBoolean;

/** Equipo médico compartido que puede reservarse por un solo despachador. */
public class EquipoMedico {
    private final String id;
    private final String descripcion;
    private final AtomicBoolean disponible = new AtomicBoolean(true);

    public EquipoMedico(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public boolean estaDisponible() { return disponible.get(); }
    public boolean ocupar() { return disponible.compareAndSet(true, false); }
    public void liberar() { disponible.set(true); }
}
