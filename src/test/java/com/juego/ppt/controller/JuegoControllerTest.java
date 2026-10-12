package com.juego.ppt.controller;

import com.juego.ppt.model.Dificultad;
import com.juego.ppt.model.GeneradorGuion;
import com.juego.ppt.model.Jugada;
import com.juego.ppt.model.JugadorHumano;
import com.juego.ppt.model.JugadorMaquina;
import com.juego.ppt.model.Partida;
import com.juego.ppt.model.ResolutorReglas;
import com.juego.ppt.model.Resultado;
import com.juego.ppt.view.INavegador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JuegoControllerTest {
    private static final int PAUSA_DERROTA_MS = 2000;

    private PanelMenuFake menu;
    private PanelJuegoFake juego;
    private NavegadorFake navegador;
    private ProgramadorInmediato programador;

    @BeforeEach
    void setUp() {
        menu = new PanelMenuFake();
        juego = new PanelJuegoFake();
        navegador = new NavegadorFake();
        programador = new ProgramadorInmediato();
    }

    private JuegoController controllerCon(Jugada... jugadasMaquina) {
        Partida partida = new Partida(
            new JugadorHumano("Jugador"),
            new JugadorMaquina("Bot", new GeneradorGuion(jugadasMaquina)),
            new ResolutorReglas(),
            new Dificultad());
        return new JuegoController(partida, navegador, menu, juego, programador);
    }

    @Test
    void elConstructorConectaAmbosPaneles() {
        JuegoController controller = controllerCon();

        assertSame(controller, menu.getControlador());
        assertSame(controller, juego.getControlador());
    }

    @Test
    void iniciarJuegoReiniciaNavegaYHabilitaBotones() {
        controllerCon().iniciarJuego();

        assertEquals(1, juego.getReinicios());
        assertTrue(juego.isBotonesHabilitados());
        assertEquals(0, juego.getNivel());
        assertEquals(INavegador.CARD_JUEGO, navegador.getUltimaPantalla());
    }

    @Test
    void unaVictoriaActualizaElPuntajeYNoNavega() {
        controllerCon(Jugada.TIJERA).elegirJugada(Jugada.PIEDRA);

        assertEquals(Resultado.GANA, juego.getResultado());
        assertEquals(Jugada.PIEDRA, juego.getJugadaHumano());
        assertEquals(Jugada.TIJERA, juego.getJugadaMaquina());
        assertEquals(1, juego.getPuntaje());
        assertEquals(1, juego.getPuntajeMaximo());
        assertTrue(juego.isBotonesHabilitados());
        assertNull(navegador.getUltimaPantalla());
        assertEquals(0, programador.getLlamadas());
    }

    @Test
    void elNivelMostradoSubeConLaRachaDeVictorias() {
        Jugada[] diezVictorias = new Jugada[10];
        for (int i = 0; i < diezVictorias.length; i++) {
            diezVictorias[i] = Jugada.TIJERA;
        }
        JuegoController controller = controllerCon(diezVictorias);

        for (int i = 0; i < 10; i++) {
            controller.elegirJugada(Jugada.PIEDRA);
        }

        assertEquals(1, juego.getNivel());
    }

    @Test
    void unEmpateNoSumaNiNavega() {
        controllerCon(Jugada.PIEDRA).elegirJugada(Jugada.PIEDRA);

        assertEquals(Resultado.EMPATA, juego.getResultado());
        assertEquals(0, juego.getPuntaje());
        assertEquals(0, juego.getPuntajeMaximo());
        assertTrue(juego.isBotonesHabilitados());
        assertNull(navegador.getUltimaPantalla());
        assertEquals(0, programador.getLlamadas());
    }

    @Test
    void unaDerrotaNavegaAlMenuTrasLaPausa() {
        controllerCon(Jugada.PAPEL).elegirJugada(Jugada.PIEDRA);

        assertEquals(Resultado.PIERDE, juego.getResultado());
        assertEquals(0, juego.getPuntaje());
        assertEquals(1, programador.getLlamadas());
        assertEquals(PAUSA_DERROTA_MS, programador.getUltimoRetardo());
        assertEquals(INavegador.CARD_MENU, navegador.getUltimaPantalla());
        assertTrue(juego.isBotonesHabilitados());
    }

    @Test
    void laDerrotaActualizaElRecordDelMenu() {
        JuegoController controller = controllerCon(Jugada.TIJERA, Jugada.PAPEL);

        controller.elegirJugada(Jugada.PIEDRA);
        controller.elegirJugada(Jugada.PIEDRA);

        assertEquals(1, menu.getPuntajeMaximo());
        assertEquals(1, menu.getActualizaciones());
        assertEquals(INavegador.CARD_MENU, navegador.getUltimaPantalla());
    }

    @Test
    void sePuedeInyectarOtroTiempoDePausa() {
        Partida partida = new Partida(
            new JugadorHumano("Jugador"),
            new JugadorMaquina("Bot", new GeneradorGuion(Jugada.PAPEL)),
            new ResolutorReglas(),
            new Dificultad());
        new JuegoController(partida, navegador, menu, juego, programador, 250)
            .elegirJugada(Jugada.PIEDRA);

        assertEquals(250, programador.getUltimoRetardo());
    }
}
