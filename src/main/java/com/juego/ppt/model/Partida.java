package com.juego.ppt.model;

public class Partida {
    private final Jugador humano;
    private final Jugador maquina;
    private final ResolutorReglas reglas;
    private final Dificultad dificultad;

    private int puntaje;
    private int puntajeMaximo;
    private Jugada ultimaJugadaMaquina;

    public Partida(Jugador humano, Jugador maquina, ResolutorReglas reglas, Dificultad dificultad) {
        this.humano = humano;
        this.maquina = maquina;
        this.reglas = reglas;
        this.dificultad = dificultad;
    }

    public Resultado jugarRonda(Jugada eleccionHumano) {
        ((JugadorHumano) humano).setJugada(eleccionHumano);
        ((JugadorMaquina) maquina).preparar(eleccionHumano, getNivel());
        ultimaJugadaMaquina = maquina.elegirJugada();
        Resultado resultado = reglas.resolver(humano.elegirJugada(), ultimaJugadaMaquina);

        switch (resultado) {
            case GANA:
                puntaje++;
                if (puntaje > puntajeMaximo) {
                    puntajeMaximo = puntaje;
                }
                break;
            case PIERDE:
                puntaje = 0;
                break;
            case EMPATA:
                break;
        }
        return resultado;
    }

    public int getNivel() {
        return dificultad.nivel(puntaje);
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public Jugada getJugadaMaquina() {
        return ultimaJugadaMaquina;
    }
}
