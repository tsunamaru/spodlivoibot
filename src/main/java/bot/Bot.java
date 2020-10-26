package bot;


import com.google.inject.internal.cglib.core.internal.$CustomizerRegistry;
import database.DatabaseManager;
import database.models.Chats;
import database.models.Dicks;
import database.models.Users;
import dick.DickRoller;
import dick.Randomizer;
import org.apache.commons.io.FileUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.*;
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
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import utils.Logs;
import utils.Tools;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static utils.Tools.REPAIR_TEXT_MESSAGE;


public class Bot extends TelegramLongPollingBot {

    private static final String DVACH_URL = "https://2ch.hk/b/catalog.json";

    private final static ArrayList<String> fightPacks = new ArrayList<String>(){{
       add("sosatlezhatsosat");
       add("fightpics");
       add("test228idinaxui");
       add("davlyu");
       add("gasiki2");
       add("durkaebt");
       add("daEntoOn");
       add("Bodyafleks3");
       add("Rjaka228");
       add("Stickers_ebat");
       add("em_sho");
       add("vdyrky");
       add("onegaionegai");
       add("sp792a9133ad7d0b9144089d7350483d5c_by_stckrRobot");
    }};

    private JSONArray baby;
    private JSONArray dota;
    private JSONArray kolchan;
    private JSONArray olds;
    private JSONArray shizik;

    public Bot(){
        InputStream insultsStream = getClass().getResourceAsStream("baby" + ".json");
        JSONObject insultsJson = new JSONObject(Tools.convertStreamToString(insultsStream));
        baby = insultsJson.getJSONArray("baby");
        insultsStream = getClass().getResourceAsStream("dota" + ".json");
        insultsJson = new JSONObject(Tools.convertStreamToString(insultsStream));
        dota = insultsJson.getJSONArray("dota");
        insultsStream = getClass().getResourceAsStream("kolchan" + ".json");
        insultsJson = new JSONObject(Tools.convertStreamToString(insultsStream));
        kolchan = insultsJson.getJSONArray("kolchan");
        insultsStream = getClass().getResourceAsStream("olds" + ".json");
        insultsJson = new JSONObject(Tools.convertStreamToString(insultsStream));
        olds = insultsJson.getJSONArray("olds");
        insultsStream = getClass().getResourceAsStream("shizik" + ".json");
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
    }


    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
           /* try{
                Logs.log(message.getSticker().getSetName());
            }catch (Exception ignored){}*/
                if (message.isCommand()) {
                    String messageText = message.getText();
                    String command = "";
                    if (messageText.contains("@")) {
                        command = messageText.split("@")[0];
                        if (!messageText.split("@")[1].equals(getBotUsername()))
                            return;
                    } else {
                        command = messageText;
                    }

                    if (command.contains("/start")) {
                        startCommand(update.getMessage());
                        return;
                    } else {
                        if (!registerUser(update.getMessage(), null)) {
                            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                            sendMessage.setText("Сначала зарегистрируйте чат коммандой /start");
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                    if (command.contains("/dick")) {
                        rollDick(message);
                    } else if (command.contains("/fight")) {
                        if (message.isReply()) {
                            sendFightSticker(message.getReplyToMessage(), true);
                            deleteMessage(message);
                        } else
                            sendFightSticker(message, false);
                    } else if (command.contains("/test")) {
                        sendMessage(message, "Я работаю, а твой писюн - нет!");
                    } else if (command.contains("/topdicks")) {
                        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                        sendMessage.setText(getTopDicks(getChatById(message.getChatId())));
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else if (command.contains("/bred")) {
                        sendRandomThread(message);
                    } else if (command.contains("/shizik")) {
                        sendRandomCopypaste(CopypasteType.shizik, message);
                    } else if (command.contains("/olds")) {
                        sendRandomCopypaste(CopypasteType.olds, message);
                    } else if (command.contains("/kolchan")) {
                        sendRandomCopypaste(CopypasteType.kolchan, message);
                    } else if (command.contains("/dota")) {
                        sendRandomCopypaste(CopypasteType.dota, message);
                    } else if (command.contains("/baby")) {
                        sendRandomCopypaste(CopypasteType.baby, message);
                    } else if (command.contains("/webm")) {
                        sendRandomWebm(message);
                    } else if(command.contains("/videostats")){
                        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                        sendMessage.setText("Количество видео в обработке: " + videoStats);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (message.isReply()) {
                    if (message.getReplyToMessage().getFrom().getUserName().equals(getBotUsername()))
                        sendFightSticker(message, true);
                } else if (message.hasText()) {
                    if (message.getText().contains(System.getProperty("botname")))
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

                        e.printStackTrace();
                    }
                });
                thread.start();
            } else if (update.hasEditedMessage()) {
                // sendMessage(update.getEditedMessage(), "Анус себе отредактируй");
            }
        } catch (Exception e){
            
            e.printStackTrace();
        }
    }

    InlineQueryResultArticle article = new InlineQueryResultArticle();
    InlineQueryResultArticle article2 = new InlineQueryResultArticle();
    InlineQueryResultArticle article3 = new InlineQueryResultArticle();
    InlineQueryResultArticle article4 = new InlineQueryResultArticle();
    InlineQueryResultArticle article5 = new InlineQueryResultArticle();
    InlineQueryResultArticle repairArticle = new InlineQueryResultArticle();
    InlineQueryResultVideo randomWeb = new InlineQueryResultVideo();

    public static void writeErrorLog(String log){
/*        try(FileWriter writer = new FileWriter("/home/triangle/bot_log.txt", true))
        {
            writer.append(log).append("\n\n");
            writer.flush();
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }*/
    }

    private void sendMessage(Message message, String text){
        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            
            e.printStackTrace();
        }
    }

    private void deleteMessage(Message message){
        try {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(message.getChatId());
            deleteMessage.setMessageId(message.getMessageId());
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            
            e.printStackTrace();
        }
    }

    enum CopypasteType{
        baby,
        dota,
        kolchan,
        olds,
        shizik
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
            
            e.printStackTrace();
        }
    }

    public JSONArray  getBThreads() throws IOException {
        URL url = new URL(DVACH_URL);
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
                e.printStackTrace();
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
                                Logs.log(this, s);
                            p.waitFor();
                            p.destroy();
                        } catch (Exception e) {e.printStackTrace();}

                    } catch (Exception e) {
                        e.printStackTrace();
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
                                    Logs.log(this, s);
                                p.waitFor();
                                p.destroy();
                            } catch (Exception er) {e.printStackTrace();}
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
                        e.printStackTrace();
                    }
                } else {
                    SendVideo sendVideo = new SendVideo().setChatId(message.getChatId());
                    sendVideo.setReplyToMessageId(message.getMessageId());
                    sendVideo.setVideo(video);
                    execute(sendVideo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void sendRandomThread(final Message message){
        Thread thread = new Thread(() -> {
            String post = "", photo = "";
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
                    e.printStackTrace();
                }
            } catch (Exception e) {
                
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void sendFightSticker(Message message, boolean reply) {
        StickerSet stickerSet = null;
        try {
            stickerSet = execute(new GetStickerSet((String) Tools.getRandomValueFormArrayList(fightPacks)));
        } catch (TelegramApiException e) {
            
            e.printStackTrace();
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
                
                e.printStackTrace();
            }
        }
    }

    public void startCommand(Message message){
        try(Session session = DatabaseManager.getSession()) {
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            Chats chat = null;
            try {
                chat = (Chats) session.createQuery("from Chats as c where c.chatId = :chat").
                        setParameter("chat", message.getChatId()).list().get(0);
            }catch (Exception e){
                //e.printStackTrace();
            }
            if(chat == null){
                session.beginTransaction();
                sendMessage.setText("Ну что? Готовы к ещё одной порции подливы?");
                chat = new Chats();
                chat.setChatId(message.getChatId());
                chat.setChatName(message.getChat().getTitle());
                session.save(chat);
                session.getTransaction().commit();
            } else{
                sendMessage.setText("Ваш чат уже зарегистрирован!");
            }
            registerUser(message, chat);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean registerUser(Message message, Chats chat){
        try(Session session = DatabaseManager.getSession()) {
            if(chat == null){
                try {
                    chat = (Chats) session.createQuery("from Chats as c where c.chatId = :chat").
                            setParameter("chat", message.getChatId()).list().get(0);
                }catch (Exception e){
                    return false;
                    //e.printStackTrace();
                }
            }
            Users user = null;
            try {
                user = (Users) session.createQuery("from Users as c where c.userId = :user AND c.chatId = :chat").
                        setParameter("user", message.getFrom().getId()).
                        setParameter("chat", chat.getId()).list().get(0);
            }catch (Exception e){
               // e.printStackTrace();
            }
            if(user == null){
                session.beginTransaction();
                user = new Users();
                user.setChat(chat);
                user.setUserId(message.getFrom().getId());
                user.setUserName(message.getFrom().getUserName());
                session.save(user);
                session.getTransaction().commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private Chats getChatById(Long id){
        Chats chat = null;
        try(Session session = DatabaseManager.getSession()) {
            chat = (Chats) session.createQuery("from Chats as c where c.chatId = :chat").
                    setParameter("chat", id).list().get(0);
        }catch (Exception e){
            //e.printStackTrace();
        }
        return chat;
    }

    public String getTopDicks(Chats c){
        StringBuilder message = new StringBuilder();
        try(Session session = DatabaseManager.getSession()) {
            Chats chat = session.get(Chats.class, c.getId());
            int number = 1;
            ArrayList<Users> users = new ArrayList<>(chat.getUsers());
            ArrayList<Dicks> dicks = new ArrayList<>();
            for (Users user : users) {
                try {
                    dicks.add(new ArrayList<>(user.getDicks()).get(0));
                }catch (Exception ignored){ }
            }
            try {
                dicks.sort((d1, d2) -> Integer.compare(d2.getSize(), d1.getSize()));
            }catch (Exception ignored){}
            for (Dicks dick : dicks) {
                message.append(number).append(". ");
                message.append(dick.getUsers().getUserName());
                message.append(" - ").append(dick.getSize()).append("см;\n");
                number++;
            }
        }catch (Exception e){
           // e.printStackTrace();
        }
        if (message.toString().equals(""))
            message = new StringBuilder("Никто ещё не роллил хуй");
        else
            message.insert(0, "Топ писюнов: \n\n");
        return message.toString();
    }

    public void rollDick(Message message){
        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
        Users user = null;
        Chats chat = null;
        try(Session session = DatabaseManager.getSession()) {
            try {
                chat = (Chats) session.createQuery("from Chats as c where c.chatId = :chat").
                        setParameter("chat", message.getChatId()).list().get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                user = (Users) session.createQuery("from Users as c where c.userId = :user AND c.chatId = :chat").
                        setParameter("user", message.getFrom().getId()).
                        setParameter("chat", chat.getId()).list().get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String mess = new String(DickRoller.Holder.getInstance().roll(user, session).getBytes(),
                    StandardCharsets.UTF_8);
            sendMessage.setReplyToMessageId(message.getMessageId());
            sendMessage.setText(mess);
            sendMessage.enableMarkdown(true);
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Bot name
    @Override
    public String getBotUsername() {
        // return "spodlivoi_bot";
	return System.getProperty("botname");
    }

    //Bot token from @
    @Override
    public String getBotToken() {
        return System.getProperty("token");
    }
}
