package spodlivoi.interactor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.database.entity.Dicks;
import spodlivoi.database.entity.Users;
import spodlivoi.database.repository.DickRepository;
import spodlivoi.service.TelegramService;
import spodlivoi.utils.Randomizer;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DickRoller implements RollerInteractor {

    private final DickRepository dickRepository;

    private final TelegramService telegramService;

    @Value("${debug}")
    private boolean debug;


    public void roll(Message message, Users user) throws TelegramApiException {
        if(user.getSettings() != null && !user.getSettings().isRollDick())
            return;
        boolean first = false;
        int size = 0;
        Dicks dick = user.getDick();
        if (dick == null) {
            dick = new Dicks();
            dick.setUser(user);
            first = true;
        } else {
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime last = dick.getLastMeasurement();
            if (current.getDayOfMonth() == last.getDayOfMonth() &&
                    current.getMonthValue() == last.getMonthValue() && !debug) {
                sendMessage(message, "Ты уже измерял свой огрызок сегодня!\nПриходи через " +
                        (23 - current.getHour()) + "ч " + (59 - current.getMinute()) + "м", telegramService);
                return;
            } else
                size = dick.getSize();
        }
        int upSize = getPlusDickSize();
        size += upSize;
        if (size < 0 || upSize == 0)
            size = 0;
        sendMessage(message, getRollMessage(first, size, upSize), telegramService);
        dick.setSize(size);
        dick.setLastMeasurement(LocalDateTime.now());
        dickRepository.save(dick);
    }

    private String getRollMessage(boolean first, int size, int upSize) {
        if (first) {
            if (size == 0)
                return "На данный момент ты не имеешь писюна, неудачник!";
            else
                return "Ого! Размер твоего писюна аж " + size +
                        "см!\nПриходи завтра. Посмотрим, изменился ли он";
        } else {
            int dickText = Randomizer.getRandomNumberInRange(1, 5);
            if (size == 0)
                return "Мои соболезнования. Сегодня у тебя произошла страшная трагедия: твой писюн отпал.";
            if (upSize < 0) {

                switch (dickText) {
                    case 1:
                        return "Ахахахах, неудачник. Твой огрызок стал меньше на целых " + Math.abs(upSize) +
                                "см.\nТеперь его длина " + size + "см.";
                    case 2:
                        return "Твой и не без того маленький пенис стал меньше на " + Math.abs(upSize) +
                                "см.\nТеперь его длина " + size + "см.";
                    case 3:
                        return "Нуууу, что тут можно ещё сказать... Твоя пипирка уменьшилась на " + Math.abs(upSize) +
                                "см.\nТеперь её длина " + size + "см.";
                    case 4:
                        return "Так-так, что тут у нас? *УМЕНЬШЕНИЕ ПЕНИСА!* на целых " + Math.abs(upSize) +
                                "см.\nТеперь его длина " + size + "см.";
                    case 5:
                        return "Мои спутники зафиксировали уменьшение твоего полового органа на " + Math.abs(upSize) +
                                "см.\nТеперь его длина " + size + "см.\n*АХАХАХАХАХАХАХ.* Ржу с тебя.";
                }
            } else
                switch (dickText) {
                    case 1:
                        return "Поздравляю! Твой член сегодня вырос на целых " + Math.abs(upSize) + "см. \n" +
                                "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                    case 2:
                        return "*Ах ты читер!* Каким-то образом ты смог увеличить свой писюн на " + Math.abs(upSize) + "см. \n" +
                                "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                    case 3:
                        return "Твой пенис скоро можно будет фиксировать со спутников. Он вырос на " + Math.abs(upSize) + "см. \n" +
                                "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                    case 4:
                        return "Ох, ну и завидую же я тебе, потому что твои причиндалы стали больше на " + Math.abs(upSize) + "см. \n" +
                                "Теперь их длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                    case 5:
                        return "Все женщины в шоке! Твой гигантский половой орган стал больше на " + Math.abs(upSize) + "см. \n" +
                                "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                }
        }
        return "Пиздец...";
    }

    @Override
    public String getTop(List<Users> users) {
        StringBuilder message = new StringBuilder();
        int number = 1;
        users.removeIf(u -> u.getDick() == null);
        users.sort((d1, d2) -> Integer.compare(d2.getDick().getSize(), d1.getDick().getSize()));
        for (Users user : users) {
            if(user.getSettings() != null && !user.getSettings().isRollDick())
                continue;
            message.append(number).append(". ");
            if(user.getFirstName() == null)
                message.append(user.getUserName());
            else {
                message.append(user.getFirstName());
                if(user.getLastName() != null)
                    message.append(" ").append(user.getLastName());
            }
            message.append(" - ").append(user.getDick().getSize()).append("см\n");
            number++;
        }
        if (message.toString().equals(""))
            message = new StringBuilder("Никто ещё не роллил писюн");
        else
            message.insert(0, "Топ писюнов:\n\n");
        return message.toString();
    }

    private int getPlusDickSize() {
        int lucky = Randomizer.getRandomNumberInRange(0, 100);
        if (lucky == 0)
            return 0;
        else if (lucky < 6)
            return Randomizer.getRandomNumberInRange(-10, -7);
        else if (lucky < 21)
            return Randomizer.getRandomNumberInRange(-7, -3);
        else if (lucky < 31)
            return Randomizer.getRandomNumberInRange(-3, -1);
        else if (lucky < 41)
            return Randomizer.getRandomNumberInRange(15, 20);
        else if (lucky < 61)
            return Randomizer.getRandomNumberInRange(7, 15);
        else
            return Randomizer.getRandomNumberInRange(1, 6);
    }


}
