package spodlivoi.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.entity.Users;

import java.util.List;

public interface Roller {

    void roll(Message message, Users user) throws TelegramApiException;

    String getTop(List<Users> users);

    default void sendMessage(Message message, String text, BotService bot) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        sendMessage.enableMarkdown(true);
        bot.execute(sendMessage);
    }

}
