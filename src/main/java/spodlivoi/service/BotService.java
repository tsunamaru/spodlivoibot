package spodlivoi.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
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
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultVideo;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.api.objects.stickers.StickerSet;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.entity.Chats;
import spodlivoi.entity.Users;
import spodlivoi.enums.CopypasteType;
import spodlivoi.repository.ChatRepository;
import spodlivoi.repository.UserRepository;
import spodlivoi.utils.Randomizer;
import spodlivoi.utils.Tools;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static spodlivoi.utils.Tools.REPAIR_TEXT_MESSAGE;

@Component
@Slf4j
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
    @Value("${dvach.url}")
    private String dvachUrl;
    @Value("${telegram.bot.sticker-packs}")
    private ArrayList<String> fightPacks;
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

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DickService dickService;
    @Autowired
    private AnusService anusService;



    @PostConstruct
    public void initialization() throws IOException {
        InputStream insultsStream = babyFile.getInputStream();
        JSONObject insultsJson = new JSONObject(Tools.convertStreamToString(insultsStream));
        baby = insultsJson.getJSONArray("baby");
        insultsStream = dotaFile.getInputStream();
        insultsJson = new JSONObject(Tools.convertStreamToString(insultsStream));
        dota = insultsJson.getJSONArray("dota");
        insultsStream = kolchanFile.getInputStream();
        insultsJson = new JSONObject(Tools.convertStreamToString(insultsStream));
        kolchan = insultsJson.getJSONArray("kolchan");
        insultsStream = oldsFile.getInputStream();
        insultsJson = new JSONObject(Tools.convertStreamToString(insultsStream));
        olds = insultsJson.getJSONArray("olds");
        insultsStream = shizikFile.getInputStream();
        insultsJson = new JSONObject(Tools.convertStreamToString(insultsStream));
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
        repairArticle.setInputMessageContent(new InputTextMessageContent().setMessageText(REPAIR_TEXT_MESSAGE)
                .setParseMode(ParseMode.HTML));

        randomWeb.setId("1");
        randomWeb.setDescription("Рандомный Webm из /b");
        randomWeb.setTitle("цуим");
        log.info("Бот запущен");
    }

    @Override
    public void onUpdateReceived(Update update) {

        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                if (message.isCommand()) {
                    acceptCommand(message);
                } else if (message.isReply()) {
                    if (message.getReplyToMessage().getFrom().getUserName().equals(getBotUsername()))
                        sendFightSticker(message, true);
                } else if (message.hasText()) {
                    if (message.getText().contains(getBotUsername()))
                        sendFightSticker(message, true);
                }
            } else if (update.hasInlineQuery()) {
                Thread thread = new Thread(() -> {
                    AnswerInlineQuery answerInlineQuery;
                    if(update.getInlineQuery().getQuery().equals("webm") ||
                            update.getInlineQuery().getQuery().equals("video")){
                        List<InlineQueryResult> results = new ArrayList<>();

                        String video = getWebmUrl();
                        if(video.contains(".webm")){
                            InlineQueryResultArticle article = new InlineQueryResultArticle();
                            article.setId("1");
                            article.setDescription("Рандомный Webm из /b");
                            article.setTitle("цуим");
                            article.setInputMessageContent(new InputTextMessageContent().
                                    setMessageText("Извини, пашеграм не поддерживает webm: " + video));
                            results.add(article);
                        } else {
                            String thumb = video;
                            thumb = thumb.replaceAll("/b/src/", "/b/thumb/");
                            thumb = thumb.replaceAll(".mp4", "s.jpg");
                            randomWeb.setVideoUrl(video);
                            randomWeb.setThumbUrl(thumb);
                            randomWeb.setMimeType("video/mp4");
                            results.add(randomWeb);
                        }
                        answerInlineQuery = new AnswerInlineQuery().setCacheTime(0)
                                .setPersonal(true)
                                .setResults(results)
                                .setInlineQueryId(update.getInlineQuery().getId());
                    }else {
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
                    try {
                        execute(answerInlineQuery);
                    } catch (Exception e) {

                        log.error("Error: ", e);
                    }
                });
                thread.start();
            }
        } catch (Exception e){

            log.error("Error: ", e);
        }
    }

    InlineQueryResultArticle article = new InlineQueryResultArticle();
    InlineQueryResultArticle article2 = new InlineQueryResultArticle();
    InlineQueryResultArticle article3 = new InlineQueryResultArticle();
    InlineQueryResultArticle article4 = new InlineQueryResultArticle();
    InlineQueryResultArticle article5 = new InlineQueryResultArticle();
    InlineQueryResultArticle repairArticle = new InlineQueryResultArticle();
    InlineQueryResultVideo randomWeb = new InlineQueryResultVideo();

    private void sendMessage(Message message, String text){
        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error: ", e);
        }
    }

    private void acceptCommand(Message message){
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
                roll(message, dickService);
                break;
            case "/anus":
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
                getDickTop(message);
                break;
            case "/topanuses":
                getAnusTop(message);
                break;
            case "/bred":
                sendRandomThread(message);
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
                sendRandomWebm(message);
                break;
            case "/videostats":
                sendMessage(message, "Количество видео в обработке: " + videoStats);
                break;
        }
    }

    private void deleteMessage(Message message){
        try {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(message.getChatId());
            deleteMessage.setMessageId(message.getMessageId());
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error("Error: ", e);
        }
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
        assert getJSONCopyPaste(type) != null;
        int rand = Randomizer.getRandomNumberInRange(0, getJSONCopyPaste(type).length() - 1);
        assert getJSONCopyPaste(type) != null;
        return  getJSONCopyPaste(type).getString(rand);
    }

    private void sendRandomCopypaste(CopypasteType type, Message message){

        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
        if(message.isReply()){
            sendMessage.setReplyToMessageId(message.getReplyToMessage().getMessageId());
            deleteMessage(message);
        }
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setText(getRandomCopyPaste(type));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error: ", e);
        }
    }

    public JSONArray  getBThreads() throws IOException {
        URL url = new URL(dvachUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(connection.getInputStream());
        JSONObject answer = new JSONObject(Tools.convertStreamToString(in));
        JSONArray answerArray = answer.getJSONArray("threads");
        in.close();
        return answerArray;
    }

    public String getWebmUrl(){
        String video = "";
        try {
            JSONArray answerArray = getBThreads();

            String threadNumber = "";
            for (int i = 0; i < answerArray.length(); i++) {
                JSONObject postJson = answerArray.getJSONObject(i);
                String subject = postJson.getString("subject");
                if (subject.toLowerCase().contains("webm thread") || subject.toLowerCase().contains("webm-thread")
                        || subject.toLowerCase().contains("цуиь thread") || subject.toLowerCase().contains("цуиь-thread")
                        || subject.toLowerCase().contains("webm тред") || subject.toLowerCase().contains("webm-тред")
                        || subject.toLowerCase().contains("цуиь тред") || subject.toLowerCase().contains("цуиь-тред")) {
                    threadNumber = postJson.getString("num");
                    break;
                }
            }

            URL url = new URL("https://2ch.hk/makaba/mobile.fcgi?task=get_thread&board=b&thread="
                    + threadNumber + "&post=0");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            JSONArray videoPosts = new JSONArray(Tools.convertStreamToString(in));
            in.close();

            while (video.equals("")) {
                int r = Randomizer.getRandomNumberInRange(0, videoPosts.length() - 1);
                JSONObject postJson = videoPosts.getJSONObject(r);
                JSONArray files = postJson.getJSONArray("files");
                if (!files.isEmpty())
                    video = "https://2ch.hk" + files.getJSONObject(0).getString("path");
                if(!video.contains(".webm") && !video.contains(".mp4"))
                    video = "";

            }

            return video;

        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    }

    private int videoStats = 0;

    public void sendRandomWebm(final Message message) {

        Thread thread = new Thread(() -> {
            String filename = String.valueOf(Randomizer.getRandomNumberInRange(0, 100000));
            String video = getWebmUrl();
            try {
                if(video.contains(".webm")){

                    try{
                        FileUtils.copyURLToFile(
                                new URL(video),
                                new File("/tmp/" + filename + ".webm"));
                        Process p;
                        try {
                            p = Runtime.getRuntime().exec(
                                    "ffmpeg -hide_banner -i /tmp/" + filename + ".webm " +
                                            "-acodec copy -vcodec copy -strict -2 -f mp4 /tmp/" + filename + ".mp4");
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(p.getInputStream()));
                            String s;
                            while ((s = br.readLine()) != null)
                                log.info(s);
                            p.waitFor();
                            p.destroy();
                        } catch (Exception e) {log.error("Error: ", e);}

                    } catch (Exception e) {
                        log.error("Error: ", e);
                    }
                    video = "/tmp/" + filename + ".mp4";
                    SendVideo sendVideo = new SendVideo().setChatId(message.getChatId());
                    sendVideo.setReplyToMessageId(message.getMessageId());
                    sendVideo.setVideo(new File(video));
                    try {
                        execute(sendVideo);
                    }catch (TelegramApiException e){
                        if(e.toString().equals("org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException: Error sending video: [400] Bad Request: file must be non-empty")){
                            Process p;
                            try {
                                SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                                sendMessage.setReplyToMessageId(message.getMessageId());
                                sendMessage.setText("Какой-то уебан закодировал видео в vp8...\nПодожди. Мне нужно немного времени, чтоб исправить это.");
                                p = Runtime.getRuntime().exec(
                                        "rm -rf /tmp/" + filename + ".mp4");
                                p.waitFor();
                                p.destroy();
                                try {
                                    execute(sendMessage);
                                }catch (TelegramApiException err){
                                    err.printStackTrace();
                                }
                                videoStats++;
                                p = Runtime.getRuntime().exec(
                                        "ffmpeg -hide_banner -i /tmp/" + filename + ".webm " +
                                                "-acodec copy -vcodec libx264 -strict -2 -f mp4 /tmp/" + filename + ".mp4");
                                BufferedReader br = new BufferedReader(
                                        new InputStreamReader(p.getInputStream()));
                                String s;
                                while ((s = br.readLine()) != null)
                                    log.info(s);
                                p.waitFor();
                                p.destroy();
                            } catch (Exception er) {log.error("Error: ", e);}
                            sendVideo = new SendVideo().setChatId(message.getChatId());
                            sendVideo.setReplyToMessageId(message.getMessageId());
                            sendVideo.setVideo(new File(video));
                            execute(sendVideo);
                            videoStats--;
                        }
                    }
                    Process p;
                    try {
                        p = Runtime.getRuntime().exec(
                                "rm -rf /tmp/" + filename + ".webm");
                        p.waitFor();
                        p.destroy();
                        p = Runtime.getRuntime().exec(
                                "rm -rf /tmp/" + filename + ".mp4");
                        p.waitFor();
                        p.destroy();
                    } catch (Exception e) {
                        log.error("Error: ", e);
                    }
                } else {
                    SendVideo sendVideo = new SendVideo().setChatId(message.getChatId());
                    sendVideo.setReplyToMessageId(message.getMessageId());
                    sendVideo.setVideo(video);
                    execute(sendVideo);
                }
            } catch (Exception e) {
                log.error("Error: ", e);
            }
        });
        thread.start();
    }

    public void sendRandomThread(final Message message){
        Thread thread = new Thread(() -> {
            String post, photo = "";
            try {

                JSONArray answerArray = getBThreads();

                int r = Randomizer.getRandomNumberInRange(0, answerArray.length() - 1);
                JSONObject postJson = answerArray.getJSONObject(r);
                post = postJson.getString("comment");
                post = post.replaceAll("<br>", "\n");
                post = post.replaceAll("<b>", "*");
                post = post.replaceAll("</b>", "*");
                post = post.replaceAll("<i>", "_");
                post = post.replaceAll("</i>", "_");
                post = post.replaceAll("<[a-zA-Z0-9=\\-\".,/_ ]+>", "");
                post += "\n\nЧитать подробнее: https://2ch.hk/b/res/" + answerArray.getJSONObject(r).getString("num")
                        + ".html";

                post = "<i>" + postJson.getString("date") + "</i>\n\n" +  post;
                JSONArray files = postJson.getJSONArray("files");

                if(!files.isEmpty()){
                    photo = "https://2ch.hk/" + files.getJSONObject(0).getString("path");
                }

                SendMessage sendMessage = null;
                SendPhoto sendPhoto = null;

                if(photo.equals("")) {
                    sendMessage = new SendMessage().setChatId(message.getChatId());
                    sendMessage.setParseMode(ParseMode.HTML);
                    sendMessage.setText(post);
                    sendMessage.enableWebPagePreview();
                }else {
                    sendPhoto = new SendPhoto().setChatId(message.getChatId());
                    sendPhoto.setParseMode(ParseMode.HTML);
                    if(post.length() < 1024)
                        sendPhoto.setCaption(post);
                    else{
                        sendMessage = new SendMessage().setChatId(message.getChatId());
                        sendMessage.setParseMode(ParseMode.HTML);
                        sendMessage.setText(post);
                        sendMessage.enableWebPagePreview();
                    }
                    sendPhoto.setPhoto(photo);
                }
                try {
                    if(photo.equals(""))
                        execute(sendMessage);
                    else {
                        execute(sendPhoto);
                        if(post.length() >= 1024)
                            execute(sendMessage);
                    }
                } catch (TelegramApiException e) {
                    log.error("Error: ", e);
                }
            } catch (Exception e) {
                log.error("Error: ", e);
            }
        });
        thread.start();
    }

    private void sendFightSticker(Message message, boolean reply) {
        StickerSet stickerSet = null;
        try {
            stickerSet = execute(new GetStickerSet((String) Tools.getRandomValueFormArrayList(fightPacks)));
        } catch (TelegramApiException e) {
            log.error("Error: ", e);
        }
        sendRandomSticker(message, stickerSet, reply);
    }

    private void sendRandomSticker(Message message, StickerSet stickerSet, boolean reply) {
        if(stickerSet != null) {
            SendSticker sendSticker = new SendSticker();
            sendSticker.setChatId(message.getChatId());
            if(reply)
                sendSticker.setReplyToMessageId(message.getMessageId());
            sendSticker.setSticker(((Sticker) Tools.getRandomValueFormArrayList(stickerSet.getStickers())).getFileId());
            try {
                execute(sendSticker);
            } catch (TelegramApiException e) {
                log.error("Error: ", e);
            }
        }
    }

    public Chats registerChat(Message message) {
        Chats chat = new Chats();
        chat.setChatId(message.getChatId());
        chat.setChatName(message.getChat().getTitle());
        chat = chatRepository.save(chat);
        registerUser(message, chat);
        return chat;
    }

    public Users registerUser(Message message, Chats chat) {
        if (chat == null)
            chat = chatRepository.getByChatId(message.getChatId());
        Users user = new Users();
        user.setChat(chat);
        user.setUserId(message.getFrom().getId());
        user.setUserName(message.getFrom().getUserName());
        return userRepository.save(user);
    }



    private void roll(Message message, Roller roller) {
        try {
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());

            Chats chat = chatRepository.getByChatId(message.getChatId());
            if(chat == null)
                chat = registerChat(message);
            Users user = userRepository.getByChatIdAndUserId(chat.getId(), message.getFrom().getId());
            if(user == null)
                user = registerUser(message, chat);

            String mess = new String(roller.roll(user).getBytes(),
                    StandardCharsets.UTF_8);
            sendMessage.setReplyToMessageId(message.getMessageId());
            sendMessage.setText(mess);
            sendMessage.enableMarkdown(true);
            execute(sendMessage);
        }catch (Exception e){
            log.error("Error: ", e);
        }
    }

    private void getDickTop(Message message){
        Chats chat = chatRepository.getByChatId(message.getChatId());
        if(chat == null)
            sendMessage(message, "Никто ещё ничего не роллил");
        sendMessage(message, dickService.getTop(userRepository.getAllByChat(chat)));
    }

    private void getAnusTop(Message message){
        Chats chat = chatRepository.getByChatId(message.getChatId());
        if(chat == null)
            sendMessage(message, "Никто ещё ничего не роллил");
        sendMessage(message, anusService.getTop(userRepository.getAllByChat(chat)));
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
