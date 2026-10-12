package com.juego.ppt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JugadorHumanoTest {

    @Test
    void devuelveLaJugadaSeleccionada() {
        JugadorHumano jugador = new JugadorHumano("Ana");
        jugador.setJugada(Jugada.TIJERA);

        assertEquals(Jugada.TIJERA, jugador.elegirJugada());
        assertEquals("Ana", jugador.getNombre());
    }

    @Test
    void sinSeleccionDevuelveNull() {
        assertNull(new JugadorHumano("Ana").elegirJugada());
    }
}
