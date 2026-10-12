package com.juego.ppt.model;

public class Dificultad {
    public static final int PUNTOS_POR_NIVEL = 10;
    public static final int NIVEL_MAX = 5;

    private static final double PROBABILIDAD_CONTRA_MAXIMA = 0.35;
    private static final double PROBABILIDAD_PIERDE_INICIAL = 0.90;

    public int nivel(int puntajeMaximo) {
        return Math.min(puntajeMaximo / PUNTOS_POR_NIVEL, NIVEL_MAX);
    }

    public double probabilidadContra(int nivel) {
        return PROBABILIDAD_CONTRA_MAXIMA * proporcion(nivel);
    }

    public double probabilidadPierde(int nivel) {
        return PROBABILIDAD_PIERDE_INICIAL * (1.0 - proporcion(nivel));
    }

    private double proporcion(int nivel) {
        int acotado = Math.max(0, Math.min(nivel, NIVEL_MAX));
        return (double) acotado / NIVEL_MAX;
    }
}
