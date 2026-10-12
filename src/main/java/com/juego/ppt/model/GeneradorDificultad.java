package com.juego.ppt.model;

import java.util.Random;

public class GeneradorDificultad implements GeneradorJugada {
    private final GeneradorJugada azar;
    private final Dificultad dificultad;
    private final Random random;

    private Jugada jugadaAnteriorHumano;

    public GeneradorDificultad(GeneradorJugada azar, Dificultad dificultad, Random random) {
        this.azar = azar;
        this.dificultad = dificultad;
        this.random = random;
    }

    @Override
    public Jugada generar(Jugada jugadaHumano, int nivel) {
        Jugada respuesta;
        if (jugadaAnteriorHumano == null) {
            respuesta = azar.generar(jugadaHumano, nivel);
        } else {
            double probabilidadContra = dificultad.probabilidadContra(nivel);
            double probabilidadPierde = dificultad.probabilidadPierde(nivel);
            double tirada = random.nextDouble();

            if (tirada < probabilidadContra) {
                respuesta = jugadaAnteriorHumano.vencedora();
            } else if (tirada < probabilidadContra + probabilidadPierde) {
                respuesta = jugadaAnteriorHumano.perdedora();
            } else {
                respuesta = azar.generar(jugadaHumano, nivel);
            }
        }
        jugadaAnteriorHumano = jugadaHumano;
        return respuesta;
    }
}
