package spodlivoi.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.service.BotService;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
@Component
public class LogImpl implements Log {

    private final BotService bot;

    @Value("${telegram.bot.admin-chat-id}")
    private String adminChatId;

    @PostConstruct
    public void init(){
        bot.setLog(this);
    }

    @Override
    public void debug(String text) {
        log.debug(text);
    }

    @Override
    public void debug(String text, Object... args) {
        log.debug(text, args);
    }

    @Override
    public void info(String text) {
        log.info(text);
    }

    @Override
    public void info(String text, Object... args) {
        log.info(text, args);
    }

    @Override
    public void warning(String text) {
        log.warn(text);
    }

    @Override
    public void error(Exception error, Update message) {
        error(error);
        if(adminChatId != null && !adminChatId.isEmpty()) {
            SendMessage sendMessage = new SendMessage();
            String text = "При обработке запроса:\n```" + message + "```\nв подливе произошёл пиздец:\n`" + error +
                    "`\nStackTrace:\n```" + Arrays.toString(error.getStackTrace()).replaceAll("\\[", "").
                    replaceAll("]", "") + "```";
            text = text.replaceAll(",", ",\n");
            sendMessage.setText(text);
            sendMessage.enableMarkdownV2(true);
            sendMessage.setChatId(adminChatId);
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                error(e);
            }
        }
    }

    @Override
    public void error(Exception error) {
        log.error("Error: ", error);
    }
}
