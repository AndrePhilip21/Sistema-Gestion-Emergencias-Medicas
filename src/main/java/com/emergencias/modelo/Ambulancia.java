package com.emergencias.modelo;

import java.util.concurrent.atomic.AtomicBoolean;

public class Ambulancia {

    private final String id;
    private final String ubicacion;
    private final double latitud;
    private final double longitud;
    private final AtomicBoolean disponible;

    public Ambulancia(String id, String ubicacion,
                      double latitud, double longitud) {

        this.id = id;
        this.ubicacion = ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.disponible = new AtomicBoolean(true);
    }

    public String getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
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

    public double calcularDistancia(double latitudDestino,
                                    double longitudDestino) {

        final double radioTierra = 6371.0;

        double diferenciaLatitud =
                Math.toRadians(latitudDestino - latitud);

        double diferenciaLongitud =
                Math.toRadians(longitudDestino - longitud);

        double a = Math.sin(diferenciaLatitud / 2)
                * Math.sin(diferenciaLatitud / 2)
                + Math.cos(Math.toRadians(latitud))
                * Math.cos(Math.toRadians(latitudDestino))
                * Math.sin(diferenciaLongitud / 2)
                * Math.sin(diferenciaLongitud / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return radioTierra * c;
    }

    @Override
    public String toString() {
        return "Ambulancia{" +
                "id='" + id + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", disponible=" + disponible.get() +
                '}';
    }
}