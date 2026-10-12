package com.juego.ppt.controller;

import com.juego.ppt.view.INavegador;

public class NavegadorFake implements INavegador {
    private String ultimaPantalla;

    @Override
    public void mostrar(String pantalla) {
        this.ultimaPantalla = pantalla;
    }

    public String getUltimaPantalla() {
        return ultimaPantalla;
    }
}
