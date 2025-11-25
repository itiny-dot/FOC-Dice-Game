import java.util.Random;

public class Dice {
    static Random random = new Random();
    //rolls dice, int 1-6 corresponding to it's initial roll value (prior to buffs)
    private int faces[6]=[1,2,3,4,5,6];
    //sets default faces for a die to default to base die values
    public static int rollDice(int dice){
        int rollValue = random.nextInt(6);
        //System.out.println(rollValue);
        return faces[rollValue];
        //random value picks the index of a given face of a die to allow for die editing
    }
    public static boolean changeFace(int face,int delta) {
    	//changes die face by a delta amount provided result is a positive int
    	if (faces[face]+delta>0) {
    		faces[face]+=delta;
    		return true;
    		else return false;
    	}
    	
    }
}
