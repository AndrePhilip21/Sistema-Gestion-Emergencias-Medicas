package com.emergencias.modelo;

public enum Gravedad {

    CRITICO(4),
    GRAVE(3),
    MODERADO(2),
    LEVE(1);

    private final int nivelPrioridad;

    Gravedad(int nivelPrioridad) {
        this.nivelPrioridad = nivelPrioridad;
    }

    public int getNivelPrioridad() {
        return nivelPrioridad;
    }
}