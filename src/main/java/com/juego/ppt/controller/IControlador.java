package com.juego.ppt.controller;

import com.juego.ppt.model.Jugada;

public interface IControlador {
    void iniciarJuego();

    void elegirJugada(Jugada jugada);
}
