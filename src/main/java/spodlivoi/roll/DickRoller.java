package spodlivoi.roll;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import spodlivoi.database.entity.Dicks;
import spodlivoi.database.repository.DickRepository;
import spodlivoi.service.TelegramService;
import spodlivoi.utils.Randomizer;

@Slf4j
@Component
public class DickRoller extends RollerBase<Dicks> {


    public DickRoller(DickRepository repository, TelegramService telegramService) {
        super((JpaRepository) repository, telegramService);
    }


    @Override
    String getRollMessage(boolean first, int size, int upSize) {
        if (first) {
            if (size == 0) {
                return "На данный момент ты не имеешь писюна, неудачник!";
            } else {
                return "Ого! Размер твоего писюна аж " + size +
                        "см!\nПриходи завтра. Посмотрим, изменился ли он!";
            }
        } else {
            int dickText = Randomizer.getRandomNumberInRange(1, 5);
            if (size == 0) {
                return "Мои соболезнования. Сегодня у тебя произошла страшная трагедия: твой писюн отпал.";
            }
            if (upSize < 0) {

                switch (dickText) {
                    case 1:
                        return "Ахахахах, неудачник. Твой огрызок стал меньше на целых " + Math.abs(upSize) +
                                "см.\nТеперь его длина " + size + "см.";
                    case 2:
                        return "Твой, и не без того маленький пенис, стал меньше аж на " + Math.abs(upSize) +
                                "см.\nТеперь его длина " + size + "см.";
                    case 3:
                        return "Нуууу, что тут можно ещё сказать... Твоя пипирка уменьшилась на " + Math.abs(upSize) +
                                "см.\nТеперь её длина " + size + "см.";
                    case 4:
                        return "Так-так, что тут у нас? *УМЕНЬШЕНИЕ ПЕНИСА!* на целых " + Math.abs(upSize) +
                                "см.\nТеперь его длина " + size + "см.";
                    case 5:
                        return "Мои спутники зафиксировали уменьшение твоего полового органа на " + Math.abs(upSize) +
                                "см.\nТеперь его длина " + size + "см.\n*АХАХАХАХАХАХАХ.* Ржу с тебя, чепух.";
                }
            } else {
                switch (dickText) {
                    case 1:
                        return "Поздравляю! Твой член сегодня вырос на целых " + Math.abs(upSize) + "см! \n" +
                                "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                    case 2:
                        return "*Ах ты читер!* Каким-то образом ты смог увеличить свой писюн на " + Math.abs(upSize) +
                                "см! \n" +
                                "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                    case 3:
                        return "Твой пенис скоро можно будет фиксировать со спутников. Он вырос на " +
                                Math.abs(upSize) + "см! \n" +
                                "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                    case 4:
                        return "Ох, ну и завидую же я тебе, потому что твои причиндалы стали больше аж на " +
                                Math.abs(upSize) + "см! \n" +
                                "Теперь их длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                    case 5:
                        return "Все тяночки вокруг в шоке! Твой гигантский половой орган стал больше на "
                                + Math.abs(upSize) +
                                "см! \n" +
                                "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                }
            }
        }
        return "Пиздец...";
    }

    @Override
    String getName() {
        return "писюн";
    }

    @Override
    String getNames() {
        return "писюнов";
    }

    @Override
    String getWaitText() {
        return "Ты уже измерял свой огрызок сегодня!";
    }

    @Override
    int getPlusSize() {
        int lucky = Randomizer.getRandomNumberInRange(0, 100);
        if (lucky == 0) {
            return 0;
        } else if (lucky < 6) {
            return Randomizer.getRandomNumberInRange(-10, -7);
        } else if (lucky < 21) {
            return Randomizer.getRandomNumberInRange(-7, -3);
        } else if (lucky < 31) {
            return Randomizer.getRandomNumberInRange(-3, -1);
        } else if (lucky < 41) {
            return Randomizer.getRandomNumberInRange(15, 20);
        } else if (lucky < 61) {
            return Randomizer.getRandomNumberInRange(7, 15);
        } else {
            return Randomizer.getRandomNumberInRange(1, 6);
        }
    }


}
