package com.juego.ppt.model;

public class JugadorMaquina extends Jugador {
    private final GeneradorJugada generador;

    private Jugada rival;
    private int nivel;

    public JugadorMaquina(String nombre, GeneradorJugada generador) {
        super(nombre);
        this.generador = generador;
    }

    public void preparar(Jugada rival, int nivel) {
        this.rival = rival;
        this.nivel = nivel;
    }

    @Override
    public Jugada elegirJugada() {
        return generador.generar(rival, nivel);
    }
}
