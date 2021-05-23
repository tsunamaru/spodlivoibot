package spodlivoi.interactor;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.database.entity.Users;
import spodlivoi.service.TelegramService;

import java.util.List;

public interface RollerInteractor {

    void roll(Message message, Users user) throws TelegramApiException;

    String getTop(List<Users> users);

    default void sendMessage(Message message, String text, TelegramService telegramService) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        sendMessage.enableMarkdown(true);
       telegramService.execute( sendMessage);
    }
}
