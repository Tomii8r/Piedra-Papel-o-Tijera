package com.juego.ppt.controller;

import com.juego.ppt.view.IPanelMenu;

public class PanelMenuFake implements IPanelMenu {
    private IControlador controlador;
    private int puntajeMaximo;
    private int actualizaciones;

    @Override
    public void setControlador(IControlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void setPuntajeMaximo(int puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
        this.actualizaciones++;
    }

    public IControlador getControlador() {
        return controlador;
    }

    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public int getActualizaciones() {
        return actualizaciones;
    }
}
