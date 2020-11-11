package spodlivoi.utils;

import java.util.List;
import java.util.Random;

public class Randomizer {

    private static final Random random = new Random();

    public static <T> T getRandomValueFormList(List<T> list){
        return  list.get(random.nextInt(list.size()));
    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }

        return random.nextInt((max - min) + 1) + min;
    }
}
