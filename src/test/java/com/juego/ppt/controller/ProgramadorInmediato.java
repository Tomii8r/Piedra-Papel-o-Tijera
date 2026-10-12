package com.juego.ppt.controller;

public class ProgramadorInmediato implements Programador {
    private int ultimoRetardo = -1;
    private int llamadas;

    @Override
    public void enMs(int milisegundos, Runnable accion) {
        this.ultimoRetardo = milisegundos;
        this.llamadas++;
        accion.run();
    }

    public int getUltimoRetardo() {
        return ultimoRetardo;
    }

    public int getLlamadas() {
        return llamadas;
    }
}
