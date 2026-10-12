package com.juego.ppt.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JugadaTest {

    @Test
    void piedraVenceATijera() {
        assertTrue(Jugada.PIEDRA.venceA(Jugada.TIJERA));
    }

    @Test
    void tijeraVenceAPapel() {
        assertTrue(Jugada.TIJERA.venceA(Jugada.PAPEL));
    }

    @Test
    void papelVenceAPiedra() {
        assertTrue(Jugada.PAPEL.venceA(Jugada.PIEDRA));
    }

    @Test
    void piedraNoVenceAPapel() {
        assertFalse(Jugada.PIEDRA.venceA(Jugada.PAPEL));
    }

    @Test
    void empataConLaMismaJugada() {
        assertTrue(Jugada.PIEDRA.empataCon(Jugada.PIEDRA));
    }

    @Test
    void noEmpataConOtraJugada() {
        assertFalse(Jugada.PIEDRA.empataCon(Jugada.PAPEL));
    }

    @Test
    void vencedoraEsLaQueLeGana() {
        assertEquals(Jugada.PAPEL, Jugada.PIEDRA.vencedora());
        assertEquals(Jugada.TIJERA, Jugada.PAPEL.vencedora());
        assertEquals(Jugada.PIEDRA, Jugada.TIJERA.vencedora());
    }

    @Test
    void perdedoraEsLaQueLePierde() {
        assertEquals(Jugada.TIJERA, Jugada.PIEDRA.perdedora());
        assertEquals(Jugada.PIEDRA, Jugada.PAPEL.perdedora());
        assertEquals(Jugada.PAPEL, Jugada.TIJERA.perdedora());
    }

    @ParameterizedTest
    @EnumSource(Jugada.class)
    void laVencedoraEfectivamenteVence(Jugada jugada) {
        assertTrue(jugada.vencedora().venceA(jugada));
    }

    @ParameterizedTest
    @EnumSource(Jugada.class)
    void laPerdedoraEfectivamentePierde(Jugada jugada) {
        assertTrue(jugada.venceA(jugada.perdedora()));
    }

    @ParameterizedTest
    @EnumSource(Jugada.class)
    void ningunaJugadaSeVenceASiMisma(Jugada jugada) {
        assertFalse(jugada.venceA(jugada));
    }

    @ParameterizedTest
    @EnumSource(Jugada.class)
    void laRelacionDeVictoriaEsAntisimetrica(Jugada a) {
        for (Jugada b : Jugada.values()) {
            if (a == b) {
                continue;
            }
            assertNotEquals(a.venceA(b), b.venceA(a));
        }
    }
}
