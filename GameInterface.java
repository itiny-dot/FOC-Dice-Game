package main;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class GameInterface extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16;//64x64tile size
    final int scale = 4; //Multiplier for how large tiles appear on screen
    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 30;
    final int maxScreenRow = 16;
    final int screenWidth = tileSize * maxScreenCol; //1920 pixels
    final int screenHeight = tileSize * maxScreenRow; //1024 pixels

    Thread gameThread;


    public GameInterface() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        Color customColor1 = new Color(0, 100, 0); //Dark Green background color
        this.setBackground(customColor1);
        this.setDoubleBuffered(true);
    }

    //Game runtime
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
            long currentTime = System.currentTimeMillis();
            double drawInterval = 1000/fps;
            double nextDrawTime = System.currentTimeMillis() + drawInterval;
            while (gameThread != null) {
                //Update Frame for animations
                update();
                //Redraws the screen each frame change
                repaint();

                try {
                    double remainingTime = nextDrawTime - System.currentTimeMillis();
                    remainingTime = remainingTime / 1000;

                    if (remainingTime < 0) {
                        remainingTime = 0;
                    }
                    Thread.sleep((long) remainingTime);
                    nextDrawTime += drawInterval;
                } catch (InterruptedException e) {
                e.printStackTrace();
                }
            }
        }

    public void update() {//May use for button presses

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Outer border of box that holds the dice (temporary)
        g2.setColor(Color.white);
        g2.fillRect(100, 100, 10, 300); //Left Border
        g2.fillRect(1820, 100, 10, 300); //Right Border
        g2.fillRect(100, 100, 1720, 10); //Top Border
        g2.fillRect(100, 400, 1730, 10); //Bottom Border
        
    }
}
