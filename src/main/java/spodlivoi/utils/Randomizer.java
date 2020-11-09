package spodlivoi.utils;

import java.util.Random;

public class Randomizer {

    private static final Random random = new Random();

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }

        return random.nextInt((max - min) + 1) + min;
    }
}
