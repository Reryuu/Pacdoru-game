/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class PlaySoundLoop implements Runnable{
    @Override
    public void run() {
        try {
            startSound();
        } catch (IOException ex) {
            Logger.getLogger(PlaySoundLoop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void startSound() throws IOException {
        MusicPlayerLoop.playMusic("audio.wav");
    }
}
