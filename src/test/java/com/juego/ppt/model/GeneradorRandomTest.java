package com.juego.ppt.model;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GeneradorRandomTest {

    private Jugada generar(GeneradorRandom generador) {
        return generador.generar(Jugada.PIEDRA, 0);
    }

    @Test
    void siempreDevuelveUnaJugadaValida() {
        GeneradorRandom generador = new GeneradorRandom(new Random(1));
        for (int i = 0; i < 1000; i++) {
            assertNotNull(generar(generador));
        }
    }

    @Test
    void mismaSemillaProduceLaMismaSecuencia() {
        GeneradorRandom a = new GeneradorRandom(new Random(42));
        GeneradorRandom b = new GeneradorRandom(new Random(42));
        for (int i = 0; i < 50; i++) {
            assertEquals(generar(a), generar(b));
        }
    }

    @Test
    void conElTiempoUtilizaLasTresOpciones() {
        GeneradorRandom generador = new GeneradorRandom(new Random(7));
        Set<Jugada> vistas = EnumSet.noneOf(Jugada.class);
        for (int i = 0; i < 1000; i++) {
            vistas.add(generar(generador));
        }
        assertEquals(Jugada.values().length, vistas.size());
    }
}
