package spodlivoi.utils;

import java.util.Random;

public class Randomizer {

    private static final Random random = new Random();
    private static int getLucky(){
        return getRandomNumberInRange(0, 100);
    }
    public static int getPlusDickSize(){
        int lucky = getLucky();
        if (lucky == 0)
            return 0;
        else if(lucky < 6)
            return getRandomNumberInRange(-10, -7);
        else if(lucky < 21)
            return getRandomNumberInRange(-7, -3);
        else if(lucky < 31)
            return getRandomNumberInRange(-3, -1);
        else if(lucky < 41)
            return getRandomNumberInRange(15, 20);
        else if(lucky < 61)
            return getRandomNumberInRange(7, 15);
        else
            return getRandomNumberInRange(1, 6);
    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return random.nextInt((max - min) + 1) + min;
    }
}
