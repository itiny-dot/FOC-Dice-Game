/*Main Class: Run the game
Game Interface Class: Runtime, Popout window, PNG files, etc. or ASCII art
Dice Class: Determining dice rolls, containing dice states
Points Class: Storing overall score
Shop Class: Contains various "items", which calculate dice outcomes for each shop item, also contains Currency
Items Class: Self explanatory
Inventory Class: Self explanatory
Logic Class: Calvin's blank canvas to have fun/Conditionals */
package main;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Ends program when window is closed
        window.setResizable(false); //Disables resizability (game can't resize display with it)
        window.setTitle("BalaDice"); //Window display name

        main.GameInterface gamePanel = new main.GameInterface();
        window.add(gamePanel); //Opens game window
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true); //Makes window display
        gamePanel.startGameThread(); //Starts runtimer
    }
}