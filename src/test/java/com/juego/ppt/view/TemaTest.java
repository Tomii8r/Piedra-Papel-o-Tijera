package com.juego.ppt.view;

import com.juego.ppt.model.Jugada;
import com.juego.ppt.model.Resultado;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemaTest {

    @Test
    void emojiPorJugada() {
        assertEquals("🪨", Tema.emoji(Jugada.PIEDRA));
        assertEquals("📄", Tema.emoji(Jugada.PAPEL));
        assertEquals("✂️", Tema.emoji(Jugada.TIJERA));
    }

    @Test
    void nombrePorJugada() {
        assertEquals("Piedra", Tema.nombre(Jugada.PIEDRA));
        assertEquals("Papel", Tema.nombre(Jugada.PAPEL));
        assertEquals("Tijera", Tema.nombre(Jugada.TIJERA));
    }

    @Test
    void colorPorResultado() {
        assertEquals(Tema.GANA, Tema.colorResultado(Resultado.GANA));
        assertEquals(Tema.PIERDE, Tema.colorResultado(Resultado.PIERDE));
        assertEquals(Tema.EMPATA, Tema.colorResultado(Resultado.EMPATA));
    }

    @Test
    void mensajePorResultado() {
        assertTrue(Tema.mensajeResultado(Resultado.GANA).contains("Ganaste"));
        assertTrue(Tema.mensajeResultado(Resultado.PIERDE).contains("Perdiste"));
        assertTrue(Tema.mensajeResultado(Resultado.EMPATA).contains("Empate"));
    }

    @Test
    void etiquetaDeNivel() {
        assertEquals("(Nivel 0)", Tema.etiquetaNivel(0));
        assertEquals("(Nivel 3)", Tema.etiquetaNivel(3));
    }
}
