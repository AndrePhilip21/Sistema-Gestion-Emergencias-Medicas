package com.emergencias.modelo;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Emergencia {

    private final String id;
    private final String descripcion;
    private final Gravedad gravedad;
    private final String ubicacion;
    private final double latitud;
    private final double longitud;
    private final Instant fechaRecepcion;

    private final AtomicReference<EstadoEmergencia> estado;

    public Emergencia(String descripcion, Gravedad gravedad,
                      String ubicacion, double latitud,
                      double longitud) {

        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException(
                "La descripcion no puede estar vacia."
            );
        }

        if (ubicacion == null || ubicacion.isBlank()) {
            throw new IllegalArgumentException(
                "La ubicacion no puede estar vacia."
            );
        }

        this.id = UUID.randomUUID().toString();
        this.descripcion = descripcion;
        this.gravedad = Objects.requireNonNull(gravedad);
        this.ubicacion = ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaRecepcion = Instant.now();

        this.estado = new AtomicReference<>(
            EstadoEmergencia.PENDIENTE
        );
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Gravedad getGravedad() {
        return gravedad;
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

    public Instant getFechaRecepcion() {
        return fechaRecepcion;
    }

    public EstadoEmergencia getEstado() {
        return estado.get();
    }

    public boolean cambiarEstado(
            EstadoEmergencia estadoActual,
            EstadoEmergencia nuevoEstado) {

        return estado.compareAndSet(
            estadoActual,
            nuevoEstado
        );
    }

    @Override
    public String toString() {
        return "Emergencia{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", gravedad=" + gravedad +
                ", ubicacion='" + ubicacion + '\'' +
                ", estado=" + estado.get() +
                '}';
    }
}
