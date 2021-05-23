package spodlivoi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import spodlivoi.utils.Log;
import spodlivoi.utils.LogImpl;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Service
public class TelegramService extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String botUserName;

    private final JmsTemplate jmsTemplate;

    private final Log log;

    @PostConstruct
    void selfInit(){
        log.setTelegramService(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            jmsTemplate.convertAndSend("podlivaQueue", update);
        }catch (Exception e){
            log.error(e, update);
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
