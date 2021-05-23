package spodlivoi.roll;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.database.entity.Anus;
import spodlivoi.database.entity.Users;
import spodlivoi.database.repository.AnusRepository;
import spodlivoi.message.Messages;
import spodlivoi.service.TelegramService;
import spodlivoi.utils.Randomizer;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class AnusRoller extends  RollerBase<Anus> {

    private final Messages messages;

    public AnusRoller(AnusRepository repository, Messages messages, TelegramService telegramService) {
        super((JpaRepository)repository, telegramService);
        this.messages = messages;
    }

    @Override
    String getRollMessage(boolean first, int size, int upSize) {
        if (first) {
            if (size == 0)
                return "На данный момент твоя анальная девственность при тебе!";
            else
                return "Ахуеть! Глубина твоего ануса аж " + size +
                        "см!\nЖду тебя завтра на повторный осмотр!";
        } else {
            if (size == 0)
                return "Мои соболезнования. Сегодня у тебя произошла страшная трагедия: твой анус зашили.";

            if (upSize < 0) {
                int messageNumber = Randomizer.getRandomNumberInRange(0, 3);
                switch (messageNumber) {
                    case 0:
                        return "Прорыв в медицине! Твой анус подшили и его глубина уменьшилась на целых " + Math.abs(upSize) +
                                "см.\nТеперь он всего лишь " + size + "см.";
                    case 1:
                        return "Одиночество даёт о себе знать, и раны былой любви начинают затягиваться.\n" +
                                "Твое очко восстановилось на " + Math.abs(upSize) + "см, и теперь его глубина " + size + "см.\n" +
                                "Возможно, завтра тебе повезет больше?";
                    case 2:
                        return "*БУ БЛЯТЬ!* Страшно? Нет? А я вижу как твое очко сжалось на " + Math.abs(upSize) + "см!\n" +
                                "Теперь его глубина " + size + "см. Возвращайся завтра, шуганый!";
                    case 3:
                        return "Ты долго сидел на холодной плите, от чего твой анус сжался на " + Math.abs(upSize) + "см.\n" +
                                "Ты заработал геморрой и теперь глубина твоего ануса составляет " + size + "см.\n" +
                                "В следующий раз попроси мамку купить тебе теплые подштанники!";
                }
            } else {
                int messageNumber = Randomizer.getRandomNumberInRange(0, 5);
                String item = messages.getRandomAnusItem();
                switch (messageNumber) {
                    case 0:
                        return "Твой *♂Dungeon Master♂* вставил *♂finger in your anal♂* от чего он стал глубже на целых " + Math.abs(upSize) + "см. \n" +
                                "Теперь он " + size + "см. Скоро ты станешь настоящим *♂fucking slave♂*!";
                    case 1:
                        return "После долгого *♂ANAL FISTING♂*, у тебя выпала кишка на " + Math.abs(upSize) + "см.\n" +
                                "Теперь твоё очко стало " + size + " см. Не забывай *♂JUST LUBE IT UP♂*.";
                    case 2:
                        return "Ты подскользнулся в ванной и случайно упал на " + item +
                                " длинной " + Math.abs(upSize) + "см.\nТеперь глубина твоего ануса " + size + "см.\n" +
                                "Не забывай о ежедневной гигиене и возвращайся завтра!";
                    case 3:
                        return "Тебя приняли мусора и посадили на " + item +
                                " длинной " + Math.abs(upSize) + "см.\nТеперь глубина твоего ануса " + size + "см.\n" +
                                "В следующий раз будешь думать башкой прежде чем пиздить из ашана!";
                    case 4:
                        return "Зря ты согласился на этот кастинг...\nВ тебя вставили " + item +
                                " длинной " + Math.abs(upSize) + "см.\nТеперь глубина твоего ануса " + size + "см.\n" +
                                "В следующий раз постарайся найти нормальную работу.";
                    case 5:
                        return "*ЧУДОВИЩНЫЙ РАЗРЫВ АНУСА!* Сегодня он стал больше аж на " + Math.abs(upSize) + "см!\n" +
                                "А причина всему - незвестный предмет, подозрительно похожий на " + item +
                                "\nИнтересно, как он там оказался? В любом случае теперь глубина твоего ануса составляет " + size + "см.";
                }
            }
        }
        return "Пиздец...";
    }

    @Override
    int getPlusSize() {
        int lucky = Randomizer.getRandomNumberInRange(0, 100);
        if (lucky == 0)
            return 0;
        else if (lucky < 6)
            return Randomizer.getRandomNumberInRange(-7, -5);
        else if (lucky < 21)
            return Randomizer.getRandomNumberInRange(-5, -3);
        else if (lucky < 31)
            return Randomizer.getRandomNumberInRange(-3, -1);
        else if (lucky < 41)
            return Randomizer.getRandomNumberInRange(5, 10);
        else if (lucky < 61)
            return Randomizer.getRandomNumberInRange(2, 5);
        else
            return Randomizer.getRandomNumberInRange(1, 2);
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
