package com.juego.ppt.model;

public enum Jugada {
    PIEDRA, PAPEL, TIJERA;

    public boolean venceA(Jugada otra) {
        return (this == PIEDRA && otra == TIJERA)
            || (this == TIJERA && otra == PAPEL)
            || (this == PAPEL && otra == PIEDRA);
    }

    public boolean empataCon(Jugada otra) {
        return this == otra;
    }

    public Jugada vencedora() {
        for (Jugada opcion : values()) {
            if (opcion.venceA(this)) {
                return opcion;
            }
        }
        throw new IllegalStateException("Ninguna jugada vence a " + this);
    }

    public Jugada perdedora() {
        for (Jugada opcion : values()) {
            if (this.venceA(opcion)) {
                return opcion;
            }
        }
        throw new IllegalStateException("Ninguna jugada pierde contra " + this);
    }
}
