package spodlivoi.database.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Copypaste {
    OLDS("Олды", "Платиновые пасты дотатреда"),
    DOTA("Дота", "Малолетние дэбилы"),
    BABY("Ребёнок", "Ещё малолетние дэбилы"),
    KOLCHAN("Колчан", "Зачем мой хуй перешёл тебе в рот?"),
    SHIZIK("Шизик", "T9 insanity");

    private final String name;
    private final String description;

    public static Copypaste ofCommand(String command) {
        for (Copypaste copypaste : values()) {
            if (("/" + copypaste.name()).equalsIgnoreCase(command)) {
                return copypaste;
            }
        }
        throw new IllegalArgumentException();
    }

    public int getNumber() {
        return ordinal() + 1;
    }

}
