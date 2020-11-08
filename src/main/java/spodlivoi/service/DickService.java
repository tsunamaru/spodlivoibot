package spodlivoi.service;

import spodlivoi.utils.Randomizer;

public interface DickService extends Roller {

    default int getPlusDickSize(){
        int lucky = Randomizer.getRandomNumberInRange(0, 100);
        if (lucky == 0)
            return 0;
        else if(lucky < 6)
            return Randomizer.getRandomNumberInRange(-10, -7);
        else if(lucky < 21)
            return Randomizer.getRandomNumberInRange(-7, -3);
        else if(lucky < 31)
            return Randomizer.getRandomNumberInRange(-3, -1);
        else if(lucky < 41)
            return Randomizer.getRandomNumberInRange(15, 20);
        else if(lucky < 61)
            return Randomizer.getRandomNumberInRange(7, 15);
        else
            return Randomizer.getRandomNumberInRange(1, 6);
    }

}
