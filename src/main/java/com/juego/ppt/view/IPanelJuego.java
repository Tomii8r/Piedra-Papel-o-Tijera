package com.juego.ppt.view;

import com.juego.ppt.controller.IControlador;
import com.juego.ppt.model.Jugada;
import com.juego.ppt.model.Resultado;

public interface IPanelJuego {
    void setControlador(IControlador controlador);

    void mostrarResultado(Resultado resultado, Jugada jugadaHumano, Jugada jugadaMaquina);

    void actualizarPuntajes(int puntaje, int puntajeMaximo, int nivel);

    void setBotonesHabilitados(boolean habilitado);

    void reiniciar();
}
