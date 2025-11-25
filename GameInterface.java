package main;
import javax.swing.JPanel;
import java.awt.*;

public class GameInterface extends JPanel{
    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;//16x16 tile
    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; //768 pixels
    final int screenHeight = tileSize * maxScreenRow; //576 pixels

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.green);
        this.setDoubleBuffered(true);
    }
}
