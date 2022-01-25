package spodlivoi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteMessageJob implements Job {

    private final TelegramService telegramService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var data = context.getMergedJobDataMap();
        var chatId = data.getLong("chatId");
        var messageId = data.getInt("messageId");
        log.debug("Удаляем сообщение {} в чате {}", messageId, chatId);
        var delete = new DeleteMessage();
        delete.setMessageId(messageId);
        delete.setChatId(String.valueOf(chatId));
        try {
            telegramService.execute(delete);
        } catch (TelegramApiException e) {
            log.error("Ошибка удаления: ", e);
        }
    }
}
