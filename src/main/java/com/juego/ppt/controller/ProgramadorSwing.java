package com.juego.ppt.controller;

import javax.swing.Timer;

public class ProgramadorSwing implements Programador {
    @Override
    public void enMs(int milisegundos, Runnable accion) {
        Timer timer = new Timer(milisegundos, e -> accion.run());
        timer.setRepeats(false);
        timer.start();
    }
}
