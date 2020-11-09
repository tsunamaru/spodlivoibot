package spodlivoi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spodlivoi.entity.Dicks;
import spodlivoi.entity.Users;
import spodlivoi.repository.DickRepository;
import spodlivoi.utils.Randomizer;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class DickServiceImpl implements DickService {

    @Autowired
    private DickRepository dickRepository;

    public String roll(Users user){
        try {

            Dicks dick = null;
            boolean first = false;
            int size = 0;
            try {
                dick = user.getDick();
            } catch (Exception e) {
                log.error("Error: " + e);
            }
            if (dick == null) {
                dick = new Dicks();
                dick.setUser(user);
                first = true;
            }else {
                LocalDateTime current = LocalDateTime.now();
                LocalDateTime last = dick.getLastMeasurement();
                current = current.minusHours(0);
                last = last.minusHours(0);
               if(current.getDayOfMonth() == last.getDayOfMonth() &&
                  current.getMonthValue() == last.getMonthValue())
                    return "Ты уже измерял свой огрызок сегодня!\nПриходи через " +
                            (23 - current.getHour()) + "ч " + (59 - current.getMinute()) + "м";
                else
                    size = dick.getSize();
            }
            int upSizeN = getPlusDickSize();
            size += upSizeN;
            if (size < 0 || upSizeN == 0)
                size = 0;
            dick.setSize(size);
            dick.setLastMeasurement(LocalDateTime.now());
            dickRepository.save(dick);
            int dickText = Randomizer.getRandomNumberInRange(1, 5);
            String upSize = String.valueOf(upSizeN).
                    replaceAll("-", "");
            if(first){
                if(size == 0)
                    return "На данный момент ты не имеешь писюна, неудачник!";
                else
                    return "Ого! Размер твоего писюна аж " + size +
                            "см!\nПриходи завтра. Посмотрим, изменился ли он";
            } else{
                if(size == 0)
                    return "Мои соболезнования. Сегодня у тебя произошла страшная трагедия: твой писюн отпал.";
                if(upSizeN < 0) {

                    switch (dickText) {
                        case 1:
                            return "Ахахахах, неудачник. Твой огрызок стал меньше на целых " + upSize +
                                    "см.\nТеперь его длина " + size + "см.";
                        case 2:
                            return "Твой и не без того маленький пенис стал меньше на " + upSize +
                                    "см.\nТеперь его длина " + size + "см.";
                        case 3:
                            return "Нуууу, что тут можно ещё сказать... Твоя пипирка уменьшилась на " + upSize +
                                    "см.\nТеперь её длина " + size + "см.";
                        case 4:
                            return "Так-так, что тут у нас? *УМЕНЬШЕНИЕ ПЕНИСА!* на целых " + upSize +
                                    "см.\nТеперь его длина " + size + "см.";
                        case 5:
                            return "Мои спутники зафиксировали уменьшение твоего полового органа на " + upSize +
                                    "см.\nТеперь его длина " + size + "см.\n*АХАХАХАХАХАХАХ.* Ржу с тебя.";
                    }
                }else
                    switch (dickText) {
                        case 1:
                            return "Поздравляю! Твой член сегодня вырос на целых " + upSize + "см. \n" +
                                    "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                        case 2:
                            return "*Ах ты читер!* Каким-то образом ты смог увеличить свой писюн на " + upSize + "см. \n" +
                                    "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                        case 3:
                            return "Твой пенис скоро можно будет фиксировать со спутников. Он вырос на " + upSize + "см. \n" +
                                    "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                        case 4:
                            return "Ох, ну и завидую же я тебе, потому что твои причиндалы стали больше на " + upSize + "см. \n" +
                                    "Теперь их длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                        case 5:
                            return "Все женщины в шоке! Твой гигантский половой орган стал больше на " + upSize + "см. \n" +
                                    "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                    }
            }

        }catch (Exception e){
            log.error("Error: " + e);
        }
        return "Произошла какая-то ошибка...";
    }

    @Override
    public String getTop(List<Users> users) {
        StringBuilder message = new StringBuilder();
        int number = 1;
        users.removeIf(u -> u.getDick() == null);
        users.sort((d1, d2) -> Integer.compare(d2.getDick().getSize(), d1.getDick().getSize()));
        for(Users user : users){
            message.append(number).append(". ");
            message.append(user.getUserName());
            message.append(" - ").append(user.getDick().getSize()).append("см;\n");
            number++;
        }
        if (message.toString().equals(""))
            message = new StringBuilder("Никто ещё не роллил писюн");
        else
            message.insert(0, "Топ писюнов:\n\n");
        return message.toString();
    }

}
