package com.juego.ppt.model;

import java.util.Random;

public class GeneradorRandom implements GeneradorJugada {
    private final Random random;

    public GeneradorRandom(Random random) {
        this.random = random;
    }

    @Override
    public Jugada generar(Jugada jugadaHumano, int nivel) {
        Jugada[] opciones = Jugada.values();
        return opciones[random.nextInt(opciones.length)];
    }
}
