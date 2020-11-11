package spodlivoi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spodlivoi.entity.Anus;
import spodlivoi.entity.Users;
import spodlivoi.repository.AnusRepository;
import spodlivoi.utils.Randomizer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AnusServiceImpl implements AnusService {

    @Autowired
    private AnusRepository anusRepository;

    @Value("${telegram.bot.anus.items}")
    private ArrayList<String> items;

    @Value("${debug}")
    private boolean debug;

    public String roll(Users user) {
        try {

            Anus anus = null;
            boolean first = false;
            int size = 0;
            try {
                anus = user.getAnus();
            } catch (Exception e) {
                log.error("Error: ", e);
            }
            if (anus == null) {
                anus = new Anus();
                anus.setUser(user);
                first = true;
            } else {
                LocalDateTime current = LocalDateTime.now();
                LocalDateTime last = anus.getLastMeasurement();
                current = current.minusHours(0);
                last = last.minusHours(0);
                if (current.getDayOfMonth() == last.getDayOfMonth() &&
                        current.getMonthValue() == last.getMonthValue() && !debug)
                    return "На сегодня достаточно углублять анус!\nПриходи через " +
                            (23 - current.getHour()) + "ч " + (59 - current.getMinute()) + "м";
                else
                    size = anus.getSize();
            }
            int upSizeN = getPlusAnusSize();
            size += upSizeN;
            if (size < 0 || upSizeN == 0)
                size = 0;
            anus.setSize(size);
            anus.setLastMeasurement(LocalDateTime.now());
            if(!debug)
                anusRepository.save(anus);
            String upSize = String.valueOf(upSizeN).
                    replaceAll("-", "");

            if (first) {
                if (size == 0)
                    return "На данный момент ты анальный девственник!";
                else
                    return "Пиздец! Глубина твоего ануса аж " + size +
                            "см!\nСледующий осмотр завтра";
            } else {
                if (size == 0)
                    return "Мои соболезнования. Сегодня у тебя произошла страшная трагедия: твой анус зашили.";

                if (upSizeN < 0) {
                    int messageNumber = Randomizer.getRandomNumberInRange(0, 2);
                    switch (messageNumber) {
                        case 0:
                            return "Прорыв в медецине! Твой анус подшили и его глубина уменьшилась на целых " + upSize +
                                    "см.\nТеперь она " + size + "см.";
                        case 1:
                            return "Одиночество дало о себе знать, и раны былой любви стали затягиваться.\n" +
                                    "Твое очко затянулось на " + upSize + "см. Теперь его глубина " + size + "см.\n" +
                                    "Возможно завтра тебе повезет больше. Возвращайся и узнаешь?";
                        case 2:
                            return "БУ! Страшно? Нет? А я вижу как твое очко сжалось на " + upSize + "см!\n" +
                                    "Теперь его глубина " + size + "см. Возвращайся завтра, шуганный!";
                    }
                }else {
                    int messageNumber = Randomizer.getRandomNumberInRange(0, 4);
                    switch (messageNumber){
                        case 0:
                            return "Твой ♂Dungeon Master♂ вставил ♂finger in your anal♂ и он стал глубже на целых " + upSize + "см. \n" +
                                "Теперь его глубина " + size + "см. Скоро ты станешь настоящим ♂fucking slaves♂!";
                        case 1:
                            return "После долгого ♂ANAL FISTING♂, у вас выпала кишка на "+upSize+"см.\n" +
                                    "Теперь очко стало " + size + " сантиметров. Не забывай ♂JUST LUBE IT UP♂.";
                        case 2:
                            return "Ты случайно подскользнулся в ванной и *СЛУЧАЙНО!* упал на " + Randomizer.getRandomValueFormList(items) +
                                    " длинной "+upSize+"см\nтеперь глубина твоего ануса " + size + "см.\n" +
                                    "Не забывай о каждодневной гигиене и возвращайся завтра.";
                        case 3:
                            return "Тебя приняли мусора и посадили на  " + Randomizer.getRandomValueFormList(items) +
                                    " длинной "+upSize+"см\nтеперь глубина твоего ануса " + size + "см.\n" +
                                    "В следующий раз будешь думать, прежде чем пиздить из ашана!";
                        case 4:
                            return "Зря ты согласился на этот кастинг...\nВ тебя вставили " + Randomizer.getRandomValueFormList(items) +
                                    " длинной "+upSize+"см\nтеперь глубина твоего ануса " + size + "см.\n" +
                                    "В следующий раз найди нормальную работу.";
                    }



                }
            }


        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return "Произошла какая-то ошибка...";
    }

    @Override
    public String getTop(List<Users> users) {
        StringBuilder message = new StringBuilder();
        int number = 1;
        users.removeIf(u -> u.getAnus() == null);
        users.sort((d1, d2) -> Integer.compare(d2.getAnus().getSize(), d1.getAnus().getSize()));
        for(Users user : users){
            message.append(number).append(". ");
            message.append(user.getUserName());
            message.append(" - ").append(user.getAnus().getSize()).append("см;\n");
            number++;
        }
        if (message.toString().equals(""))
            message = new StringBuilder("Никто ещё не роллил анус");
        else
            message.insert(0, "Топ anal'ов:\n\n");
        return message.toString();
    }



}
