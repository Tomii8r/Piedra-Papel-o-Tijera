package com.juego.ppt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneradorDificultadTest {
    private static final int NIVEL_CERO = 0;

    private final Dificultad dificultad = new Dificultad();

    private GeneradorDificultad generadorCon(double tirada, Jugada resultadoAzar) {
        GeneradorJugada azarFijo = (rival, nivel) -> resultadoAzar;
        return new GeneradorDificultad(azarFijo, dificultad, new RandomFijo(tirada));
    }

    private void sembrar(GeneradorDificultad generador, int nivel, Jugada... jugadas) {
        for (Jugada jugada : jugadas) {
            generador.generar(jugada, nivel);
        }
    }

    @Test
    void sinPrediccionJuegaAlAzar() {
        GeneradorDificultad generador = generadorCon(0.0, Jugada.PAPEL);

        assertEquals(Jugada.PAPEL, generador.generar(Jugada.PIEDRA, NIVEL_CERO));
    }

    @Test
    void contrarrestaLaJugadaPredicha() {
        GeneradorDificultad generador = generadorCon(0.0, Jugada.TIJERA);
        sembrar(generador, NIVEL_CERO, Jugada.PIEDRA, Jugada.TIJERA, Jugada.PIEDRA, Jugada.TIJERA);

        Jugada respuesta = generador.generar(Jugada.PIEDRA, NIVEL_CERO);

        assertEquals(Jugada.PAPEL, respuesta);
    }

    @Test
    void alRepetirLaMismaJugadaLaContrarrestaDesdeElNivelCero() {
        GeneradorDificultad generador = generadorCon(0.0, Jugada.TIJERA);
        sembrar(generador, NIVEL_CERO, Jugada.PIEDRA, Jugada.PIEDRA);

        Jugada respuesta = generador.generar(Jugada.PIEDRA, NIVEL_CERO);

        assertEquals(Jugada.PAPEL, respuesta);
    }

    @Test
    void juegaAlAzarCuandoLaTiradaSuperaElUmbralDePatron() {
        GeneradorDificultad generador = generadorCon(0.6, Jugada.TIJERA);
        sembrar(generador, NIVEL_CERO, Jugada.PIEDRA, Jugada.TIJERA, Jugada.PIEDRA, Jugada.TIJERA);

        Jugada respuesta = generador.generar(Jugada.PIEDRA, NIVEL_CERO);

        assertEquals(Jugada.TIJERA, respuesta);
    }

    @Test
    void sinPrediccionPorEmpateJuegaAlAzar() {
        GeneradorDificultad generador = generadorCon(0.0, Jugada.TIJERA);
        sembrar(generador, NIVEL_CERO,
            Jugada.PIEDRA, Jugada.TIJERA, Jugada.PIEDRA, Jugada.PAPEL, Jugada.PIEDRA);

        Jugada respuesta = generador.generar(Jugada.PIEDRA, NIVEL_CERO);

        assertEquals(Jugada.TIJERA, respuesta);
    }
}
