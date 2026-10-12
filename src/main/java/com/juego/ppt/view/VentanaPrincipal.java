package com.juego.ppt.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class VentanaPrincipal extends JFrame implements INavegador {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);

    public VentanaPrincipal() {
        setTitle("Piedra, Papel o Tijera");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 380);
        setLocationRelativeTo(null);
        setContentPane(contenedor);
        contenedor.setBackground(Tema.FONDO);
    }

    public void agregarPantalla(JPanel panel, String nombre) {
        contenedor.add(panel, nombre);
    }

    @Override
    public void mostrar(String nombre) {
        cardLayout.show(contenedor, nombre);
    }
}
