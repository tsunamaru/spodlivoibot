package spodlivoi.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.stickers.GetStickerSet;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedSticker;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.api.objects.stickers.StickerSet;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.entity.Chats;
import spodlivoi.entity.Users;
import spodlivoi.enums.CopypasteType;
import spodlivoi.interactor.DvachInteractor;
import spodlivoi.repository.ChatRepository;
import spodlivoi.repository.UserRepository;
import spodlivoi.utils.Log;
import spodlivoi.utils.Randomizer;
import ws.schild.jave.EncoderException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Setter
public class BotService extends TelegramLongPollingBot {


    private JSONArray baby;
    private JSONArray dota;
    private JSONArray kolchan;
    private JSONArray olds;
    private JSONArray shizik;

    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String botUserName;
    @Value("${telegram.bot.sticker-packs}")
    private ArrayList<String> fightPacks;
    @Value("${telegram.bot.random-usernames}")
    private ArrayList<String> userNames;
    @Value("classpath:baby.json")
    private Resource babyFile;
    @Value("classpath:dota.json")
    private Resource dotaFile;
    @Value("classpath:kolchan.json")
    private Resource kolchanFile;
    @Value("classpath:olds.json")
    private Resource oldsFile;
    @Value("classpath:shizik.json")
    private Resource shizikFile;
    @Value("${telegram.bot.maxinline}")
    private int maxInlines;

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final DvachInteractor dvachInteractor;
    private DickService dickService;
    private AnusService anusService;
    private Log log;

    private final InlineQueryResultArticle article = new InlineQueryResultArticle();
    private final InlineQueryResultArticle article2 = new InlineQueryResultArticle();
    private final InlineQueryResultArticle article3 = new InlineQueryResultArticle();
    private final InlineQueryResultArticle article4 = new InlineQueryResultArticle();
    private final InlineQueryResultArticle article5 = new InlineQueryResultArticle();
    private final InlineQueryResultArticle repairArticle = new InlineQueryResultArticle();

    @PostConstruct
    public void initialization() throws IOException {
        InputStream insultsStream = babyFile.getInputStream();
        JSONObject insultsJson = new JSONObject(StreamUtils.copyToString(insultsStream, StandardCharsets.UTF_8));
        baby = insultsJson.getJSONArray("baby");
        insultsStream = dotaFile.getInputStream();
        insultsJson = new JSONObject(StreamUtils.copyToString(insultsStream, StandardCharsets.UTF_8));
        dota = insultsJson.getJSONArray("dota");
        insultsStream = kolchanFile.getInputStream();
        insultsJson = new JSONObject(StreamUtils.copyToString(insultsStream, StandardCharsets.UTF_8));
        kolchan = insultsJson.getJSONArray("kolchan");
        insultsStream = oldsFile.getInputStream();
        insultsJson = new JSONObject(StreamUtils.copyToString(insultsStream, StandardCharsets.UTF_8));
        olds = insultsJson.getJSONArray("olds");
        insultsStream = shizikFile.getInputStream();
        insultsJson = new JSONObject(StreamUtils.copyToString(insultsStream, StandardCharsets.UTF_8));
        shizik = insultsJson.getJSONArray("shizik");
        article3.setTitle("Ребёнок");
        article3.setDescription("Ещё малолетние дэбилы");
        article4.setTitle("Колчан");
        article4.setDescription("Зачем мой хуй перешёл тебе в рот?");
        article5.setId("5");
        article5.setTitle("Шизик");
        article5.setDescription("T9 insanity");
        article4.setId("4");
        article3.setId("3");
        article2.setId("2");
        article2.setTitle("Дота");
        article2.setDescription("Малолетние дэбилы");
        article.setTitle("Олды");
        article.setDescription("Платиновые пасты дотатреда");
        article.setId("1");

        repairArticle.setId("6");
        repairArticle.setDescription("");
        repairArticle.setTitle("ЧИНИ!");
        repairArticle.setInputMessageContent(new InputTextMessageContent().setMessageText("<b>" + "ЧИНИ ".repeat(700) +"</b>")
                .setParseMode(ParseMode.HTML));
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                if (message.isCommand()) {
                    acceptCommand(message);
                } else if (message.isReply()) {
                    if (message.getReplyToMessage().getFrom() != null &&
                            message.getReplyToMessage().getFrom().getUserName() != null &&
                            message.getReplyToMessage().getFrom().getUserName().equals(getBotUsername()))
                        sendFightSticker(message, true);
                } else if (message.hasText()) {
                    if (message.getText().contains(getBotUsername()))
                        sendFightSticker(message, true);
                }
            } else if (update.hasInlineQuery()) {

                AnswerInlineQuery answerInlineQuery;
                /*if (update.getInlineQuery().getQuery().equals("webm") ||
                        update.getInlineQuery().getQuery().equals("video")) {
                    List<InlineQueryResult> results = new ArrayList<>();
                    InlineQueryResultVideo randomWeb = dvachInteractor.getInlineVideo();
                    randomWeb.setId("1");
                    randomWeb.setDescription("Рандомный Webm из /b");
                    randomWeb.setTitle("цуим");
                    randomWeb.setMimeType("video/mp4");
                    results.add(randomWeb);

                    answerInlineQuery = new AnswerInlineQuery().setCacheTime(0)
                            .setPersonal(true)
                            .setResults(results)
                            .setInlineQueryId(update.getInlineQuery().getId());
                } else*/ if (update.getInlineQuery().getQuery().toLowerCase().matches("fight") ||
                        update.getInlineQuery().getQuery().toLowerCase().matches("боевая") ||
                        update.getInlineQuery().getQuery().toLowerCase().matches("хрю")) {

                    int setCount = fightPacks.size();
                    int sets = Randomizer.getRandomNumberInRange(1, (int) Math.ceil((double) setCount / (double) maxInlines));
                    int start = maxInlines * (sets - 1);
                    int end = maxInlines * sets;
                    if (setCount < end)
                        end = setCount;
                    if (end - start != maxInlines)
                        start = start + (end - start) - maxInlines;
                    List<String> packs = fightPacks.subList(start, end);
                    List<InlineQueryResult> results = new ArrayList<>();
                    int i = 1;
                    for (String pack : packs) {
                        InlineQueryResultCachedSticker q = new InlineQueryResultCachedSticker();
                        q.setId(String.valueOf(i));
                        q.setStickerFileId(getSticker(getStickerSet(pack)).getFileId());
                        results.add(q);
                        i++;
                    }
                    answerInlineQuery = new AnswerInlineQuery().setCacheTime(0)
                            .setPersonal(true)
                            .setResults(results)
                            .setInlineQueryId(update.getInlineQuery().getId());
                } else {
                    List<InlineQueryResult> results = new ArrayList<>();
                    article.setInputMessageContent(new InputTextMessageContent().setMessageText(
                            getRandomCopyPaste(CopypasteType.olds)).setParseMode(ParseMode.MARKDOWN));
                    results.add(article);
                    article2.setInputMessageContent(new InputTextMessageContent().setMessageText(
                            getRandomCopyPaste(CopypasteType.dota)).setParseMode(ParseMode.MARKDOWN));
                    results.add(article2);
                    article3.setInputMessageContent(new InputTextMessageContent().setMessageText(
                            getRandomCopyPaste(CopypasteType.baby)).setParseMode(ParseMode.MARKDOWN));
                    results.add(article3);
                    article4.setInputMessageContent(new InputTextMessageContent().setMessageText(
                            getRandomCopyPaste(CopypasteType.kolchan)).setParseMode(ParseMode.MARKDOWN));
                    results.add(article4);
                    article5.setInputMessageContent(new InputTextMessageContent().setMessageText(
                            getRandomCopyPaste(CopypasteType.shizik)).setParseMode(ParseMode.MARKDOWN));
                    results.add(article5);
                    results.add(repairArticle);
                    answerInlineQuery = new AnswerInlineQuery().setCacheTime(0)
                            .setPersonal(true)
                            .setResults(results)
                            .setInlineQueryId(update.getInlineQuery().getId());
                }
                execute(answerInlineQuery);
            }
        } catch (Exception e) {
            log.error(e, update);
        }
    }

    private void sendMessage(Message message, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        execute(sendMessage);
    }

    private void acceptCommand(Message message) throws TelegramApiException, InterruptedException, EncoderException, IOException {
        String messageText = message.getText();
        String command;
        if (messageText.contains("@")) {
            command = messageText.split("@")[0];
            if (!messageText.split("@")[1].equals(getBotUsername()))
                return;
        } else {
            command = messageText;
        }
        switch (command) {
            case "/dick":
                log.debug("Start roll time: {}", System.currentTimeMillis());
                roll(message, dickService);
                break;
            case "/anus":
                log.debug("Start roll time: {}", System.currentTimeMillis());
                roll(message, anusService);
                break;
            case "/fight":
                if (message.isReply()) {
                    sendFightSticker(message.getReplyToMessage(), true);
                    deleteMessage(message);
                } else
                    sendFightSticker(message, false);
                break;
            case "/test":
                sendMessage(message, "Я работаю, а твой писюн - нет!");
                break;
            case "/topdicks":
                getTop(message, dickService);
                break;
            case "/topanuses":
                getTop(message, anusService);
                break;
            case "/bred":
                SendPhoto sendPhoto = dvachInteractor.getThread(message.getChatId());
                sendPhoto.setReplyToMessageId(message.getMessageId());
                execute(sendPhoto);
                break;
            case "/shizik":
                sendRandomCopypaste(CopypasteType.shizik, message);
                break;
            case "/olds":
                sendRandomCopypaste(CopypasteType.olds, message);
                break;
            case "/kolchan":
                sendRandomCopypaste(CopypasteType.kolchan, message);
                break;
            case "/dota":
                sendRandomCopypaste(CopypasteType.dota, message);
                break;
            case "/baby":
                sendRandomCopypaste(CopypasteType.baby, message);
                break;
            case "/webm":
                sendMessage(message, "Это операция может занять продолжительное время из-за перекодирования видео...");
                Thread t = new Thread(() -> {
                    try {
                        SendVideo sendVideo  = dvachInteractor.getVideo(message.getChatId());
                        sendVideo.setReplyToMessageId(message.getMessageId());
                        execute(sendVideo);
                    } catch (Exception e) {
                        log.error(e, message);
                    }
                });
                t.start();
                break;
            case "/videostats":
                sendMessage(message, "Количество видео в обработке: " + dvachInteractor.getVideoStats());
                break;
        }
    }

    private void deleteMessage(Message message) throws TelegramApiException {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId());
        deleteMessage.setMessageId(message.getMessageId());
        execute(deleteMessage);
    }

    private JSONArray getJSONCopyPaste(CopypasteType type){
        switch (type){
            case baby: return baby;
            case dota: return dota;
            case olds: return olds;
            case shizik: return shizik;
            case kolchan: return kolchan;
            default: return null;
        }
    }

    private String getRandomCopyPaste(CopypasteType type){
        JSONArray jsonArray = getJSONCopyPaste(type);
        if(jsonArray != null) {
            int rand = Randomizer.getRandomNumberInRange(0, jsonArray.length() - 1);
            return jsonArray.getString(rand);
        }else
            return "Копипасту спиздили армяне!";
    }

    private void sendRandomCopypaste(CopypasteType type, Message message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
        if (message.isReply()) {
            sendMessage.setReplyToMessageId(message.getReplyToMessage().getMessageId());
            deleteMessage(message);
        }
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setText(getRandomCopyPaste(type));
        execute(sendMessage);
    }

    private StickerSet getFightStickers() throws TelegramApiException {
        return execute(new GetStickerSet(Randomizer.getRandomValueFormList(fightPacks)));
    }

    private  StickerSet getStickerSet(String name) throws TelegramApiException {
        return execute(new GetStickerSet(name));
    }

    private Sticker getSticker() throws TelegramApiException {
        return getSticker(getFightStickers());
    }

    private Sticker getSticker(StickerSet stickerSet){
        if(stickerSet == null) {
            return null;
        }
        return  Randomizer.getRandomValueFormList(stickerSet.getStickers());
    }

    private void sendFightSticker(Message message, boolean reply) throws TelegramApiException {
        sendSticker(message, getSticker(), reply);
    }

    private void sendSticker(Message message, Sticker sticker, boolean reply) throws TelegramApiException {
        if (sticker == null)
            return;
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(message.getChatId());
        if (reply)
            sendSticker.setReplyToMessageId(message.getMessageId());
        sendSticker.setSticker(sticker.getFileId());
        execute(sendSticker);
    }

    public Chats registerChat(Message message) {
        Chats chat = new Chats();
        chat.setChatId(message.getChatId());
        chat.setChatName(message.getChat().getTitle());
        chat = chatRepository.save(chat);
        registerUser(message, chat);
        return chat;
    }

    Users registerUser(Message message, Chats chat) {
        if (chat == null)
            chat = chatRepository.getByChatId(message.getChatId());
        User telegramUser = message.getFrom();
        Users user = new Users();
        user.setChat(chat);
        user.setUserId(telegramUser.getId());
        user.setUserName(telegramUser.getUserName());
        if(user.getUserName() == null){
            user.setUserName(telegramUser.getFirstName());
        }
        if(user.getUserName() == null){
            user.setUserName(Randomizer.getRandomValueFormList(userNames));
        }
        return userRepository.save(user);
    }



    private void roll(Message message, Roller roller) throws TelegramApiException {
        Chats chat = chatRepository.getByChatId(message.getChatId());
        if (chat == null)
            chat = registerChat(message);
        Users user = userRepository.getByChatIdAndUserId(chat.getId(), message.getFrom().getId());
        if (user == null)
            user = registerUser(message, chat);
        roller.roll(message, user);
    }

    private void getTop(Message message, Roller roller) throws TelegramApiException {
        Chats chat = chatRepository.getByChatId(message.getChatId());
        if(chat == null)
            sendMessage(message, "Никто ещё ничего не роллил");
        sendMessage(message, roller.getTop(userRepository.getAllByChat(chat)));
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
