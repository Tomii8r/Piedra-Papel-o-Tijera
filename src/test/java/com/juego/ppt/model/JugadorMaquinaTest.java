package com.juego.ppt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JugadorMaquinaTest {

    @Test
    void delegaLaEleccionEnElGeneradorConElContexto() {
        GeneradorJugada generadorFijo = (rival, nivel) -> nivel == 3 ? Jugada.TIJERA : Jugada.PIEDRA;
        JugadorMaquina bot = new JugadorMaquina("Bot", generadorFijo);

        bot.preparar(Jugada.PAPEL, 3);
        assertEquals(Jugada.TIJERA, bot.elegirJugada());
        assertEquals("Bot", bot.getNombre());
    }
}
