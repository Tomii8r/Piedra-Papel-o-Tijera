package com.juego.ppt.model;

public abstract class Jugador {
    protected final String nombre;

    protected Jugador(String nombre) {
        this.nombre = nombre;
    }

    public abstract Jugada elegirJugada();

    public String getNombre() {
        return nombre;
    }
}
