package spodlivoi.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.service.BotService;

import javax.annotation.PostConstruct;
import java.io.PrintWriter;
import java.io.StringWriter;

@RequiredArgsConstructor
@Slf4j
@Component
public class Log {

    private final BotService bot;

    @Value("${telegram.bot.admin-chat-id}")
    private String adminChatId;

    @PostConstruct
    public void init(){
        bot.setLog(this);
    }

    public void debug(String text) {
        log.debug(text);
    }

    public void debug(String text, Object... args) {
        log.debug(text, args);
    }

    public void info(String text) {
        log.info(text);
    }

    public void info(String text, Object... args) {
        log.info(text, args);
    }

    public void warning(String text) {
        log.warn(text);
    }

    public void error(Exception error, BotApiObject message) {
        error(error);
        if(adminChatId != null && !adminChatId.isEmpty()) {
            SendMessage sendMessage = new SendMessage();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            error.printStackTrace(pw);
            String text = "При обработке запроса:\n```" + message.toString().replaceAll(",", ",\n")
                    + "```\nв подливе произошёл пиздец:\n`" + error + "`\nStackTrace:\n```"
                    + sw.getBuffer().toString() + "```";
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

    public void error(Exception error) {
        log.error("Error: ", error);
    }
}
