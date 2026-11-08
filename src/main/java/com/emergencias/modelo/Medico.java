package com.emergencias.modelo;

import java.util.concurrent.atomic.AtomicBoolean;

public class Medico {

    private final String id;
    private final String nombre;
    private final String especialidad;
    private final AtomicBoolean disponible;

    public Medico(String id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.disponible = new AtomicBoolean(true);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public boolean estaDisponible() {
        return disponible.get();
    }

    public boolean ocupar() {
        return disponible.compareAndSet(true, false);
    }

    public void liberar() {
        disponible.set(true);
    }

    @Override
    public String toString() {
        return "Medico{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", disponible=" + disponible.get() +
                '}';
    }
}