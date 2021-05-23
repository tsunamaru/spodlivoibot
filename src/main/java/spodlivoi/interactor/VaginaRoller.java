package spodlivoi.interactor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.database.entity.Users;
import spodlivoi.database.entity.Vagina;
import spodlivoi.database.repository.VaginaRepository;
import spodlivoi.message.Messages;
import spodlivoi.service.TelegramService;
import spodlivoi.utils.Randomizer;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class VaginaRoller implements RollerInteractor {

    private final VaginaRepository vaginaRepository;

    private final TelegramService telegramService;

    @Value("${debug}")
    private boolean debug;

    private final Messages messages;

    public void roll(Message message, Users user) throws TelegramApiException {
        if(user.getSettings() != null && !user.getSettings().isRollVagina())
            return;
        boolean first = false;
        int size = 0;
        Vagina vagina = user.getVagina();
        if (vagina == null) {
            vagina = new Vagina();
            vagina.setUser(user);
            first = true;
        } else {
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime last = vagina.getLastMeasurement();
            if (current.getDayOfMonth() == last.getDayOfMonth() &&
                    current.getMonthValue() == last.getMonthValue() && !debug) {
                sendMessage(message, "На сегодня достаточно вагинальных игр!\nВозвращайся через " +
                        (23 - current.getHour()) + "ч " + (59 - current.getMinute()) + "м", telegramService);
                return;
            } else
                size = vagina.getSize();
        }
        int upSize = getPlusVaginaSize();
        size += upSize;
        if (size < 0 || upSize == 0)
            size = 0;
        sendMessage(message, getRollMessage(first, size, upSize), telegramService);
        vagina.setSize(size);
        vagina.setLastMeasurement(LocalDateTime.now());
        vaginaRepository.save(vagina);
    }

    private String getRollMessage(boolean first, int size, int upSize) {
        if (first) {
            if (size == 0)
                return "На данный момент твоя вагинальная девственность при тебе!";
            else
                return "Ахуеть! Глубина твоей вагины аж " + size +
                        "см!\nЖду тебя завтра на повторный осмотр!";
        } else {
            if (size == 0)
                return "Мои соболезнования. Сегодня у тебя произошла страшная трагедия: твоя вагина заросла.";

            if (upSize < 0) {
                int messageNumber = Randomizer.getRandomNumberInRange(0, 3);
                switch (messageNumber) {
                    case 0:
                        return "Прорыв в медицине! Твою вагину подшили и её глубина уменьшилась на целых " + Math.abs(upSize) +
                                "см.\nТеперь она всего лишь " + size + "см.";
                    case 1:
                        return "Одиночество даёт о себе знать, и раны былой любви начинают затягиваться.\n" +
                                "Твоя вагина восстановилось на " + Math.abs(upSize) + "см, и теперь её глубина " + size + "см.\n" +
                                "Возможно, завтра тебе повезет больше?";
                    case 2:
                        return "Ты испугалась планового приёма гениколога и не дрочила целых " + Randomizer.getRandomNumberInRange(5, 9) + " дней от чего твоя вагина стала меньше на "  + Math.abs(upSize) + "см!\n" +
                                "Теперь её глубина " + size + "см. Возвращайся завтра, шуганый!";
                    case 3:
                        return "Батя решил напоить тебя лечебными траваими, от чего твоя вагина сократилась на " + Math.abs(upSize) + "см.\n" +
                                "Ты заработала камни в почках и твоя вагина теперь: " + size + "см.\n" +
                                "В следующий раз лучше притворяйся спящей!";
                }
            } else {
                int messageNumber = Randomizer.getRandomNumberInRange(0, 5);
                String item = messages.getRandomAnusItem();
                switch (messageNumber) {
                    case 0:
                        return "Ты залетела от бомжа на вписке и родила " + getBabyGender() + " от чего случился разрыв вагины на целых " + Math.abs(upSize) + "см. \n" +
                                "Теперь она " + size + "см. Скоро ты станешь настояшей мамой!";
                    case 1:
                        return "После долгого *BBC*, у тебя жуткий разрыв вагины на " + Math.abs(upSize) + "см.\n" +
                                "Теперь твоя вагина стала " + size + " см. Не забывай посещать гинеколога.";
                    case 2:
                        return "Ты подскользнулась в ванной и случайно упала на " + item +
                                " длинной " + Math.abs(upSize) + "см.\nТеперь глубина твоей вагины " + size + "см.\n" +
                                "Не забывай о ежедневной гигиене и возвращайся завтра!";
                    case 3:
                        return "Тебя приняли мусора и посадили на " + item +
                                " длинной " + Math.abs(upSize) + "см.\nТеперь глубина твоей вагины " + size + "см.\n" +
                                "В следующий раз будешь думать башкой прежде чем пиздить из ашана!";
                    case 4:
                        return "Зря ты согласилась на этот кастинг...\nВ тебя вставили " + item +
                                " длинной " + Math.abs(upSize) + "см.\nТеперь глубина твоей вагины " + size + "см.\n" +
                                "В следующий раз постарайся найти нормальную работу.";
                    case 5:
                        return "*ЧУДОВИЩНЫЙ РАЗРЫВ ВАГИНЫ!* Сегодня она стала больше аж на " + Math.abs(upSize) + "см!\n" +
                                "А причина всему - незвестный предмет, подозрительно похожий на " + item +
                                "\nИнтересно, как он там оказался? В любом случае теперь глубина твоей вагины составляет " + size + "см.";
                }
            }
        }
        return "Пиздец...";
    }

    public String getTop(List<Users> users) {
        StringBuilder message = new StringBuilder();
        int number = 1;
        users.removeIf(u -> u.getVagina() == null);
        users.sort((d1, d2) -> Integer.compare(d2.getVagina().getSize(), d1.getVagina().getSize()));
        for (Users user : users) {
            if(user.getSettings() != null && !user.getSettings().isRollVagina())
                continue;
            message.append(number).append(". ");
            if(user.getFirstName() == null)
                message.append(user.getUserName());
            else {
                message.append(user.getFirstName());
                if(user.getLastName() != null)
                    message.append(" ").append(user.getLastName());
            }
            message.append(" - ").append(user.getVagina().getSize()).append("см;\n");
            number++;
        }
        if (message.toString().equals(""))
            message = new StringBuilder("Пока ещё никто не измерил вагину!");
        else
            message.insert(0, "Топ вагин:\n\n");
        return message.toString();
    }

    private int getPlusVaginaSize() {
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

    private String getBabyGender(){
        switch (Randomizer.getRandomNumberInRange(1, 3)){
            case 1: return "Мальчика";
            case 2: return "Девочку";
            case 3: return "Боевой вертолёт";
            default: return "Жопного глиста";
        }
    }

}
