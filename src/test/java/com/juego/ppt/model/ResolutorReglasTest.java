package com.juego.ppt.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResolutorReglasTest {
    private final ResolutorReglas reglas = new ResolutorReglas();

    @Test
    void ganaCuandoElHumanoVence() {
        assertEquals(Resultado.GANA, reglas.resolver(Jugada.PIEDRA, Jugada.TIJERA));
    }

    @Test
    void pierdeCuandoLaMaquinaVence() {
        assertEquals(Resultado.PIERDE, reglas.resolver(Jugada.PIEDRA, Jugada.PAPEL));
    }

    @Test
    void empataCuandoSonIguales() {
        assertEquals(Resultado.EMPATA, reglas.resolver(Jugada.TIJERA, Jugada.TIJERA));
    }

    @ParameterizedTest
    @CsvSource({
        "PIEDRA, TIJERA, GANA",
        "TIJERA, PAPEL, GANA",
        "PAPEL,  PIEDRA, GANA",
        "PIEDRA, PAPEL,  PIERDE",
        "PAPEL,  TIJERA, PIERDE",
        "TIJERA, PIEDRA, PIERDE",
        "PIEDRA, PIEDRA, EMPATA",
        "PAPEL,  PAPEL,  EMPATA",
        "TIJERA, TIJERA, EMPATA"
    })
    void resuelveTodasLasCombinaciones(Jugada humano, Jugada maquina, Resultado esperado) {
        assertEquals(esperado, reglas.resolver(humano, maquina));
    }
}
