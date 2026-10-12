package com.juego.ppt.model;

import java.util.Random;

public class RandomFijo extends Random {
    private final double valor;

    public RandomFijo(double valor) {
        this.valor = valor;
    }

    @Override
    public double nextDouble() {
        return valor;
    }
}
