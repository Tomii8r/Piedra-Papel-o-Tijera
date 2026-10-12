package com.juego.ppt.model;

public class JugadorHumano extends Jugador {
    private Jugada jugada;

    public JugadorHumano(String nombre) {
        super(nombre);
    }

    public void setJugada(Jugada jugada) {
        this.jugada = jugada;
    }

    @Override
    public Jugada elegirJugada() {
        return jugada;
    }
}
