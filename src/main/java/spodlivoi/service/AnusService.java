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
                    sendMessage(message, "На сегодня достаточно углублять анус!\nПриходи через " +
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
                return "На данный момент ты анальный девственник!";
            else
                return "Пиздец! Глубина твоего ануса аж " + size +
                        "см!\nСледующий осмотр завтра";
        } else {
            if (size == 0)
                return "Мои соболезнования. Сегодня у тебя произошла страшная трагедия: твой анус зашили.";

            if (upSize < 0) {
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
            } else {
                int messageNumber = Randomizer.getRandomNumberInRange(0, 4);
                switch (messageNumber) {
                    case 0:
                        return "Твой ♂Dungeon Master♂ вставил ♂finger in your anal♂ и он стал глубже на целых " + upSize + "см. \n" +
                                "Теперь его глубина " + size + "см. Скоро ты станешь настоящим ♂fucking slaves♂!";
                    case 1:
                        return "После долгого ♂ANAL FISTING♂, у вас выпала кишка на " + upSize + "см.\n" +
                                "Теперь очко стало " + size + " сантиметров. Не забывай ♂JUST LUBE IT UP♂.";
                    case 2:
                        return "Ты случайно подскользнулся в ванной и *СЛУЧАЙНО!* упал на " + Randomizer.getRandomValueFormList(items) +
                                " длинной " + upSize + "см\nтеперь глубина твоего ануса " + size + "см.\n" +
                                "Не забывай о каждодневной гигиене и возвращайся завтра.";
                    case 3:
                        return "Тебя приняли мусора и посадили на  " + Randomizer.getRandomValueFormList(items) +
                                " длинной " + upSize + "см\nтеперь глубина твоего ануса " + size + "см.\n" +
                                "В следующий раз будешь думать, прежде чем пиздить из ашана!";
                    case 4:
                        return "Зря ты согласился на этот кастинг...\nВ тебя вставили " + Randomizer.getRandomValueFormList(items) +
                                " длинной " + upSize + "см\nтеперь глубина твоего ануса " + size + "см.\n" +
                                "В следующий раз найди нормальную работу.";
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
            message = new StringBuilder("Никто ещё не роллил анус");
        else
            message.insert(0, "Топ anal'ов:\n\n");
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
