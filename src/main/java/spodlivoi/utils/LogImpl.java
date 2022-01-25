package spodlivoi.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.service.TelegramService;

import java.io.PrintWriter;
import java.io.StringWriter;

@RequiredArgsConstructor
@Slf4j
@Component
public class LogImpl implements Log {

    private TelegramService bot;

    @Value("${telegram.bot.admin-chat-id}")
    private String adminChatId;

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

    void sendErrorMessage(Throwable error, Object message) {
        error(error);
        if (adminChatId != null && !adminChatId.isEmpty()) {
            SendMessage sendMessage = new SendMessage();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            error.printStackTrace(pw);
            String text;
            if (message != null) {
                text = "При обработке запроса:\n```" + message.toString().replaceAll(",", ",\n")
                        + "```\nв подливе произошёл пиздец:\n`" + error + "`\nStackTrace:\n```"
                        + sw.getBuffer().toString() + "```";
            } else {
                text = "В подливе произошёл пиздец:\n`" + error + "`\nStackTrace:\n```"
                        + sw.getBuffer().toString() + "```";
            }
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
    public void error(Throwable error, BotApiObject message) {
        sendErrorMessage(error, message);
    }

    @Override
    public void error(Throwable error) {
        log.error("Error: ", error);
        sendErrorMessage(error, null);
    }

    @Override
    public void error(String message, Throwable error) {
        log.error(message, error);
        sendErrorMessage(error, message);
    }

    @Override
    public void setTelegramService(TelegramService bot) {
        this.bot = bot;
    }
}
