package com.juego.ppt.view;

import com.juego.ppt.controller.IControlador;
import com.juego.ppt.model.Jugada;
import com.juego.ppt.model.Resultado;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class PanelJuego extends JPanel implements IPanelJuego {
    private IControlador controlador;

    private final JLabel lblPuntaje = new JLabel("0", SwingConstants.CENTER);
    private final JLabel lblNivel = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblRecord = new JLabel("Récord 0", SwingConstants.CENTER);
    private final JLabel lblDuelo = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblMensaje = new JLabel(" ", SwingConstants.CENTER);

    private final List<BotonRedondeado> botones = new ArrayList<>();

    public PanelJuego() {
        setBackground(Tema.FONDO);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 28, 26, 28));

        add(crearMarcador(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearOpciones(), BorderLayout.SOUTH);

        registrarAtajos();
    }

    private JPanel crearMarcador() {
        JPanel panel = transparente();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblEtiqueta = new JLabel("Puntaje", SwingConstants.CENTER);
        lblEtiqueta.setFont(Tema.CUERPO);
        lblEtiqueta.setForeground(Tema.TEXTO_SUAVE);
        lblEtiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblPuntaje.setFont(Tema.PUNTAJE);
        lblPuntaje.setForeground(Tema.TEXTO);
        lblPuntaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblNivel.setFont(Tema.CUERPO);
        lblNivel.setForeground(Tema.TEXTO_SUAVE);

        JPanel filaPuntaje = transparente();
        filaPuntaje.setLayout(new BoxLayout(filaPuntaje, BoxLayout.X_AXIS));
        filaPuntaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        filaPuntaje.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        filaPuntaje.add(Box.createHorizontalGlue());
        filaPuntaje.add(lblPuntaje);
        filaPuntaje.add(Box.createHorizontalStrut(8));
        filaPuntaje.add(lblNivel);
        filaPuntaje.add(Box.createHorizontalGlue());

        lblRecord.setFont(Tema.CUERPO);
        lblRecord.setForeground(Tema.TEXTO_SUAVE);
        lblRecord.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblEtiqueta);
        panel.add(Box.createVerticalStrut(2));
        panel.add(filaPuntaje);
        panel.add(Box.createVerticalStrut(2));
        panel.add(lblRecord);
        return panel;
    }

    private JPanel crearCentro() {
        JPanel panel = transparente();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        lblDuelo.setFont(Tema.EMOJI);
        lblDuelo.setForeground(Tema.TEXTO);
        lblDuelo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblMensaje.setFont(Tema.SUBTITULO);
        lblMensaje.setForeground(Tema.TEXTO_SUAVE);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(lblDuelo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblMensaje);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel crearOpciones() {
        JPanel panel = transparente();
        panel.setLayout(new GridLayout(1, 3, 14, 0));

        for (Jugada jugada : Jugada.values()) {
            panel.add(crearOpcion(jugada));
        }
        return panel;
    }

    private JPanel crearOpcion(Jugada jugada) {
        JPanel panel = transparente();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        BotonRedondeado boton =
            new BotonRedondeado(Tema.emoji(jugada), Tema.SUPERFICIE, Tema.SUPERFICIE_HOVER);
        boton.setFont(Tema.EMOJI_BOTON);
        boton.setForeground(Tema.TEXTO);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(130, 82));
        boton.setPreferredSize(new Dimension(130, 82));
        boton.addActionListener(e -> elegir(jugada));
        botones.add(boton);

        JLabel caption = new JLabel(Tema.nombre(jugada), SwingConstants.CENTER);
        caption.setFont(Tema.CUERPO);
        caption.setForeground(Tema.TEXTO_SUAVE);
        caption.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(boton);
        panel.add(Box.createVerticalStrut(6));
        panel.add(caption);
        return panel;
    }

    private void registrarAtajos() {
        atajo(KeyStroke.getKeyStroke("1"), Jugada.PIEDRA);
        atajo(KeyStroke.getKeyStroke("2"), Jugada.PAPEL);
        atajo(KeyStroke.getKeyStroke("3"), Jugada.TIJERA);
    }

    private void atajo(KeyStroke tecla, Jugada jugada) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(tecla, tecla.toString());
        getActionMap().put(tecla.toString(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BotonRedondeado boton = botones.get(jugada.ordinal());
                if (boton.isEnabled()) {
                    elegir(jugada);
                }
            }
        });
    }

    private void elegir(Jugada jugada) {
        if (controlador != null) {
            controlador.elegirJugada(jugada);
        }
    }

    private JPanel transparente() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    @Override
    public void setControlador(IControlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarResultado(Resultado resultado, Jugada jugadaHumano, Jugada jugadaMaquina) {
        lblDuelo.setText(Tema.emoji(jugadaHumano) + "   vs   " + Tema.emoji(jugadaMaquina));
        lblDuelo.setForeground(Tema.TEXTO);
        lblMensaje.setText(Tema.mensajeResultado(resultado));
        lblMensaje.setForeground(Tema.colorResultado(resultado));
    }

    @Override
    public void actualizarPuntajes(int puntaje, int puntajeMaximo, int nivel) {
        lblPuntaje.setText(String.valueOf(puntaje));
        lblNivel.setText(Tema.etiquetaNivel(nivel));
        lblRecord.setText("Récord " + puntajeMaximo);
    }

    @Override
    public void setBotonesHabilitados(boolean habilitado) {
        for (JButton boton : botones) {
            boton.setEnabled(habilitado);
        }
    }

    @Override
    public void reiniciar() {
        lblDuelo.setText(" ");
        lblMensaje.setText("Elegí tu jugada");
        lblMensaje.setForeground(Tema.TEXTO_SUAVE);
    }
}
