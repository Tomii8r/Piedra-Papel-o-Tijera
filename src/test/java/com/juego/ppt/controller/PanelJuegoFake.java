package com.juego.ppt.controller;

import com.juego.ppt.model.Jugada;
import com.juego.ppt.model.Resultado;
import com.juego.ppt.view.IPanelJuego;

public class PanelJuegoFake implements IPanelJuego {
    private IControlador controlador;
    private Resultado resultado;
    private Jugada jugadaHumano;
    private Jugada jugadaMaquina;
    private int puntaje = -1;
    private int puntajeMaximo = -1;
    private int nivel = -1;
    private boolean botonesHabilitados;
    private int reinicios;
    private int actualizaciones;

    @Override
    public void setControlador(IControlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarResultado(Resultado resultado, Jugada jugadaHumano, Jugada jugadaMaquina) {
        this.resultado = resultado;
        this.jugadaHumano = jugadaHumano;
        this.jugadaMaquina = jugadaMaquina;
    }

    @Override
    public void actualizarPuntajes(int puntaje, int puntajeMaximo, int nivel) {
        this.puntaje = puntaje;
        this.puntajeMaximo = puntajeMaximo;
        this.nivel = nivel;
        this.actualizaciones++;
    }

    @Override
    public void setBotonesHabilitados(boolean habilitado) {
        this.botonesHabilitados = habilitado;
    }

    @Override
    public void reiniciar() {
        this.reinicios++;
    }

    public IControlador getControlador() {
        return controlador;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public Jugada getJugadaHumano() {
        return jugadaHumano;
    }

    public Jugada getJugadaMaquina() {
        return jugadaMaquina;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public int getNivel() {
        return nivel;
    }

    public boolean isBotonesHabilitados() {
        return botonesHabilitados;
    }

    public int getReinicios() {
        return reinicios;
    }

    public int getActualizaciones() {
        return actualizaciones;
    }
}
