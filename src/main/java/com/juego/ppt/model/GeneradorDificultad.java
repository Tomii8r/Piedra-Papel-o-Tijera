package com.juego.ppt.model;

import java.util.Optional;
import java.util.Random;

public class GeneradorDificultad implements GeneradorJugada {
    private static final double MINIMO_CONTRA_PATRON = 0.50;

    private final GeneradorJugada azar;
    private final Dificultad dificultad;
    private final Random random;
    private final PredictorJugada predictor = new PredictorJugada();

    public GeneradorDificultad(GeneradorJugada azar, Dificultad dificultad, Random random) {
        this.azar = azar;
        this.dificultad = dificultad;
        this.random = random;
    }

    @Override
    public Jugada generar(Jugada jugadaHumano, int nivel) {
        Optional<Jugada> predicha = predictor.predecir();

        Jugada respuesta;
        if (predicha.isEmpty()) {
            respuesta = azar.generar(jugadaHumano, nivel);
        } else {
            double probabilidadContra =
                Math.max(dificultad.probabilidadContra(nivel), MINIMO_CONTRA_PATRON);
            if (random.nextDouble() < probabilidadContra) {
                respuesta = predicha.get().vencedora();
            } else {
                respuesta = azar.generar(jugadaHumano, nivel);
            }
        }

        predictor.registrar(jugadaHumano);
        return respuesta;
    }
}
