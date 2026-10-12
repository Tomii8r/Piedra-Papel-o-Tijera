package com.juego.ppt.view;

import com.juego.ppt.controller.IControlador;
import com.juego.ppt.model.Jugada;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Dimension;

public class PanelMenu extends JPanel implements IPanelMenu {
    private IControlador controlador;

    private final JLabel lblRecord = new JLabel("Récord: 0", SwingConstants.CENTER);

    public PanelMenu() {
        setBackground(Tema.FONDO);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(44, 48, 44, 48));

        JLabel titulo = new JLabel("Piedra · Papel · Tijera", SwingConstants.CENTER);
        titulo.setFont(Tema.TITULO);
        titulo.setForeground(Tema.TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel iconos = new JLabel(
            Tema.emoji(Jugada.PIEDRA) + "   " + Tema.emoji(Jugada.PAPEL) + "   " + Tema.emoji(Jugada.TIJERA),
            SwingConstants.CENTER);
        iconos.setFont(Tema.EMOJI);
        iconos.setForeground(Tema.TEXTO);
        iconos.setAlignmentX(Component.CENTER_ALIGNMENT);

        BotonRedondeado btnEmpezar =
            new BotonRedondeado("Empezar juego", Tema.PRIMARIO, Tema.PRIMARIO_HOVER);
        btnEmpezar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEmpezar.setMaximumSize(new Dimension(210, 46));
        btnEmpezar.addActionListener(e -> {
            if (controlador != null) {
                controlador.iniciarJuego();
            }
        });

        lblRecord.setFont(Tema.CUERPO);
        lblRecord.setForeground(Tema.TEXTO_SUAVE);
        lblRecord.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(titulo);
        add(Box.createVerticalStrut(10));
        add(iconos);
        add(Box.createVerticalStrut(30));
        add(btnEmpezar);
        add(Box.createVerticalStrut(18));
        add(lblRecord);
        add(Box.createVerticalGlue());
    }

    @Override
    public void setControlador(IControlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void setPuntajeMaximo(int puntajeMaximo) {
        lblRecord.setText("Récord: " + puntajeMaximo);
    }
}
