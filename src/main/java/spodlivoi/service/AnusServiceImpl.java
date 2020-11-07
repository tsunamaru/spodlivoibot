package spodlivoi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spodlivoi.entity.Anus;
import spodlivoi.entity.Users;
import spodlivoi.repository.AnusRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AnusServiceImpl implements AnusService {

    @Autowired
    private AnusRepository anusRepository;

    public String roll(Users user) {
        try {

            Anus anus = null;
            boolean first = false;
            int size = 0;
            try {
                anus = user.getAnus();
            } catch (Exception e) {
                e.printStackTrace();
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
                        current.getMonthValue() == last.getMonthValue())
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
                if (upSizeN < 0)
                    return "Прорыв в медецине! Твой анус подшили и его глубина уменьшилась на целых " + upSize +
                            "см.\nТеперь она " + size + "см.";
                else
                    return "Твой Dungeon Master вставил finger in your anal и он стал глубже на целых " + upSize + "см. \n" +
                            "Теперь его глубина " + size + "см. Скоро ты станешь настоящим fucking slave!";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Произошла какая-то ошибка...";
    }

    @Override
    public String getTop(List<Users> users) {
        StringBuilder message = new StringBuilder();
        int number = 1;
        ArrayList<Anus> rollBases = new ArrayList<>();
        for(Users user : users)
            rollBases.add(user.getAnus());
        try {
            rollBases.sort((d1, d2) -> Integer.compare(d2.getSize(), d1.getSize()));
        } catch (Exception ignored) {
        }
        for (Anus rollBase : rollBases) {
            message.append(number).append(". ");
            message.append(rollBase.getUser().getUserName());
            message.append(" - ").append(rollBase.getSize()).append("см;\n");
            number++;
        }
        if (message.toString().equals(""))
            message = new StringBuilder("Никто ещё не роллил anal");
        else
            message.insert(0, "Топ anal'ов:\n\n");
        return message.toString();
    }



}
