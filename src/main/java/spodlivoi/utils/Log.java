package spodlivoi.utils;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.service.TelegramService;

import java.io.PrintWriter;
import java.io.StringWriter;

public interface Log {
    void debug(String text);

    void debug(String text, Object... args);

    void info(String text);

    void info(String text, Object... args);

    void warning(String text);

    void error(Throwable error, BotApiObject message);

    void error(Throwable error);

    void setTelegramService(TelegramService bot);

}
