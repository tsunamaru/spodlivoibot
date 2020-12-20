package spodlivoi.interactor;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import ws.schild.jave.EncoderException;

import java.io.IOException;

public interface DvachInteractor {

    SendPhoto getThread(Long chatId) throws IOException;
    SendVideo getVideo(Long chatId) throws IOException, InterruptedException, EncoderException;
    int getVideoStats();

}
