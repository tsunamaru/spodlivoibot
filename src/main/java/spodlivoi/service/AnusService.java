package spodlivoi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.entity.Anus;
import spodlivoi.entity.Users;
import spodlivoi.repository.AnusRepository;
import spodlivoi.utils.Randomizer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnusService implements Roller  {

    private final AnusRepository anusRepository;
    private final BotService bot;

    @Value("${telegram.bot.anus.items}")
    private ArrayList<String> items;
    @Value("${debug}")
    private boolean debug;

    public void roll(Message message, Users user) {
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
                if (current.getDayOfMonth() == last.getDayOfMonth() &&
                        current.getMonthValue() == last.getMonthValue() && !debug) {
                    sendMessage(message, "На сегодня достаточно шалить со своим анусом!\nВозвращайся через " +
                            (23 - current.getHour()) + "ч " + (59 - current.getMinute()) + "м");
                    return;
                }else
                    size = anus.getSize();
            }
            int upSize = getPlusAnusSize();
            size += upSize;
            if (size < 0 || upSize == 0)
                size = 0;
            sendMessage(message, getRollMessage(first, size, upSize));
            anus.setSize(size);
            anus.setLastMeasurement(LocalDateTime.now());
            anusRepository.save(anus);

        } catch (Exception e) {
            log.error("Error: ", e);
            sendMessage(message, "Произошла какая-то ошибка...");
        }
    }

    private void sendMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        sendMessage.enableMarkdown(true);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error: ", e);
        }
        log.debug("End roll time: {}", System.currentTimeMillis());
    }

    private String getRollMessage(boolean first, int size, int upSize) {
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
                        return "Прорыв в медицине! Твой анус подшили и его глубина уменьшилась на целых " + upSize +
                                "см.\nТеперь он всего лишь " + size + "см.";
                    case 1:
                        return "Одиночество даёт о себе знать, и раны былой любви начинают затягиваться.\n" +
                                "Твое очко восстановилось на " + upSize + "см, и теперь его глубина " + size + "см.\n" +
                                "Возможно, завтра тебе повезет больше?";
                    case 2:
                        return "*БУ БЛЯТЬ!* Страшно? Нет? А я вижу как твое очко сжалось на " + upSize + "см!\n" +
                                "Теперь его глубина " + size + "см. Возвращайся завтра, шуганый!";
                    case 3:
                        return "Ты долго сидел на холодной плите, от чего твой анус сжался на " + upSize + "см.\n" +
                                "Ты заработал геморрой и теперь глубина твоего ануса составляет " + size + "см.\n" +
                                "В следующий раз попроси мамку купить тебе теплые подштанники!";
                }
            } else {
                int messageNumber = Randomizer.getRandomNumberInRange(0, 5);
                switch (messageNumber) {
                    case 0:
                        return "Твой *♂Dungeon Master♂* вставил *♂finger in your anal♂* от чего он стал глубже на целых " + upSize + "см. \n" +
                                "Теперь он " + size + "см. Скоро ты станешь настоящим *♂fucking slave♂*!";
                    case 1:
                        return "После долгого *♂ANAL FISTING♂*, у тебя выпала кишка на " + upSize + "см.\n" +
                                "Теперь твоё очко стало " + size + " см. Не забывай *♂JUST LUBE IT UP♂*.";
                    case 2:
                        return "Ты подскользнулся в ванной и случайно упал на " + Randomizer.getRandomValueFormList(items) +
                                " длинной " + upSize + "см.\nТеперь глубина твоего ануса " + size + "см.\n" +
                                "Не забывай о ежедневной гигиене и возвращайся завтра!";
                    case 3:
                        return "Тебя приняли мусора и посадили на " + Randomizer.getRandomValueFormList(items) +
                                " длинной " + upSize + "см.\nТеперь глубина твоего ануса " + size + "см.\n" +
                                "В следующий раз будешь думать башкой прежде чем пиздить из ашана!";
                    case 4:
                        return "Зря ты согласился на этот кастинг...\nВ тебя вставили " + Randomizer.getRandomValueFormList(items) +
                                " длинной " + upSize + "см.\nТеперь глубина твоего ануса " + size + "см.\n" +
                                "В следующий раз постарайся найти нормальную работу.";
                    case 5:
                        return "*ЧУДОВИЩНЫЙ РАЗРЫВ АНУСА!* Сегодня он стал больше аж на " + upSize + "см!\n" +
                                "А причина всему - незвестный предмет, подозрительно похожий на " + Randomizer.getRandomValueFormList(items) +
                                "\nИнтересно, как он там оказался? В любом случае теперь глубина твоего ануса составляет " + size + "см.";
                }
            }
        }
        return "Пиздец...";
    }

    public String getTop(List<Users> users) {
        StringBuilder message = new StringBuilder();
        int number = 1;
        users.removeIf(u -> u.getAnus() == null);
        users.sort((d1, d2) -> Integer.compare(d2.getAnus().getSize(), d1.getAnus().getSize()));
        for (Users user : users) {
            message.append(number).append(". ");
            message.append(user.getUserName());
            message.append(" - ").append(user.getAnus().getSize()).append("см;\n");
            number++;
        }
        if (message.toString().equals(""))
            message = new StringBuilder("Пока ещё никто не измерил анус!");
        else
            message.insert(0, "Топ анусов:\n\n");
        return message.toString();
    }

    private int getPlusAnusSize(){
        int lucky = Randomizer.getRandomNumberInRange(0, 100);
        if (lucky == 0)
            return 0;
        else if(lucky < 6)
            return Randomizer.getRandomNumberInRange(-7, -5);
        else if(lucky < 21)
            return Randomizer.getRandomNumberInRange(-5, -3);
        else if(lucky < 31)
            return Randomizer.getRandomNumberInRange(-3, -1);
        else if(lucky < 41)
            return Randomizer.getRandomNumberInRange(5, 10);
        else if(lucky < 61)
            return Randomizer.getRandomNumberInRange(2, 5);
        else
            return Randomizer.getRandomNumberInRange(1, 2);
    }


}
