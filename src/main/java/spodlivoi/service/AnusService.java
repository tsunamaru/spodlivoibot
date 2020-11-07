package spodlivoi.service;

import spodlivoi.utils.Randomizer;

public interface AnusService  extends Roller  {

    default int getPlusAnusSize(){
        int lucky = Randomizer.getRandomNumberInRange(0, 100);
        if (lucky == 0)
            return 0;
        else if(lucky < 6)
            return Randomizer.getRandomNumberInRange(-7, -5);
        else if(lucky < 21)
            return Randomizer.getRandomNumberInRange(-5, -3);
        else if(lucky < 31)
            return Randomizer.getRandomNumberInRange(-3, -1);
        else if(lucky < 41)
            return Randomizer.getRandomNumberInRange(5, 10);
        else if(lucky < 61)
            return Randomizer.getRandomNumberInRange(2, 5);
        else
            return Randomizer.getRandomNumberInRange(1, 2);
    }

}
