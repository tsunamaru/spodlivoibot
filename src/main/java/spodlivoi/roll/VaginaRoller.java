package spodlivoi.roll;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import spodlivoi.database.entity.Vagina;
import spodlivoi.database.repository.VaginaRepository;
import spodlivoi.message.Messages;
import spodlivoi.service.TelegramService;
import spodlivoi.utils.Randomizer;

@Component
@Slf4j
public class VaginaRoller extends RollerBase<Vagina> {

    private final Messages messages;

    public VaginaRoller(VaginaRepository repository, TelegramService telegramService, Messages messages) {
        super((JpaRepository) repository, telegramService);
        this.messages = messages;
    }


    @Override
    String getRollMessage(boolean first, int size, int upSize) {
        if (first) {
            if (size == 0) {
                return "На данный момент твоя девственность при тебе!";
            } else {
                return "Ахуеть! Глубина твоей пизды аж " + size +
                        "см!\nЖду тебя завтра на повторный осмотр!";
            }
        } else {
            if (size == 0) {
                return "Мои соболезнования. Сегодня у тебя произошла страшная трагедия: у тебя произошел отвал пизды.";
            }

            if (upSize < 0) {
                int messageNumber = Randomizer.getRandomNumberInRange(0, 3);
                switch (messageNumber) {
                    case 0:
                        return "Прорыв в медицине! Твою пизду подшили и её глубина уменьшилась на целых " +
                                Math.abs(upSize) +
                                "см.\nТеперь она составляет всего лишь " + size + "см!";
                    case 1:
                        return "Одиночество даёт о себе знать, и раны былой любви начинают затягиваться.\n" +
                                "Твоя пизда восстановилось на " + Math.abs(upSize) + "см, и теперь её глубина " +
                                size + "см.\n" +
                                "Возможно, завтра тебе повезет больше?";
                    case 2:
                        return "Ты испугалась планового приёма гинеколога и не трогала себя целых " +
                                Randomizer.getRandomNumberInRange(5, 9) +
                                " дней, от чего твоя пизда стала сократилась на " +
                                Math.abs(upSize) + "см.\n" +
                                "Теперь её глубина составляет " + size + "см!";
                    case 3:
                        return "Батя решил напоить тебя лечебными траваими, от чего твоя пизда сузилась на " +
                                Math.abs(upSize) + "см.\n" +
                                "Ты заработала камни в почках и размер твоей пизды теперь составляет " 
                                + size + "см.\n" +
                                "В следующий раз лучше притворись спящей!";
                }
            } else {
                int messageNumber = Randomizer.getRandomNumberInRange(0, 5);
                String item = messages.getRandomAnusItem();
                switch (messageNumber) {
                    case 0:
                        return "Ты залетела от бомжа на вписке и родила " + getBabyGender() +
                                " от чего случился разрыв пизды на целых " + Math.abs(upSize) + "см. \n" +
                                "Теперь она составляет " + size + "см.";
                    case 1:
                        return "После длинного *BBC*, у тебя жуткий разрыв пизды на " + Math.abs(upSize) + "см.\n" +
                                "Теперь твоя пизда стала " + size + " см. Не забудь посетить гинеколога!";
                    case 2:
                        return "Ты подскользнулась в ванной и случайно упала на " + item +
                                " размером " + Math.abs(upSize) + 
                                "см.\nТеперь глубина твоей пизды аж " + size + "см.\n" +
                                "Не забывай о ежедневной гигиене и возвращайся завтра!";
                    case 3:
                        return "Тебя приняли мусора и посадили на " + item +
                                " размером " + Math.abs(upSize) + "см.\nТеперь глубина твоей пизды " + size + "см.\n" +
                                "В следующий раз будешь думать башкой прежде чем пиздить шоколадные сырки из ашана!";
                    case 4:
                        return "Зря ты согласилась на этот кастинг...\nВ тебя вставили " + item +
                                " размером " + Math.abs(upSize) + "см.\nТеперь глубина твоей пизды " + size + "см.\n" +
                                "В следующий раз постарайся найти нормальную работу.";
                    case 5:
                        return "*ЧУДОВИЩНЫЙ РАЗРЫВ ПИЗДЫ!* Сегодня она стала больше аж на " + Math.abs(upSize) +
                                "см!\n" +
                                "А причина всему - странный предмет, подозрительно похожий на " + item +
                                "\nИнтересно, как он там оказался? В любом случае теперь глубина твоей пизды " +
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
        return "пизду";
    }

    @Override
    String getNames() {
        return "пизд";
    }

    @Override
    String getWaitText() {
        return "На сегодня достаточно вагинальных игр!";
    }

    private String getBabyGender() {
        switch (Randomizer.getRandomNumberInRange(1, 10)) {
            case 1:
                return "Мальчика";
            case 2:
                return "Девочку";
            case 3:
                return "Боевой вертолёт";
            case 4:
                return "Гоблина";
            case 5:
                return "Небинарную персону";
            case 6:
                return "Альт-райта";
            case 7:
                return "ЛГБТ фурри активиста";
            case 8:
                return "Коммуниста";
            case 9:
                return "Пользователя Twitter";
            case 10:
                return "Модератора Reddit";
            default:
                return "Жопного глиста";
        }
    }

}
