package com.juego.ppt.model;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PredictorJugadaTest {
    private final PredictorJugada predictor = new PredictorJugada();

    @Test
    void sinHistorialNoPredice() {
        assertTrue(predictor.predecir().isEmpty());

        predictor.registrar(Jugada.PIEDRA);
        assertTrue(predictor.predecir().isEmpty());
    }

    @Test
    void prediceLaTransicionMasFrecuente() {
        predictor.registrar(Jugada.PIEDRA);
        predictor.registrar(Jugada.TIJERA);
        predictor.registrar(Jugada.PIEDRA);
        predictor.registrar(Jugada.TIJERA);

        assertEquals(Optional.of(Jugada.PIEDRA), predictor.predecir());
    }

    @Test
    void noPrediceCuandoHayEmpate() {
        predictor.registrar(Jugada.PIEDRA);
        predictor.registrar(Jugada.TIJERA);
        predictor.registrar(Jugada.PIEDRA);
        predictor.registrar(Jugada.PAPEL);
        predictor.registrar(Jugada.PIEDRA);

        assertTrue(predictor.predecir().isEmpty());
    }

    @Test
    void respetaLaVentanaDeLasUltimasDiezJugadas() {
        predictor.registrar(Jugada.PIEDRA);
        predictor.registrar(Jugada.TIJERA);
        predictor.registrar(Jugada.PIEDRA);
        assertEquals(Optional.of(Jugada.TIJERA), predictor.predecir());

        for (int i = 0; i < PredictorJugada.VENTANA; i++) {
            predictor.registrar(Jugada.PIEDRA);
        }

        assertEquals(Optional.of(Jugada.PIEDRA), predictor.predecir());
    }
}
