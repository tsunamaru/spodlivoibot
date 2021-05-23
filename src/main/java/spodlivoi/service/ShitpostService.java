package spodlivoi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spodlivoi.database.entity.Chats;
import spodlivoi.database.entity.UserMessage;
import spodlivoi.database.repository.ChatRepository;
import spodlivoi.database.repository.UserMessageRepository;
import spodlivoi.message.Messages;
import spodlivoi.utils.Randomizer;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShitpostService {

    private final UserMessageRepository userMessageRepository;
    private final ChatRepository chatRepository;
    private final Messages messages;

    private final TelegramService telegramService;

    private int trySend = 0;

    @Scheduled(cron = "0 0 0 */1 * *")
    public void sendShitPostOfTheDay() {
        try {
            for (Chats chat : chatRepository.findAll()) {
                try {
                    List<UserMessage> messageList = userMessageRepository.findAllByChat(chat);
                    if (messageList.isEmpty())
                        continue;
                    sendRandomMessage(messageList, String.valueOf(chat.getChatId()));
                } catch (Exception e) {
                    log.error("Ошибка получения сообщения дня ", e);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка получения сообщения дня ", e);
        } finally {
            userMessageRepository.deleteAll();
        }
    }

    private void sendRandomMessage(List<UserMessage> messageList, String chatId){
        var randomMessage = Randomizer.getRandomValueFromList(messageList);
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(Integer.valueOf(randomMessage.getMessageId()));
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setText(messages.getRandomMessageText(randomMessage.getUser().getUserName()));
        try {
           telegramService.execute(sendMessage);
           trySend = 0;
        } catch (Exception e){
            try{
                sendMessage.setReplyToMessageId(null);
                sendMessage.setText(messages.getRandomMessageTextDeleted(randomMessage.getUser().getUserName()));
                trySend = 0;
            } catch (Exception err) {
                log.error("Ошибка отправки высера дня ", err);
                List<UserMessage> messages = new ArrayList<>(messageList);
                messages.removeIf(m -> m.getId().toString().equals(randomMessage.getId().toString()));
                trySend++;
                if (trySend > 5) {
                    log.error("Ошибка отправки высера дня после пяти попыток");
                    trySend = 0;
                    return;
                }
                sendRandomMessage(messageList, chatId);
            }
        }
    }

}
