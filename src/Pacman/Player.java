/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

/**
 *
 * @author Admin
 */
public class Player {
    public int id_nguoichoi=0;

    public Player() {
    }

    public Player(int id_nguoichoi) {
        this.id_nguoichoi = id_nguoichoi;
    }

    public int getId_nguoichoi() {
        return id_nguoichoi;
    }

    public void setId_nguoichoi(int id_nguoichoi) {
        this.id_nguoichoi = id_nguoichoi;
    }
}
