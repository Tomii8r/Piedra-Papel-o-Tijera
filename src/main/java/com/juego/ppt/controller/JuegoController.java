package com.juego.ppt.controller;

import com.juego.ppt.model.Jugada;
import com.juego.ppt.model.Partida;
import com.juego.ppt.model.Resultado;
import com.juego.ppt.view.INavegador;
import com.juego.ppt.view.IPanelJuego;
import com.juego.ppt.view.IPanelMenu;

public class JuegoController implements IControlador {
    private static final int PAUSA_VICTORIA_MS = 2000;
    private static final int PAUSA_DERROTA_MS = 3000;

    private final Partida partida;
    private final INavegador navegador;
    private final IPanelMenu panelMenu;
    private final IPanelJuego panelJuego;
    private final Programador programador;
    private final int pausaVictoriaMs;
    private final int pausaDerrotaMs;

    public JuegoController(Partida partida,
                           INavegador navegador,
                           IPanelMenu panelMenu,
                           IPanelJuego panelJuego,
                           Programador programador) {
        this(partida, navegador, panelMenu, panelJuego, programador,
            PAUSA_VICTORIA_MS, PAUSA_DERROTA_MS);
    }

    public JuegoController(Partida partida,
                           INavegador navegador,
                           IPanelMenu panelMenu,
                           IPanelJuego panelJuego,
                           Programador programador,
                           int pausaVictoriaMs,
                           int pausaDerrotaMs) {
        this.partida = partida;
        this.navegador = navegador;
        this.panelMenu = panelMenu;
        this.panelJuego = panelJuego;
        this.programador = programador;
        this.pausaVictoriaMs = pausaVictoriaMs;
        this.pausaDerrotaMs = pausaDerrotaMs;
        panelMenu.setControlador(this);
        panelJuego.setControlador(this);
    }

    @Override
    public void iniciarJuego() {
        panelJuego.reiniciar();
        panelJuego.actualizarPuntajes(partida.getPuntaje(), partida.getPuntajeMaximo(), partida.getNivel());
        panelJuego.setBotonesHabilitados(true);
        navegador.mostrar(INavegador.CARD_JUEGO);
    }

    @Override
    public void elegirJugada(Jugada jugada) {
        Resultado resultado = partida.jugarRonda(jugada);

        panelJuego.setBotonesHabilitados(false);
        panelJuego.actualizarPuntajes(partida.getPuntaje(), partida.getPuntajeMaximo(), partida.getNivel());
        panelJuego.mostrarResultado(resultado, jugada, partida.getJugadaMaquina());

        if (resultado == Resultado.PIERDE) {
            panelMenu.setPuntajeMaximo(partida.getPuntajeMaximo());
            programador.enMs(pausaDerrotaMs, this::volverAlMenu);
        } else if (resultado == Resultado.GANA) {
            programador.enMs(pausaVictoriaMs, this::habilitarJugadas);
        } else {
            habilitarJugadas();
        }
    }

    private void habilitarJugadas() {
        panelJuego.setBotonesHabilitados(true);
    }

    private void volverAlMenu() {
        navegador.mostrar(INavegador.CARD_MENU);
        panelJuego.setBotonesHabilitados(true);
    }
}
