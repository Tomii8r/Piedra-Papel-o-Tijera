package com.juego.ppt.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class PredictorJugada {
    public static final int VENTANA = 10;

    private final Deque<Jugada> historial = new ArrayDeque<>();

    public void registrar(Jugada jugada) {
        historial.addLast(jugada);
        if (historial.size() > VENTANA) {
            historial.removeFirst();
        }
    }

    public Optional<Jugada> predecir() {
        if (historial.size() < 2) {
            return Optional.empty();
        }
        Jugada contexto = historial.peekLast();
        int[] conteo = new int[Jugada.values().length];
        Jugada anterior = null;
        for (Jugada jugada : historial) {
            if (anterior == contexto) {
                conteo[jugada.ordinal()]++;
            }
            anterior = jugada;
        }

        int maximo = 0;
        int elegida = -1;
        boolean empate = false;
        for (int i = 0; i < conteo.length; i++) {
            if (conteo[i] > maximo) {
                maximo = conteo[i];
                elegida = i;
                empate = false;
            } else if (conteo[i] == maximo && conteo[i] > 0) {
                empate = true;
            }
        }

        if (maximo == 0 || empate) {
            return Optional.empty();
        }
        return Optional.of(Jugada.values()[elegida]);
    }
}
