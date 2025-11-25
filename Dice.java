import java.util.Random;

public class Dice {
    static Random random = new Random();
    //rolls dice, int 1-6 corresponding to it's initial roll value (prior to buffs)
    public static int rollDice(int dice){
        int rollValue = random.nextInt(6);
        //add 1 to the random value as it's range is index 0-5; this is for convenience.
        rollValue += 1;
        //System.out.println(rollValue);
        return rollValue;
    }
}
