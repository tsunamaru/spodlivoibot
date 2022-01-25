package spodlivoi.utils;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import spodlivoi.service.TelegramService;

public interface Log {
    void debug(String text);

    void debug(String text, Object... args);

    void info(String text);

    void info(String text, Object... args);

    void warning(String text);

    void error(Throwable error, BotApiObject message);

    void error(Throwable error);

    void error(String message, Throwable error);

    void setTelegramService(TelegramService bot);

}
