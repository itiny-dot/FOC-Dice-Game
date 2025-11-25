/*Main Class: Run the game
Game Interface Class: Runtime, Popout window, PNG files, etc. or ASCII art
Dice Class: Determining dice rolls, containing dice states
Points Class: Storing overall score
Shop Class: Contains various "items", which calculate dice outcomes for each shop item, also contains Currency
Items Class: Self explanatory
Inventory Class: Self explanatory
Logic Class: Calvin's blank canvas to have fun/Conditionals */
package main;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("BalaDice");

        GameInterface gamePanel = new GameInterface();


        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}