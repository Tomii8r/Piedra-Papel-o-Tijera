package com.juego.ppt.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DificultadTest {
    private static final double DELTA = 1e-9;

    private final Dificultad dificultad = new Dificultad();

    @ParameterizedTest
    @CsvSource({
        "0, 0",
        "9, 0",
        "10, 1",
        "19, 1",
        "20, 2",
        "49, 4",
        "50, 5",
        "999, 5"
    })
    void elNivelSubeCadaDiezPuntosYSeTopa(int puntajeMaximo, int esperado) {
        assertEquals(esperado, dificultad.nivel(puntajeMaximo));
    }

    @Test
    void enNivelCeroNoContrarrestaYSoloSeDejaGanar() {
        assertEquals(0.0, dificultad.probabilidadContra(0), DELTA);
        assertEquals(0.90, dificultad.probabilidadPierde(0), DELTA);
    }

    @Test
    void enElNivelMaximoContrarrestaElTopeYNoSeDejaGanar() {
        assertEquals(0.35, dificultad.probabilidadContra(Dificultad.NIVEL_MAX), DELTA);
        assertEquals(0.0, dificultad.probabilidadPierde(Dificultad.NIVEL_MAX), DELTA);
    }

    @Test
    void laProbabilidadDeContrarrestarEsCrecienteYLaDePerderDecreciente() {
        double contraPrevia = -1;
        double pierdePrevia = 2;
        for (int nivel = 0; nivel <= Dificultad.NIVEL_MAX; nivel++) {
            double contra = dificultad.probabilidadContra(nivel);
            double pierde = dificultad.probabilidadPierde(nivel);
            assertTrue(contra > contraPrevia);
            assertTrue(pierde < pierdePrevia);
            contraPrevia = contra;
            pierdePrevia = pierde;
        }
    }

    @Test
    void lasProbabilidadesSeAcotanFueraDeRango() {
        assertEquals(dificultad.probabilidadContra(Dificultad.NIVEL_MAX), dificultad.probabilidadContra(99), DELTA);
        assertEquals(dificultad.probabilidadContra(0), dificultad.probabilidadContra(-3), DELTA);
    }
}
