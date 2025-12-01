package main;

import java.util.HashMap;

public class Score {
    public static int getScore(int[] dice) {
        //Calculates highest possible score from dice set using modified Yahtzee rules (Adjust for balance since rerolls work differently from Yahtzee)
        int score = 0;
        HashMap<Integer, Integer> count = new HashMap<>();
        for (int d : dice) {
            if (count.containsKey(d)) {
                count.put(d, count.get(d) + 1);
            } else
                count.put(d, 1);
        }
        // Yahtzee
        if (count.containsValue(5)) return 50;
        // 5 length straight
        if (count.size() == 5 && count.containsKey(2) && count.containsKey(3) && count.containsKey(4) && count.containsKey(5)) return 40;
        // 4 length straight
        if (count.size() == 4 && count.containsKey(3) && count.containsKey(4)) {
            if (count.containsKey(2) && (count.containsKey(1) || count.containsKey(5))) return 30;
            else if (count.containsKey(5) && count.containsKey(6)) return 30;
        }
        // Full House
        if (count.containsValue(3) && count.containsValue(2)) return 25;
        // Four of a kind, Three of a kind, etc.
        for (int i = 6; i >= 0; i--) {
            if (!count.containsKey(i)) continue;
            if (count.get(i) == 4 && (i+i+i+i > score)) score = i + i + i + i;
            if (count.get(i) == 3 && (i+i+i > score)) score = i + i + i;
            if (count.get(i) == 2 && (i+i > score)) score = i + i;
            if (count.get(i) == 1 && (i > score)) score = i;
        }
        return score;
    }
}
