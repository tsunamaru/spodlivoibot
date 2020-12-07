package spodlivoi.utils;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Log {

    void debug(String text);
    void debug(String text, Object... args);
    void info(String text);
    void info(String text, Object... args);
    void warning(String text);
    void error(Exception error, Update message);
    void error(Exception error);

}
