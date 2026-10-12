package com.juego.ppt.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotonRedondeado extends JButton {
    private static final int RADIO = 16;

    private final Color colorBase;
    private final Color colorHover;
    private boolean hover;

    public BotonRedondeado(String texto, Color colorBase, Color colorHover) {
        super(texto);
        this.colorBase = colorBase;
        this.colorHover = colorHover;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Tema.TEXTO);
        setFont(Tema.CUERPO_NEGRITA);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color relleno;
        if (!isEnabled()) {
            relleno = colorBase.darker();
        } else if (hover) {
            relleno = colorHover;
        } else {
            relleno = colorBase;
        }
        g2.setColor(relleno);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIO, RADIO);
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setCursor(enabled
            ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            : Cursor.getDefaultCursor());
    }
}
