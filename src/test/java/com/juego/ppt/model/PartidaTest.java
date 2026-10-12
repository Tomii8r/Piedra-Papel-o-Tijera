package com.juego.ppt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PartidaTest {

    private Partida partidaCon(Jugada... jugadasMaquina) {
        Jugador humano = new JugadorHumano("Jugador");
        Jugador maquina = new JugadorMaquina("Bot", new GeneradorGuion(jugadasMaquina));
        return new Partida(humano, maquina, new ResolutorReglas(), new Dificultad());
    }

    @Test
    void ganarSumaUnPuntoYActualizaElMaximo() {
        Partida partida = partidaCon(Jugada.TIJERA);

        Resultado resultado = partida.jugarRonda(Jugada.PIEDRA);

        assertEquals(Resultado.GANA, resultado);
        assertEquals(1, partida.getPuntaje());
        assertEquals(1, partida.getPuntajeMaximo());
    }

    @Test
    void perderReiniciaElPuntajeYConservaElMaximo() {
        Partida partida = partidaCon(Jugada.TIJERA, Jugada.PAPEL);

        partida.jugarRonda(Jugada.PIEDRA);
        Resultado resultado = partida.jugarRonda(Jugada.PIEDRA);

        assertEquals(Resultado.PIERDE, resultado);
        assertEquals(0, partida.getPuntaje());
        assertEquals(1, partida.getPuntajeMaximo());
    }

    @Test
    void empateNoModificaElMarcador() {
        Partida partida = partidaCon(Jugada.PIEDRA);

        Resultado resultado = partida.jugarRonda(Jugada.PIEDRA);

        assertEquals(Resultado.EMPATA, resultado);
        assertEquals(0, partida.getPuntaje());
        assertEquals(0, partida.getPuntajeMaximo());
    }

    @Test
    void secuenciaGanaGanaPierdeDejaElMaximoEnDos() {
        Partida partida = partidaCon(Jugada.TIJERA, Jugada.TIJERA, Jugada.PAPEL);

        partida.jugarRonda(Jugada.PIEDRA);
        partida.jugarRonda(Jugada.PIEDRA);
        partida.jugarRonda(Jugada.PIEDRA);

        assertEquals(0, partida.getPuntaje());
        assertEquals(2, partida.getPuntajeMaximo());
    }

    @Test
    void elMaximoNoBajaAlReiniciarseElPuntaje() {
        Partida partida = partidaCon(Jugada.TIJERA, Jugada.TIJERA, Jugada.PAPEL, Jugada.TIJERA);

        partida.jugarRonda(Jugada.PIEDRA);
        partida.jugarRonda(Jugada.PIEDRA);
        partida.jugarRonda(Jugada.PIEDRA);
        partida.jugarRonda(Jugada.PIEDRA);

        assertEquals(1, partida.getPuntaje());
        assertEquals(2, partida.getPuntajeMaximo());
    }

    @Test
    void laJugadaDeLaMaquinaEsLaQueSeResolvio() {
        Partida partida = partidaCon(Jugada.TIJERA);

        partida.jugarRonda(Jugada.PIEDRA);

        assertEquals(Jugada.TIJERA, partida.getJugadaMaquina());
    }

    @Test
    void elNivelArrancaEnCero() {
        assertEquals(0, partidaCon().getNivel());
    }

    @Test
    void elNivelSubeConDiezVictoriasSeguidas() {
        Jugada[] guion = new Jugada[10];
        for (int i = 0; i < guion.length; i++) {
            guion[i] = Jugada.TIJERA;
        }
        Partida partida = partidaCon(guion);

        for (int i = 0; i < guion.length; i++) {
            partida.jugarRonda(Jugada.PIEDRA);
        }

        assertEquals(10, partida.getPuntaje());
        assertEquals(1, partida.getNivel());
    }

    @Test
    void elNivelSeReiniciaAlPerder() {
        Jugada[] guion = new Jugada[11];
        for (int i = 0; i < 10; i++) {
            guion[i] = Jugada.TIJERA;
        }
        guion[10] = Jugada.PAPEL;
        Partida partida = partidaCon(guion);

        for (int i = 0; i < 10; i++) {
            partida.jugarRonda(Jugada.PIEDRA);
        }
        assertEquals(1, partida.getNivel());

        partida.jugarRonda(Jugada.PIEDRA);

        assertEquals(0, partida.getPuntaje());
        assertEquals(0, partida.getNivel());
    }
}
