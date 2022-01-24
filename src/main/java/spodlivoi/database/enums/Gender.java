package spodlivoi.database.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spodlivoi.utils.Randomizer;

@RequiredArgsConstructor
public enum Gender {
    @Deprecated
    DEFAULT("По умолчанию"),
    MALE("Мужской"),
    FEMALE("Женский"),
    FIGHT_HELICOPTER("!БОЕВОЙ ВЕРТОЛЁТ!");

    @Getter
    private final String name;

    public static Gender getRandomGender() {
        int random = Randomizer.getRandomNumberInRange(1, 100);
        if (random <= 45) {
            return MALE;
        } else if (random <= 90) {
            return FEMALE;
        } else {
            return FIGHT_HELICOPTER;
        }
    }

}
