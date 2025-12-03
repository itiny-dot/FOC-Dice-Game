package main;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.Random;

public class GameInterface extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16;//64x64tile size
    final int scale = 3; //Multiplier for how large tiles appear on screen
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
    public static final int STATE_SHOP = 2; //Shop Menu
    public static final int STATE_INVENTORY = 3;
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
    private JButton tutorialButton;
    private JButton rollAllButton;
    private JButton backButton;
    private JButton[] rerollButtons = new JButton[5];
    private JButton finishRollingButton;
    private JButton shopButton;
    private JButton inventoryButton;

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
        int boxRight = 1280;
        int boxWidth = boxRight - boxLeft;
        int dieSize = 120;
        int totalDiceWidth = 5 * dieSize;

        int spacing = (boxWidth - totalDiceWidth) / 9; // even spacing before/after each die

        int x = boxLeft + spacing;
        for (int i = 0; i < 5; i++) {
            diceX[i] = x;
            x += dieSize + spacing;
        }
    }
    // ---------------- MENU BUTTON ----------------
    private void setupMenuButtons() {
        gameState = STATE_MENU;
        startButton = new JButton("START GAME");
        startButton.setFont(pixelFont.deriveFont(32f));
        startButton.setBounds(screenWidth/2 - 200, screenHeight/2 - 50, 400, 100);
        startButton.addActionListener(e -> switchToGame());
        this.add(startButton);

        tutorialButton = new JButton("HOW TO PLAY");
        tutorialButton.setFont(pixelFont.deriveFont(32f));
        tutorialButton.setBounds(screenWidth/2 - 200, screenHeight/2 + 200, 400, 100);
        tutorialButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "HOW TO PLAY: BalaDice is a game of both luck and skill. The concept of the game\nis to roll dice to meet a score threshold that increases each successful round.\nScoring rules for the dice are modeled after Yahtzee rules. The objective of the game\nis to pass as many rounds as possible. There is a shop where you can purchase items\nthat make it easier to win. Good luck and have fun!");
        });
        this.add(tutorialButton);

        
    }

    private void switchToMenu() {
        gameState = STATE_MENU;

        // Hide game buttons
        if (rollAllButton != null)      rollAllButton.setVisible(false);
        if (backButton != null)         backButton.setVisible(false);
        if (shopButton != null)         shopButton.setVisible(false);
        if (inventoryButton != null)    inventoryButton.setVisible(false);
        if (finishRollingButton != null)finishRollingButton.setVisible(false);
        if (rerollButtons != null) {
            for (JButton b : rerollButtons) {
                if (b != null) b.setVisible(false);
            }
        }

        // Show menu buttons (they were created in setupMenuButtons())
        if (startButton != null)        startButton.setVisible(true);
        if (tutorialButton != null)     tutorialButton.setVisible(true);

        repaint();
    }

    private void switchToShop(){

        gameState = STATE_SHOP;

        // Hide game buttons
        if (rollAllButton != null)      rollAllButton.setVisible(false);
        if (backButton != null)         backButton.setVisible(true);
        if (shopButton != null)         shopButton.setVisible(false);
        if (inventoryButton != null)    inventoryButton.setVisible(false);
        if (finishRollingButton != null)finishRollingButton.setVisible(false);
        if (rerollButtons != null) {
            for (JButton b : rerollButtons) {
                if (b != null) b.setVisible(false);
            }
        }
        if (startButton != null)        startButton.setVisible(false);
        if (tutorialButton != null)     tutorialButton.setVisible(false);

        repaint();
    }

    private void switchToInventory(){
        gameState = STATE_INVENTORY;

        // Hide game buttons
        if (rollAllButton != null)      rollAllButton.setVisible(false);
        if (backButton != null)         backButton.setVisible(true);
        if (shopButton != null)         shopButton.setVisible(false);
        if (inventoryButton != null)    inventoryButton.setVisible(false);
        if (finishRollingButton != null)finishRollingButton.setVisible(false);
        if (rerollButtons != null) {
            for (JButton b : rerollButtons) {
                if (b != null) b.setVisible(false);
            }
        }
        if (startButton != null)        startButton.setVisible(false);
        if (tutorialButton != null)     tutorialButton.setVisible(false);

        repaint();
    }
    
    private void switchToGame() {
        gameState = STATE_PLAY;
        if (startButton != null)        startButton.setVisible(false);
        if (tutorialButton != null)     tutorialButton.setVisible(false);
        if(rollAllButton == null){
            setupGameButtons();
        }
        setGameButtonsVisible(true);
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

        //Back button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(100, 600, 150, 50);
        backButton.addActionListener(e -> {
            switch(gameState){
                case 1: switchToMenu(); break;
                case 2: switchToGame(); break;
                case 3: switchToGame(); break;
            }
            });
        this.add(backButton);

        // ---------- SHOP BUTTON ----------
        shopButton = new JButton("SHOP");
        shopButton.setFont(new Font("Arial", Font.BOLD, 20));
        // Position it to the left of ROLL
        shopButton.setBounds(screenWidth/2 - 300, 450, 150, 50);
        shopButton.addActionListener(e -> openShop());
        this.add(shopButton);

        // ---------- INVENTORY BUTTON ----------
        inventoryButton = new JButton("INVENTORY");
        inventoryButton.setFont(new Font("Arial", Font.BOLD, 20));
        // Position it to the right of ROLL
        inventoryButton.setBounds(screenWidth/2 + 160, 450, 200, 50);
        inventoryButton.addActionListener(e -> openInventory());
        this.add(inventoryButton);

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
            repaint();
            if (score >= quota) JOptionPane.showMessageDialog(this, "Success");
            else JOptionPane.showMessageDialog(this, "Failure");

        });

        this.add(finishRollingButton);
    }

    //to not create new ones but change the visibility
    private void setGameButtonsVisible(boolean visible){
        if (rollAllButton != null)       rollAllButton.setVisible(visible);
        if (backButton != null)          backButton.setVisible(visible);
        if (shopButton != null)          shopButton.setVisible(visible);
        if (inventoryButton != null)     inventoryButton.setVisible(visible);
        if (finishRollingButton != null) finishRollingButton.setVisible(visible);
        if (rerollButtons != null) {
            for (JButton b : rerollButtons) {
                if (b != null) b.setVisible(visible);
            }
        }
    }

    // ---------------- Shop Button ---------------
    private void openShop(){
        //Shop shopItems = new Shop();
        switchToShop();
        repaint();
        
    }

    private void openInventory(){
        switchToInventory();
        repaint();
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
        switch(gameState){
            case 0: drawMenu(g2); break;
            case 1: drawGame(g2); break;
            case 2: drawShop(g2); break;
            case 3: drawInventory(g2); break;
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

    private void drawShop(Graphics2D g2){
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 64));

        String title = "Shop";
        int width = g2.getFontMetrics().stringWidth(title);

        g2.drawString(title, screenWidth/2 - width/2, 100);
        
    }

    private void drawInventory(Graphics2D g2){
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 64));

        String title = "Inventory";
        int width = g2.getFontMetrics().stringWidth(title);

        g2.drawString(title, screenWidth/2 - width/2, 100);
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
        g2.fillRect(1280, 100, 10, 300);
        g2.fillRect(100, 100, 1180, 10);
        g2.fillRect(100, 400, 1190, 10);

        // Draw the roll counter
        g2.setColor(Color.white);
        if (pixelFont != null) {
            g2.setFont(getPixelFont(32f));
        }
        g2.setColor(Color.white);
        g2.setFont(getPixelFont(32f));
        g2.drawString("RerollCount: " + RerollCount, 100, 80);
        g2.drawString("Score: " + score + " / " + quota, screenWidth - 300, screenHeight - 100);

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
