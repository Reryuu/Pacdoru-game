/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 *
 * @author Admin
 */
public class MusicPlayerLoop {

    public static void main(String args[]) throws IOException {
        playMusic("mixkit-arcade-retro-game-over-213.wav");
    }

    public static void playMusic(String filepath) throws IOException {
//        try {
//            AudioData data = new AudioStream(new FileInputStream(filepath)).getData();
//            ContinuousAudioDataStream sound = new ContinuousAudioDataStream(data);
//            AudioPlayer.player.start(sound);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(MusicPlayerLoop.class.getName()).log(Level.SEVERE, null, ex);
//        }
        AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;
        AudioData MD;

        ContinuousAudioDataStream loop = null;

        try {
            InputStream test = new FileInputStream("audio.wav");
            BGM = new AudioStream(test);
            AudioPlayer.player.start(BGM);
            MD = BGM.getData();
            loop = new ContinuousAudioDataStream(MD);

        } catch (FileNotFoundException e) {
            System.out.print(e.toString());
        } catch (IOException error) {
            System.out.print(error.toString());
        }
        MGP.start(loop);
    }
}
