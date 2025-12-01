package main;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.Random;

public class GameInterface extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16;//64x64tile size
    final int scale = 4; //Multiplier for how large tiles appear on screen
    final int tileSize = originalTileSize * scale;

    final int maxScreenCol = 30;
    final int maxScreenRow = 16;
    final int screenWidth = tileSize * maxScreenCol; //1920 pixels
    final int screenHeight = tileSize * maxScreenRow; //1024 pixels
    private Font pixelFont;
    // FPS & GAME LOOP
    int fps = 30;
    Thread gameThread;

    // --- GAME STATE ---
    public static final int STATE_MENU = 0; //Start Menu
    public static final int STATE_PLAY = 1; //Game Menu
    //public static final int STATE_SHOP = 2; //Start Menu
    //public static final int STATE_UPGRADE = 3;
    private int gameState = STATE_MENU; //Starts game in Start Menu

    // --- DICE DATA ---
    //private int[] diceValues = {1,1,1,1,1}; //Starting Dice Data

    // rerolls counter
    private int RerollCount = 5;

    //Scoring and Quota
    private int score = 0;
    private int quota = 20;

    // --- BUTTONS ---
    private JButton startButton;
    private JButton rollAllButton;
    private JButton[] rerollButtons = new JButton[5];
    private JButton finishRollingButton;

    // DICE
    private final int DICE_COUNT = 5;
    private int[] diceValues = new int[DICE_COUNT];
    private boolean rolling = false;
    private int animationFrames = 0;
    private final int maxAnimationFrames = fps * 1;
    Random rand = new Random();
    private int diceY = 190; //Dice Positions
    private int[] diceX = new int[5];

    public GameInterface() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        Color customColor1 = new Color(0, 100, 0); //Dark Green background color
        this.setBackground(customColor1);
        this.setDoubleBuffered(true);
        this.setLayout(null);
        loadPixelFont();
        setupMenuButtons();
        computeDicePositions();
    }
    //Compute dice X positions so they are perfectly centered
    private void computeDicePositions() {
        int boxLeft = 100;
        int boxRight = 1820;
        int boxWidth = boxRight - boxLeft; // 1720
        int dieSize = 120;
        int totalDiceWidth = 5 * dieSize;

        int spacing = (boxWidth - totalDiceWidth) / 6; // even spacing before/after each die

        int x = boxLeft + spacing;
        for (int i = 0; i < 5; i++) {
            diceX[i] = x;
            x += dieSize + spacing;
        }
    }
    // ---------------- MENU BUTTON ----------------
    private void setupMenuButtons() {
        startButton = new JButton("START GAME");
        startButton.setFont(pixelFont.deriveFont(32f));
        startButton.setBounds(screenWidth/2 - 200, screenHeight/2 - 50, 400, 100);
        startButton.addActionListener(e -> switchToGame());
        this.add(startButton);
    }
    private void switchToGame() {
        gameState = STATE_PLAY;
        startButton.setVisible(false);
        setupGameButtons();
        repaint();
    }
    // ---------------- GAME BUTTONS ----------------
    private void setupGameButtons() {
        // Big roll button
        rollAllButton = new JButton("ROLL");
        rollAllButton.setFont(new Font("Arial", Font.BOLD, 24));
        rollAllButton.setBounds(screenWidth/2 - 100, 450, 200, 60);

        rollAllButton.addActionListener(e -> {
            if (RerollCount > 0) {
                rollAllDice();
                RerollCount--;
                updateRollAvailability();
            }
        });

        this.add(rollAllButton);

        // Each reroll button
        for (int i = 0; i < 5; i++) {
            rerollButtons[i] = new JButton("Reroll");
            rerollButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            rerollButtons[i].setBounds(diceX[i] + 20, diceY + 130, 80, 30);

            final int index = i;
            rerollButtons[i].addActionListener(e -> {
                if (RerollCount > 0) {
                    diceValues[index] = main.Dice.rollDice(5);
                    RerollCount--;
                    updateRollAvailability();
                }
            });

            this.add(rerollButtons[i]);
        }

        //Submit Dice for Scoring Button
        finishRollingButton = new JButton("Submit Dice");
        finishRollingButton.setFont(new Font("Arial", Font.BOLD, 24));
        finishRollingButton.setBounds(screenWidth/2 - 100, 550, 200, 60);

        finishRollingButton.addActionListener(e -> {
            score = Score.getScore(diceValues);
            if (score >= quota) JOptionPane.showMessageDialog(this, "Success");
            else JOptionPane.showMessageDialog(this, "Failure");

        });

        this.add(finishRollingButton);
    }
    //Grays out buttons and makes them unusable when out of rerolls
    private void updateRollAvailability() {
        rollAllButton.setEnabled(RerollCount > 0);
        for (JButton b : rerollButtons) b.setEnabled(RerollCount > 0);
    }
    // ---------------- ROLL LOGIC ----------------
    private void rollAllDice() {
        for (int i = 0; i < 5; i++)
            diceValues[i] = main.Dice.rollDice(5);
    }
    //Game runtime
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        double drawInterval = 1000 / fps;
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
    public void update() {
        if (rolling) {
            // Randomize dice each frame for animation effect
            for (int i = 0; i < DICE_COUNT; i++) {
                diceValues[i] = rand.nextInt(6) + 1;
            }
            animationFrames++;
        }
            // Stop animation after 1 second
            if (animationFrames >= maxAnimationFrames) {
                rolling = false;
                animationFrames = 0;

                // Final actual roll using your Dice class
                for (int i = 0; i < DICE_COUNT; i++) {
                    diceValues[i] = main.Dice.rollDice(1);
                }
            }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        this.setDoubleBuffered(true);
        if (gameState == STATE_MENU) {
            drawMenu(g2);
        } else if (gameState == STATE_PLAY) {
            drawGame(g2);
        }
    }
    public Font getPixelFont(float size) {
        return pixelFont == null ? getFont() : pixelFont.deriveFont(size);
    }
    private void loadPixelFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/Minecraft.ttf");
            Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
            this.pixelFont = pixelFont; // store in class field for reuse
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void drawMenu(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 64));

        String title = "BalaDice";
        int width = g2.getFontMetrics().stringWidth(title);

        g2.drawString(title, screenWidth/2 - width/2, 200);
    }
    private void drawGame(Graphics2D g2) {

        // Draw the dice outer border
        g2.setColor(Color.white);
        g2.fillRect(100, 100, 10, 300);
        g2.fillRect(1820, 100, 10, 300);
        g2.fillRect(100, 100, 1720, 10);
        g2.fillRect(100, 400, 1730, 10);

        // Draw the roll counter
        g2.setColor(Color.white);
        if (pixelFont != null) {
            g2.setFont(getPixelFont(32f));
        }
        g2.setColor(Color.white);
        g2.setFont(getPixelFont(32f));
        g2.drawString("RerollCount: " + RerollCount, 100, 80);
        g2.drawString("Score: " + score + " / " + quota, screenWidth - 200, screenHeight - 100);

        // Draw dice
        for (int i = 0; i < 5; i++) {
            drawDie(g2, diceX[i], diceY, diceValues[i]);
        }
    }
    private void drawDie(Graphics2D g2, int x, int y, int value) {
        g2.setColor(Color.white);
        g2.fillRect(x, y, 120, 120);

        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(x, y, 120, 120);

        g2.setFont(new Font("Arial", Font.BOLD, 48));
        String text = String.valueOf(value);
        int width = g2.getFontMetrics().stringWidth(text);

        g2.drawString(text, x + 60 - width/2, y + 75);
    }
}