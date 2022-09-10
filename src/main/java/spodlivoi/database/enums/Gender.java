package spodlivoi.database.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
    @Deprecated
    DEFAULT("По умолчанию"),
    MALE("Мужской"),
    FEMALE("Женский"),
    FIGHT_HELICOPTER("!БОЕВОЙ ВЕРТОЛЁТ!");

    @Getter
    private final String name;

    public Gender getNextGender() {
        if (ordinal() + 1 == values().length) {
            return MALE;
        } else {
            return values()[ordinal() + 1];
        }
    }

}
