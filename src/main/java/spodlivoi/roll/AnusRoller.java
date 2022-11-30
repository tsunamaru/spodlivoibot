package spodlivoi.roll;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import spodlivoi.database.entity.Anus;
import spodlivoi.database.repository.AnusRepository;
import spodlivoi.message.Messages;
import spodlivoi.service.TelegramService;
import spodlivoi.utils.Randomizer;

@Component
@Slf4j
public class AnusRoller extends RollerBase<Anus> {

    private final Messages messages;

    public AnusRoller(AnusRepository repository, Messages messages, TelegramService telegramService) {
        super((JpaRepository) repository, telegramService);
        this.messages = messages;
    }

    @Override
    String getRollMessage(boolean first, int size, int upSize) {
        if (first) {
            if (size == 0) {
                return "На данный момент твоя анальная девственность при тебе!";
            } else {
                return "Ахуеть! Ширина твоего ануса составляет аж " + size +
                        "см!\nЖду тебя завтра на повторный осмотр!";
            }
        } else {
            if (size == 0) {
                return "Мои поздравления! Сегодня твой раздроченый в хлам анус наконец-то зашили 🎉🎉🎉";
            }

            if (upSize < 0) {
                int messageNumber = Randomizer.getRandomNumberInRange(0, 3);
                switch (messageNumber) {
                    case 0:
                        return "Прорыв в медицине! Твой анус подшили и он сократился аж на " +
                                Math.abs(upSize) +
                                "см.\nТеперь его ширина всего лишь " + size + "см." +
                                "\nЖду тебя завтра на повторный осмотр!";
                    case 1:
                        return "Одиночество даёт о себе знать, и раны былой любви начинают затягиваться.\n" +
                                "Твое очко восстановилось на " + Math.abs(upSize) + 
                                "см и теперь его ширина составляет " + size +
                                "см.\n" +
                                "Возможно, завтра тебе повезет больше?";
                    case 2:
                        return "*БУ БЛЯТЬ!* Страшно? Нет? А я вижу как твое очко сжалось на " + Math.abs(upSize) +
                                "см!\n" +
                                "Теперь его ширина составляет " + size + "см. Возвращайся завтра, шуганый!";
                    case 3:
                        return "Ты долго сидел на холодной плите, от чего твой анус сжался на " + Math.abs(upSize) +
                                "см.\n" +
                                "Ты заработал геморрой и теперь ширина твоего ануса составляет " + size + "см.\n" +
                                "В следующий раз попроси мамку купить тебе теплые подштанники!";
                }
            } else {
                int messageNumber = Randomizer.getRandomNumberInRange(0, 5);
                String item = messages.getRandomAnusItem();
                switch (messageNumber) {
                    case 0:
                        return "Твой *♂Dungeon Master♂* вставил *♂finger in your anal♂* от чего очко расширилось на " +
                                "целых " + Math.abs(upSize) + "см. \n" +
                                "Теперь его ширина аж " + size + "см! Скоро ты станешь настоящим *♂fucking slave♂*!";
                    case 1:
                        return "После долгого *♂ANAL FISTING♂*, у тебя выпала кишка на " + Math.abs(upSize) + "см.\n" +
                                "Теперь его ширина аж " + size + " см! Не забывай *♂JUST LUBE IT UP♂*.";
                    case 2:
                        return "Ты подскользнулся в ванной и случайно упал на " + item +
                                " размером " + Math.abs(upSize) + "см.\nТеперь ширина твоего ануса составляет "
                                + size + "см!\n" +
                                "Не забывай о ежедневной гигиене и возвращайся завтра!";
                    case 3:
                        return "Тебя приняли мусора и посадили на " + item +
                                " размером " + Math.abs(upSize) + "см.\nТеперь ширина твоего ануса составляет "
                                + size + "см.\n" +
                                "В следующий раз будешь думать башкой прежде чем пиздить шоколадные сырки из ашана!";
                    case 4:
                        return "Зря ты согласился на этот кастинг...\nВ тебя вставили " + item +
                                " размером " + Math.abs(upSize) + "см.\nТеперь ширина твоего ануса " + size + "см.\n" +
                                "В следующий раз постарайся найти нормальную работу!";
                    case 5:
                        return "*ЧУДОВИЩНЫЙ РАЗРЫВ АНУСА!* Сегодня он стал шире аж на " + Math.abs(upSize) + "см!\n" +
                                "А причиной всему - странный предмет, подозрительно похожий на " + item +
                                "\nИнтересно, как он там оказался? В любом случае теперь ширина твоего ануса теперь " +
                                "составляет " + size + "см.";
                }
            }
        }
        return "Пиздец...";
    }

    @Override
    int getPlusSize() {
        int lucky = Randomizer.getRandomNumberInRange(0, 100);
        if (lucky == 0) {
            return 0;
        } else if (lucky < 6) {
            return Randomizer.getRandomNumberInRange(-7, -5);
        } else if (lucky < 21) {
            return Randomizer.getRandomNumberInRange(-5, -3);
        } else if (lucky < 31) {
            return Randomizer.getRandomNumberInRange(-3, -1);
        } else if (lucky < 41) {
            return Randomizer.getRandomNumberInRange(5, 10);
        } else if (lucky < 61) {
            return Randomizer.getRandomNumberInRange(2, 5);
        } else {
            return Randomizer.getRandomNumberInRange(1, 2);
        }
    }

    @Override
    String getName() {
        return "анус";
    }

    @Override
    String getNames() {
        return "анусов";
    }

    @Override
    String getWaitText() {
        return "На сегодня достаточно шалить со своим анусом!";
    }

}
