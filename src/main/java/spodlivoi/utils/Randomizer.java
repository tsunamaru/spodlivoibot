package spodlivoi.utils;

import java.security.SecureRandom;
import java.util.List;

public class Randomizer {

    private static final SecureRandom random = new SecureRandom();

    public static <T> T getRandomValueFromList(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }

        return random.nextInt((max - min) + 1) + min;
    }
}
