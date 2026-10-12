package com.juego.ppt;

import com.juego.ppt.controller.JuegoController;
import com.juego.ppt.controller.ProgramadorSwing;
import com.juego.ppt.model.Dificultad;
import com.juego.ppt.model.GeneradorDificultad;
import com.juego.ppt.model.GeneradorRandom;
import com.juego.ppt.model.Jugador;
import com.juego.ppt.model.JugadorHumano;
import com.juego.ppt.model.JugadorMaquina;
import com.juego.ppt.model.Partida;
import com.juego.ppt.model.ResolutorReglas;
import com.juego.ppt.view.INavegador;
import com.juego.ppt.view.PanelJuego;
import com.juego.ppt.view.PanelMenu;
import com.juego.ppt.view.VentanaPrincipal;

import javax.swing.UIManager;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        configurarLookAndFeel();

        Random random = new Random();
        Dificultad dificultad = new Dificultad();

        Jugador humano = new JugadorHumano("Jugador");
        Jugador maquina = new JugadorMaquina("Bot",
            new GeneradorDificultad(new GeneradorRandom(random), dificultad, random));
        Partida partida = new Partida(humano, maquina, new ResolutorReglas(), dificultad);

        VentanaPrincipal ventana = new VentanaPrincipal();
        PanelMenu panelMenu = new PanelMenu();
        PanelJuego panelJuego = new PanelJuego();

        ventana.agregarPantalla(panelMenu, INavegador.CARD_MENU);
        ventana.agregarPantalla(panelJuego, INavegador.CARD_JUEGO);

        new JuegoController(partida, ventana, panelMenu, panelJuego, new ProgramadorSwing());

        ventana.setVisible(true);
        ventana.mostrar(INavegador.CARD_MENU);
    }

    private static void configurarLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (Exception ignored) {
        }
    }
}
