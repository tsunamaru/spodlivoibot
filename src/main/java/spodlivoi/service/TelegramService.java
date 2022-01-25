package spodlivoi.service;

import lombok.RequiredArgsConstructor;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.database.repository.ChatSettingRepository;
import spodlivoi.utils.Log;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class TelegramService extends TelegramLongPollingBot {

    private final JmsTemplate jmsTemplate;
    private final Log log;
    private final ChatSettingRepository chatSettingRepository;
    private final Scheduler scheduler;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String botUserName;

    @PostConstruct
    void selfInit() {
        log.setTelegramService(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            jmsTemplate.convertAndSend("podlivaQueue", update);
        } catch (Exception e) {
            log.error(e, update);
        }
    }

    public Message sendSticker(SendSticker sendSticker) throws TelegramApiException {
        var result = execute(sendSticker);
        setDeleteTimer(result);
        return result;
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method)
            throws TelegramApiException {
        var result = super.execute(method);
        if (method instanceof SendMessage sendMessage && sendMessage.getReplyMarkup() == null) {
            setDeleteTimer(result);
        }
        return result;
    }

    public <T extends Serializable> void setDeleteTimer(T result) {
        if (result instanceof Message message) {
            var setting = chatSettingRepository.findByChatId(message.getChatId());
            if (setting.isPresent() && setting.get().getTimeForDeleteMessage() != null) {
                var data = new JobDataMap();
                data.put("chatId", message.getChatId());
                data.put("messageId", message.getMessageId());
                var jobKey = JobKey.jobKey("delete-message-" + message.getChatId() + "-" + message.getMessageId());
                var startDate = Date.from(
                        LocalDateTime.now().plus(Duration.ofMillis(setting.get().getTimeForDeleteMessage()))
                                .atZone(ZoneId.systemDefault()).toInstant());
                var job = JobBuilder.newJob().ofType(DeleteMessageJob.class).storeDurably()
                        .withIdentity(jobKey)
                        .usingJobData(data)
                        .build();
                var trigger = TriggerBuilder.newTrigger().forJob(jobKey)
                        .withIdentity(jobKey.getName() + "-trigger")
                        .startAt(startDate)
                        .build();
                try {
                    scheduler.scheduleJob(job, trigger);
                } catch (SchedulerException e) {
                    log.error("Ошибка создания события удаления", e);
                }
            }
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
