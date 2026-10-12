package com.juego.ppt.view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class ReproductorSonido {
    private static final String RUTA_VICTORIA = "/com/juego/ppt/view/sonidos/win.wav";
    private static final String RUTA_DERROTA = "/com/juego/ppt/view/sonidos/gameover.wav";

    private final Clip victoria;
    private final Clip derrota;

    public ReproductorSonido() {
        this.victoria = cargar(RUTA_VICTORIA);
        this.derrota = cargar(RUTA_DERROTA);
    }

    public void reproducirVictoria() {
        reproducir(victoria);
    }

    public void reproducirDerrota() {
        reproducir(derrota);
    }

    private Clip cargar(String ruta) {
        try {
            URL recurso = ReproductorSonido.class.getResource(ruta);
            if (recurso == null) {
                return null;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(recurso);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (Exception e) {
            return null;
        }
    }

    private void reproducir(Clip clip) {
        if (clip == null) {
            return;
        }
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}
