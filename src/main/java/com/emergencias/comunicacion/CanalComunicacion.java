package com.emergencias.comunicacion;

import java.time.LocalTime;

/** Canal compartido de mensajes entre operador, despacho, ambulancia y hospital. */
public class CanalComunicacion {
    public synchronized void publicar(String emisor, String destinatario, String mensaje) {
        System.out.printf("[%s] %s -> %s: %s%n",
                LocalTime.now().withNano(0), emisor, destinatario, mensaje);
    }
}
