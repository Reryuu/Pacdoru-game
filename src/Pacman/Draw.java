/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author Admin
 */
public class Draw extends Board {

    final int scrsize = nrofblocks * blocksize;
    final int pacmananimcount = 4;
    int pacanimdir = 1;

    long startTime;
    long endTime;
    float time ;

    int player_id;
    // Dummy timer that reduces the lives every second. For demo purposes only of course
    Timer timer = new Timer(40, this);

    public Draw(int id) {
        addKeyListener(new TAdapter());
        getImages();
        timer.start();
        player_id = id;
    }
    
    public void playGame(Graphics2D g2d) {
        if (dying) {
            isDeath();
        } else {
            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
//            if(finished){
//                wingame(g2d);
//            }
        }
    }

    @Override
    public void isDeath() {
        pacsleft--;
        if (pacsleft == 0) {
            ingame = false;
            endTime = System.currentTimeMillis();
            time = (int) ((endTime - startTime) / 1000);
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //db_demo là tên của database, root là username và password là rỗng
                try (Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/epj2_game", "root", "")) {
                    //db_demo là tên của database, root là username và password là rỗng
                    Statement stmt = con.createStatement();

                    stmt.executeUpdate("INSERT INTO `achievement`( `player_id`, `score`, `time`) VALUES ('" + player_id + "','" + score + "','" + time + "')");
                    System.out.println("Game over");
                }
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println(e);
            }
        }
        continueLevel();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);
        doAnimation();
        if (ingame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        g.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void drawMaze(Graphics2D g2d) {
        short i = 0;
        int x, y;

        for (y = 0; y < scrsize; y += blocksize) {
            for (x = 0; x < scrsize; x += blocksize) {
                g2d.setColor(mazecolor);
                g2d.setStroke(new BasicStroke(2));

                if ((screendata[i] & 1) != 0) // draws left
                {
                    g2d.drawLine(x, y, x, y + blocksize - 1);
                }
                if ((screendata[i] & 2) != 0) // draws top
                {
                    g2d.drawLine(x, y, x + blocksize - 1, y);
                }
                if ((screendata[i] & 4) != 0) // draws right
                {
                    g2d.drawLine(x + blocksize - 1, y, x + blocksize - 1,
                            y + blocksize - 1);
                }
                if ((screendata[i] & 8) != 0) // draws bottom
                {
                    g2d.drawLine(x, y + blocksize - 1, x + blocksize - 1,
                            y + blocksize - 1);
                }
                if ((screendata[i] & 16) != 0) // draws point
                {
                    g2d.setColor(dotcolor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }
                i++;
            }
        }
    }

    public void drawScore(Graphics2D g) {
        int i;
        String s,l;

        g.setFont(smallfont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + score;
        g.drawString(s, scrsize / 2 + 300, scrsize + 16);
        for (i = 0; i < pacsleft; i++) {
            g.drawImage(pacman3left, i * 28 + 8, scrsize + 1, this);
        }
        l = "Level "+level;
        g.drawString(l, scrsize / 2 + 230, scrsize + 16);
    }

    public void doAnimation() {
        pacanimcount--;
        if (pacanimcount <= 0) {
            pacanimcount = pacanimdelay;
            pacmananimpos = pacmananimpos + pacanimdir;
            if (pacmananimpos == (pacmananimcount - 1) || pacmananimpos == 0) {
                pacanimdir = -pacanimdir;
            }
        }
    }

    @Override
    public void drawPacman(Graphics2D g2d) {
        if (viewdx == -1) {
            drawPacmanLeft(g2d);
        } else if (viewdx == 1) {
            drawPacmanRight(g2d);
        } else if (viewdy == -1) {
            drawPacmanUp(g2d);
        } else {
            drawPacmanDown(g2d);
        }
    }

    public void drawPacmanUp(Graphics2D g2d) {
        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2up, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3up, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4up, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    public void drawPacmanDown(Graphics2D g2d) {
        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2down, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3down, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4down, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    public void drawPacmanLeft(Graphics2D g2d) {
        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2left, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3left, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4left, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    public void drawPacmanRight(Graphics2D g2d) {
        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2right, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3right, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4right, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    public void showIntroScreen(Graphics2D g2d) {
        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);

        String s = "Press S to start.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2);
    }


    public void addNotify() {
        super.addNotify();
        initGame();
    }

    public final void getImages() {

        ghost = new ImageIcon("pacpix/padoru.png").getImage();
        pacman1 = new ImageIcon("pacpix/pacman.png").getImage();
        pacman2up = new ImageIcon("pacpix/up1.png").getImage();
        pacman3up = new ImageIcon("pacpix/up2.png").getImage();
        pacman4up = new ImageIcon("pacpix/up3.png").getImage();
        pacman2down = new ImageIcon("pacpix/down1.png").getImage();
        pacman3down = new ImageIcon("pacpix/down2.png").getImage();
        pacman4down = new ImageIcon("pacpix/down3.png").getImage();
        pacman2left = new ImageIcon("pacpix/left1.png").getImage();
        pacman3left = new ImageIcon("pacpix/left2.png").getImage();
        pacman4left = new ImageIcon("pacpix/left3.png").getImage();
        pacman2right = new ImageIcon("pacpix/right1.png").getImage();
        pacman3right = new ImageIcon("pacpix/right2.png").getImage();
        pacman4right = new ImageIcon("pacpix/right3.png").getImage();

    }

    public void initGame() {
        pacsleft = paclifes;
        score = 0;
        initLevel();
        nrofghosts = ghosts;
        currentspeed = 3;
        startTime = System.currentTimeMillis();
    }
    
//    public void wingame(Graphics2D g2d){
//        g2d.setColor(new Color(0, 32, 48));
//        g2d.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
//        g2d.setColor(Color.white);
//        g2d.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);
//
//        String s = "Press S to continue.";
//        Font small = new Font("Cambria", Font.BOLD, 14);
//        FontMetrics metr = this.getFontMetrics(small);
//
//        g2d.setColor(Color.white);
//        g2d.setFont(small);
//        g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2);
//    }

    class TAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (ingame) {
                if (key == KeyEvent.VK_LEFT) {
                    reqdx = -1;
                    reqdy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    reqdx = 1;
                    reqdy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    reqdx = 0;
                    reqdy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    reqdx = 0;
                    reqdy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    ingame = false;
                } else if (key == KeyEvent.VK_PAUSE) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                }
            } else {
                if (key == 's' || key == 'S') {
                    ingame = true;
                    initGame();
                }
            }
        }
    }
}
