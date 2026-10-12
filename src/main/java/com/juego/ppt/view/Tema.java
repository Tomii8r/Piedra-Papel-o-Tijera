package com.juego.ppt.view;

import com.juego.ppt.model.Jugada;
import com.juego.ppt.model.Resultado;

import java.awt.Color;
import java.awt.Font;

public final class Tema {
    public static final Color FONDO = new Color(0x1E1E1E);
    public static final Color SUPERFICIE = new Color(0x2A2A2A);
    public static final Color SUPERFICIE_HOVER = new Color(0x3A3A3A);
    public static final Color TEXTO = new Color(0xF5F5F5);
    public static final Color TEXTO_SUAVE = new Color(0x9E9E9E);
    public static final Color PRIMARIO = new Color(0x5B8DEF);
    public static final Color PRIMARIO_HOVER = new Color(0x7AA4F2);
    public static final Color GANA = new Color(0x4CAF50);
    public static final Color PIERDE = new Color(0xE05C5C);
    public static final Color EMPATA = new Color(0x9E9E9E);

    public static final Font TITULO = new Font("Segoe UI Light", Font.PLAIN, 27);
    public static final Font SUBTITULO = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font CUERPO = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font CUERPO_NEGRITA = new Font("Segoe UI Semibold", Font.PLAIN, 15);
    public static final Font PUNTAJE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font EMOJI = new Font("Segoe UI Emoji", Font.PLAIN, 42);
    public static final Font EMOJI_BOTON = new Font("Segoe UI Emoji", Font.PLAIN, 36);

    private Tema() {
    }

    public static String emoji(Jugada jugada) {
        switch (jugada) {
            case PIEDRA:
                return "🪨";
            case PAPEL:
                return "📄";
            case TIJERA:
                return "✂️";
            default:
                return "";
        }
    }

    public static String nombre(Jugada jugada) {
        switch (jugada) {
            case PIEDRA:
                return "Piedra";
            case PAPEL:
                return "Papel";
            case TIJERA:
                return "Tijera";
            default:
                return "";
        }
    }

    public static Color colorResultado(Resultado resultado) {
        switch (resultado) {
            case GANA:
                return GANA;
            case PIERDE:
                return PIERDE;
            case EMPATA:
                return EMPATA;
            default:
                return TEXTO;
        }
    }

    public static String mensajeResultado(Resultado resultado) {
        switch (resultado) {
            case GANA:
                return "¡Ganaste!  +1";
            case PIERDE:
                return "Perdiste";
            case EMPATA:
                return "Empate";
            default:
                return "";
        }
    }

    public static String etiquetaNivel(int nivel) {
        return "(Nivel " + nivel + ")";
    }
}
