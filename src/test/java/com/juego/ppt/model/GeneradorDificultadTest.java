package com.juego.ppt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneradorDificultadTest {
    private static final int NIVEL_MEDIO = 1;
    private static final int NIVEL_MAXIMO = Dificultad.NIVEL_MAX;

    private final Dificultad dificultad = new Dificultad();

    private GeneradorDificultad generadorCon(double tirada, Jugada resultadoAzar) {
        GeneradorJugada azarFijo = (rival, nivel) -> resultadoAzar;
        return new GeneradorDificultad(azarFijo, dificultad, new RandomFijo(tirada));
    }

    @Test
    void laPrimeraRondaNoUsaEstrategia() {
        GeneradorDificultad generador = generadorCon(0.0, Jugada.PAPEL);

        assertEquals(Jugada.PAPEL, generador.generar(Jugada.PIEDRA, NIVEL_MAXIMO));
    }

    @Test
    void contrarrestaLaJugadaAnteriorDelHumano() {
        GeneradorDificultad generador = generadorCon(0.0, Jugada.PIEDRA);

        generador.generar(Jugada.PIEDRA, NIVEL_MEDIO);
        Jugada respuesta = generador.generar(Jugada.TIJERA, NIVEL_MEDIO);

        assertEquals(Jugada.PAPEL, respuesta);
    }

    @Test
    void usaSiempreLaUltimaJugadaDelHumanoComoReferencia() {
        GeneradorDificultad generador = generadorCon(0.0, Jugada.PIEDRA);

        generador.generar(Jugada.PIEDRA, NIVEL_MEDIO);
        generador.generar(Jugada.TIJERA, NIVEL_MEDIO);
        Jugada respuesta = generador.generar(Jugada.PAPEL, NIVEL_MEDIO);

        assertEquals(Jugada.PIEDRA, respuesta);
    }

    @Test
    void enNivelCeroSeDejaGanarCuandoCaeEnSuFranja() {
        GeneradorDificultad generador = generadorCon(0.2, Jugada.PAPEL);

        generador.generar(Jugada.PIEDRA, 0);
        Jugada respuesta = generador.generar(Jugada.TIJERA, 0);

        assertEquals(Jugada.TIJERA, respuesta);
    }

    @Test
    void juegaAlAzarCuandoLaTiradaSuperaLasFranjas() {
        GeneradorDificultad generador = generadorCon(0.99, Jugada.TIJERA);

        generador.generar(Jugada.PIEDRA, NIVEL_MEDIO);
        Jugada respuesta = generador.generar(Jugada.PAPEL, NIVEL_MEDIO);

        assertEquals(Jugada.TIJERA, respuesta);
    }

    @Test
    void enElNivelMaximoContrarrestaConElTope() {
        GeneradorDificultad generador = generadorCon(0.3, Jugada.PIEDRA);

        generador.generar(Jugada.PIEDRA, NIVEL_MAXIMO);
        Jugada respuesta = generador.generar(Jugada.TIJERA, NIVEL_MAXIMO);

        assertEquals(Jugada.PAPEL, respuesta);
    }
}
