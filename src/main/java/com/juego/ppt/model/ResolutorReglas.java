package com.juego.ppt.model;

public class ResolutorReglas {
    public Resultado resolver(Jugada humano, Jugada maquina) {
        if (humano.empataCon(maquina)) {
            return Resultado.EMPATA;
        }
        if (humano.venceA(maquina)) {
            return Resultado.GANA;
        }
        return Resultado.PIERDE;
    }
}
