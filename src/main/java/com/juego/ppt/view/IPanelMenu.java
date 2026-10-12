package com.juego.ppt.view;

import com.juego.ppt.controller.IControlador;

public interface IPanelMenu {
    void setControlador(IControlador controlador);

    void setPuntajeMaximo(int puntajeMaximo);
}
