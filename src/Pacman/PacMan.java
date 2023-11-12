package Pacman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;

public class PacMan extends JFrame {

    public PacMan(int id, boolean music) {
//        add(new Board());
        add(new Draw(id));
        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(882, 940);
        setLocationRelativeTo(null);
        if (music) {
            PlaySound playsound = new PlaySound("gamemusic.wav");
            Thread t = new Thread(playsound);
            t.start();
        }
    }

    class ThaoTac {

        public Connection ketNoi() throws ClassNotFoundException, SQLException {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/epj2_game", "root", "");
            return con;
        }
    }
}
