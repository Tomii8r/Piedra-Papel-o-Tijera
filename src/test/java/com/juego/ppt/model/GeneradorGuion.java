package com.juego.ppt.model;

import java.util.ArrayDeque;
import java.util.Deque;

public class GeneradorGuion implements GeneradorJugada {
    private final Deque<Jugada> jugadas = new ArrayDeque<>();

    public GeneradorGuion(Jugada... jugadas) {
        for (Jugada jugada : jugadas) {
            this.jugadas.add(jugada);
        }
    }

    @Override
    public Jugada generar(Jugada jugadaHumano, int nivel) {
        if (jugadas.isEmpty()) {
            throw new IllegalStateException("El guion de jugadas se agotó");
        }
        return jugadas.poll();
    }
}
