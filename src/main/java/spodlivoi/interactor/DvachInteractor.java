package spodlivoi.interactor;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import ws.schild.jave.EncoderException;

import java.io.IOException;

public interface DvachInteractor {

    SendPhoto getThread(String chatId) throws IOException;
    SendVideo getVideo(String chatId) throws IOException, InterruptedException, EncoderException;
    void deleteVideo(SendVideo sendVideo);
    int getVideoStats();

}
