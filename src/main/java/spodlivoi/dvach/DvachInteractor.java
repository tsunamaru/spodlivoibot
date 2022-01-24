package spodlivoi.dvach;

import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.IOException;

public interface DvachInteractor {

    @Async
    void sendThreadAsync(Message message) throws IOException, TelegramApiException;

    @Async
    void sendVideoAsync(Message message) throws IOException, InterruptedException, EncoderException;

    int getVideoStats();

    @Async
    void convertAndSendVideoAsync(Message message, File sourceVideo, File targetVideo, String format)
            throws EncoderException, InterruptedException;

}
