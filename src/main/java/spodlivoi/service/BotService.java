package spodlivoi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.stickers.GetStickerSet;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedSticker;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.api.objects.stickers.StickerSet;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.database.entity.*;
import spodlivoi.database.enums.Copypaste;
import spodlivoi.database.enums.Gender;
import spodlivoi.database.repository.*;
import spodlivoi.interactor.CopyPasteInteractor;
import spodlivoi.interactor.DvachInteractor;
import spodlivoi.interactor.RollerInteractor;
import spodlivoi.message.Messages;
import spodlivoi.utils.Log;
import spodlivoi.utils.Randomizer;
import spodlivoi.utils.StringUtil;
import ws.schild.jave.EncoderException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BotService {

    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String botUserName;
    @Value("${telegram.bot.maxinline}")
    private int maxInlines;
    @Value("${telegram.bot.admin-chat-id}")
    private String adminChatId;
    @Value("${debug}")
    private boolean debug;

    private final static String ERROR = "org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException: Error sending message: [429] Too Many Requests: retry after ";
    private final static String ERROR_REGEX = "org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException: Error sending message: \\[429\\] Too Many Requests: retry after ";

    private final TelegramService telegramService;
    private final UserMessageRepository userMessageRepository;
    private final ChatRepository chatRepository;
    private final DickRepository dickRepository;
    private final AnusRepository anusRepository;
    private final VaginaRepository vaginaRepository;
    private final DdosRepository ddosRepository;
    private final UserRepository userRepository;
    private final DvachInteractor dvachInteractor;
    private final UserSettingsRepository userSettingsRepository;
    private final List<RollerInteractor> rollers;
    private final CopyPasteInteractor copyPasteInteractor;
    private final Messages messages;
    private final Log log;

    private final InlineQueryResultArticle repairArticle = new InlineQueryResultArticle();

    @PostConstruct
    public void initialization() {
        repairArticle.setId("228");
        repairArticle.setDescription("");
        repairArticle.setTitle("ЧИНИ!");
        InputTextMessageContent inputTextMessageContent = new InputTextMessageContent();
        inputTextMessageContent.setMessageText("<b>" + "ЧИНИ ".repeat(700) +"</b>");
        inputTextMessageContent.setParseMode(ParseMode.HTML);
        repairArticle.setInputMessageContent(inputTextMessageContent);
    }

    //@JmsListener(destination = "podlivaQueue", containerFactory = "myFactory")
    public void acceptQueue(Update update) throws InterruptedException {
        acceptUpdateAcync(update);
    }

    @Async
    public void acceptUpdateAcync(Update update) throws InterruptedException {
    //    log.debug(String.valueOf(update));
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                Optional<Ddos> ddos = ddosRepository.findByTelegramUserId(message.getFrom().getId().longValue());
                if(ddos.isPresent() && ddos.get().getActive()){
                    sendRandomCopypaste(ddos.get().getCopypaste(), message, true);
                }
                if (message.isCommand()) {
                    acceptCommand(message);
                } else if (message.isReply()) {
                    if (message.getReplyToMessage().getFrom() != null &&
                            message.getReplyToMessage().getFrom().getUserName() != null &&
                            message.getReplyToMessage().getFrom().getUserName().equals(botUserName))
                        sendFightSticker(message, true);
                } else if (message.hasText()) {
                    if (message.getText().contains(botToken))
                        sendFightSticker(message, true);
                    saveUserMessageAsync(message);
                } else if(message.hasDocument()) {
                    Document document = message.getDocument();
                    if (document.getMimeType().equalsIgnoreCase("video/webm")) {
                        GetFile getFile = GetFile.builder().fileId(document.getFileId()).build();
                        String filePath = telegramService.execute(getFile).getFilePath();
                        File outputFile = new File(filePath);
                        File file = telegramService.downloadFile(filePath, outputFile);
                        File output = new File(filePath.replaceAll("webm", "mp4"));
                        dvachInteractor.convertAndSendVideoAsync(message, file, output, "webm");
                    }
                }
            } else if (update.hasInlineQuery()) {

                AnswerInlineQuery answerInlineQuery;
                if (update.getInlineQuery().getQuery().toLowerCase().matches("fight") ||
                        update.getInlineQuery().getQuery().toLowerCase().matches("боевая") ||
                        update.getInlineQuery().getQuery().toLowerCase().matches("хрю")) {

                    int setCount = messages.getFightPacks().size();
                    int sets = Randomizer.getRandomNumberInRange(1, (int) Math.ceil((double) setCount / (double) maxInlines));
                    int start = maxInlines * (sets - 1);
                    int end = maxInlines * sets;
                    if (setCount < end)
                        end = setCount;
                    if (end - start != maxInlines)
                        start = start + (end - start) - maxInlines;
                    List<String> packs = messages.getFightPacks().subList(start, end);
                    List<InlineQueryResult> results = new ArrayList<>();
                    int i = 1;
                    for (String pack : packs) {
                        InlineQueryResultCachedSticker q = new InlineQueryResultCachedSticker();
                        q.setId(String.valueOf(i));
                        q.setStickerFileId(getSticker(getStickerSet(pack)).getFileId());
                        results.add(q);
                        i++;
                    }
                    answerInlineQuery = new AnswerInlineQuery();
                    answerInlineQuery.setCacheTime(0);
                    answerInlineQuery.setIsPersonal(true);
                    answerInlineQuery.setResults(results);
                    answerInlineQuery.setInlineQueryId(update.getInlineQuery().getId());
                } else {
                    List<InlineQueryResult> results = new ArrayList<>();
                    for(Copypaste copypaste : Copypaste.values()) {
                        InlineQueryResultArticle article = new InlineQueryResultArticle();
                        article.setId(String.valueOf(copypaste.getNumber()));
                        article.setTitle(copypaste.getName());
                        article.setDescription(copypaste.getDescription());
                        InputTextMessageContent inputTextMessageContent = new InputTextMessageContent();
                        inputTextMessageContent.setMessageText(copyPasteInteractor.getRandomCopyPaste(copypaste));
                        inputTextMessageContent.setParseMode(ParseMode.MARKDOWN);
                        article.setInputMessageContent(inputTextMessageContent);
                        results.add(article);
                    }
                    results.add(repairArticle);
                    answerInlineQuery = new AnswerInlineQuery();
                    answerInlineQuery.setCacheTime(0);
                    answerInlineQuery.setIsPersonal(true);
                    answerInlineQuery.setResults(results);
                    answerInlineQuery.setInlineQueryId(update.getInlineQuery().getId());
                }
               telegramService.execute( answerInlineQuery);
            } else if(update.hasCallbackQuery()){
                acceptCallbackQuery(update.getCallbackQuery());
            }
        }catch (TelegramApiException telegramApiException){
            String errorString = telegramApiException.toString();
            if(errorString.startsWith(ERROR)){
                int waitTime = Integer.parseInt(errorString.replaceFirst(ERROR_REGEX, ""));
                Thread.sleep(waitTime);
                acceptUpdateAcync(update);
            }else{
                log.error(telegramApiException, update);
            }
        } catch (Exception e){
            log.error(e, update);
        }
    }


    private void sendMessage(Message message, String text, boolean markDown) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        if (markDown)
            sendMessage.enableMarkdown(true);
       telegramService.execute( (sendMessage));
    }

    private void acceptCommand(Message message) throws TelegramApiException, IOException, EncoderException, InterruptedException {
        String messageText = message.getText();
        String command;
        if (messageText.contains("@")) {
            command = messageText.split("@")[0];
            if (!messageText.split("@")[1].equals(botUserName))
                return;
        } else {
            command = messageText;
        }
        if (messages.getCopypasteCommands().contains(command)) {
            sendRandomCopypaste(Copypaste.ofCommand(command), message, false);
        }
        if (command.contains("/stats"))
            showStat(message);
        switch (command) {
            case "/roll":
             //   log.debug("Start roll time: {}", System.currentTimeMillis());
                roll(message);
                break;
            case "/fight":
                if (message.isReply()) {
                    sendFightSticker(message.getReplyToMessage(), true);
                    deleteMessage(message);
                } else
                    sendFightSticker(message, false);
                break;
            case "/test":
                sendMessage(message, "Я работаю, а твой писюн - нет!", false);
                break;
            case "/top":
                getTop(message);
                break;
            case "/bred":
                dvachInteractor.sendThreadAsync(message);
                break;
            case "/webm":
                sendMessage(message, "Это операция может занять продолжительное время из-за перекодирования видео...", false);
                dvachInteractor.sendVideoAsync(message);
                break;
            case "/videostats":
                sendMessage(message, "Количество видео в обработке: " + dvachInteractor.getVideoStats(), false);
                break;
            case "/setting":
                showSetting(message);
                break;
            default:
                acceptAdminCommand(command, message);
                break;
        }

    }

    private void showStat(Message message) throws TelegramApiException {
        String username = message.getText().replaceFirst( "/stats ", "");
        if(username.isEmpty())
            return;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        Chats chat = chatRepository.getByChatId(message.getChatId());
        if (chat == null)
            chat = registerChat(message);
        Optional<Users> userOpional = userRepository.getByChatIdAndUserName(chat.getId(), username);
        if(userOpional.isEmpty())
            sendMessage(message, "Пользователь *" + username +"* не найден!", true);
        else {
            Users user = userOpional.get();
            if(user.getSettings() == null){
                UserSettings settings = new UserSettings();
                settings.setUser(user);
                settings.setRollAnus(true);
                settings.setRollDick(true);
                settings.setChangeSetting(false);
                user.setSettings(userSettingsRepository.save(settings));
            }
            sendMessage(message, messages.getSettingName(username, user.getSettings()), true);
        }
    }

    private void showSetting(Message message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        Users user = getUserByMessage(message);
        UserSettings settings;
        if(user.getSettings() == null){
            settings = new UserSettings();
            settings.setUser(user);
            settings.setRollAnus(true);
            settings.setRollDick(true);
            settings = userSettingsRepository.save(settings);
        }else{
            settings = user.getSettings();
        }
        settings.setChangeSetting(true);
        settings = userSettingsRepository.save(settings);
        sendMessage.setReplyMarkup(getSettingsReplyKeyboard(settings));
        sendMessage.setText(messages.getSettingName(message.getFrom().getUserName(), settings));
        sendMessage.enableMarkdown(true);
       telegramService.execute( sendMessage);
    }

    private ReplyKeyboard getSettingsReplyKeyboard(UserSettings settings){
        Long id = settings.getId();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        if(settings.getGender() != Gender.FEMALE) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(InlineKeyboardButton.builder()
                    .text(messages.getDickRollSetting(settings.isRollDick()))
                    .callbackData(messages.getSettingsButtonPrefix() + "1" + "_" + id).build());
            rowsInline.add(rowInline);
            List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
            rowInline1.add(InlineKeyboardButton.builder()
                    .text(messages.getAmputateDick())
                    .callbackData(messages.getSettingsButtonPrefix() + "11" + "_" + id).build());
            rowsInline.add(rowInline1);
        }
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(InlineKeyboardButton.builder()
                .text(messages.getAnusRollSetting(settings.isRollAnus()))
                .callbackData(messages.getSettingsButtonPrefix() + "2" + "_" + id).build());
        rowsInline.add(rowInline2);
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline3.add(InlineKeyboardButton.builder()
                .text(messages.getSewAnus())
                .callbackData(messages.getSettingsButtonPrefix() + "12" + "_" + id).build());
        rowsInline.add(rowInline3);
        if(settings.getGender() == Gender.FEMALE || settings.getGender() == Gender.FIGHT_HELICOPTER) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(InlineKeyboardButton.builder()
                    .text(messages.getVaginaRollSetting(settings.isRollVagina()))
                    .callbackData(messages.getSettingsButtonPrefix() + "3" + "_" + id).build());
            rowsInline.add(rowInline);
            List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
            rowInline1.add(InlineKeyboardButton.builder()
                    .text(messages.getSewVagina())
                    .callbackData(messages.getSettingsButtonPrefix() + "13" + "_" + id).build());
            rowsInline.add(rowInline1);
        }
        if(settings.isAccessGenderChange() || debug) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(InlineKeyboardButton.builder()
                    .text(messages.getChangeGender())
                    .callbackData(messages.getSettingsButtonPrefix() + "4" + "_" + id).build());
            rowsInline.add(rowInline);
        }
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder()
                .text(messages.getCancel())
                .callbackData(messages.getSettingsButtonPrefix() + "-1" + "_" + id).build());
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private void acceptAdminCommand(String command, Message message) throws TelegramApiException {
        if(!message.getChatId().toString().equals(adminChatId))
            return;
        if(command.contains(messages.getDdosActivatedCommand())){
            Long telegramId = Long.valueOf(command.replaceFirst(messages.getDdosActivatedCommand() + " ", ""));
            Optional<Ddos> ddosOptional = ddosRepository.findByTelegramUserId(telegramId);
            Long id;
            if(ddosOptional.isPresent()){
                id = ddosOptional.get().getId();
            } else{
                Ddos ddos = new Ddos();
                ddos.setActive(false);
                ddos.setTelegramUserId(telegramId);
                id = ddosRepository.save(ddos).getId();
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(adminChatId);
            sendMessage.setText(messages.getDdosSelectCopypastes());
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            for(Copypaste copypaste : Copypaste.values()) {
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(InlineKeyboardButton.builder()
                        .text(copypaste.getName())
                        .callbackData(messages.getDdosButtonPrefix() + copypaste.ordinal()+ "_" + id).build());
                rowsInline.add(rowInline);
            }
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(InlineKeyboardButton.builder()
                    .text("Отмена")
                    .callbackData(messages.getDdosButtonPrefix() + "-1" + "_" + id).build());
            rowsInline.add(rowInline);
            markupInline.setKeyboard(rowsInline);
            sendMessage.setReplyMarkup(markupInline);
           telegramService.execute( sendMessage);
        } else if(command.contains(messages.getDdosdeactivatedCommand())){
            String telegramId = command.replaceFirst(messages.getDdosdeactivatedCommand() + " ", "");
            Optional<Ddos> ddosOptional = ddosRepository.findByTelegramUserId(Long.valueOf(telegramId));
            if(ddosOptional.isPresent()){
                Ddos ddos = ddosOptional.get();
                if(ddos.getActive()){
                    ddos.setActive(false);
                    ddosRepository.save(ddos);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(adminChatId);
                    sendMessage.setText(messages.getDdosdeactivatedMessage(getUserNameById(ddos.getTelegramUserId())));
                   telegramService.execute( sendMessage);
                    return;
                }
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(adminChatId);
            sendMessage.setText(messages.getDdosAlreadyDiactiveMessage());
           telegramService.execute( sendMessage);
        }
    }

    private String getUserNameById(Long telegramId) {
        List<Users> users = userRepository.getByUserId(telegramId);
        String name = "";
        if (users.isEmpty())
            name = "(" + telegramId + ")";
        else {
            Users user = users.get(0);
            if (!StringUtil.isNullOrWhiteSpace(user.getUserName()))
                name += user.getUserName();
            else {
                name += !StringUtil.isNullOrWhiteSpace(user.getFirstName()) ? user.getFirstName() + " " : "";
                name += !StringUtil.isNullOrWhiteSpace(user.getLastName()) ? user.getLastName() : "";
            }
            name += " (" + telegramId + ")";
        }
        return name;
    }

    private void acceptCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        if (callbackQuery.getData().startsWith(messages.getDdosButtonPrefix())){
            setDdos(callbackQuery.getData(), callbackQuery.getMessage().getMessageId());
        } else if (callbackQuery.getData().startsWith(messages.getSettingsButtonPrefix())){
            setSettings(callbackQuery.getData(), callbackQuery);
        }
    }

    private void setSettings(String data, CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        String[] ids = data.replaceAll(messages.getSettingsButtonPrefix(), "").split("_");
        int actionId = Integer.parseInt(ids[0]);
        Long id = Long.valueOf(ids[1]);
        UserSettings userSettings = userSettingsRepository.getById(id);
        Users user = userSettings.getUser();
        if (userSettings.getUser().getUserId() != callbackQuery.getFrom().getId())
            return;

        switch (actionId) {
            case -1:
               telegramService.execute( DeleteMessage.builder()
                        .chatId(message.getChatId().toString()).messageId(message.getMessageId()).build());
                userSettings.setChangeSetting(false);
                userSettingsRepository.save(userSettings);
                return;
            case 1:
                userSettings.setRollDick(!userSettings.isRollDick());
                break;
            case 2:
                userSettings.setRollAnus(!userSettings.isRollAnus());
                break;
            case 3:
                userSettings.setRollVagina(!userSettings.isRollVagina());
                break;
            case 4:
                Gender gender = Gender.getRandomGender();
                userSettings.setGender(gender);
                userSettings.setAccessGenderChange(false);
                if(gender == Gender.MALE)
                    userSettings.setRollVagina(false);
                if(gender == Gender.FEMALE)
                    userSettings.setRollDick(false);
                break;
            case 11:
                Dicks dick = user.getDick();
                if(dick == null){
                    dick = new Dicks();
                    dick.setUser(userSettings.getUser());
                }
                dick.setSize(0);
                dickRepository.save(dick);
                break;
            case 12:
                Anus anus = user.getAnus();
                if(anus == null){
                    anus = new Anus();
                    anus.setUser(userSettings.getUser());
                }
                anus.setSize(0);
                anusRepository.save(anus);
                break;
            case 13:
                Vagina vagina = user.getVagina();
                if(vagina == null){
                    vagina = new Vagina();
                    vagina.setUser(userSettings.getUser());
                }
                vagina.setSize(0);
                vaginaRepository.save(vagina);
                break;
            default:
                return;
        }

        userSettings = userSettingsRepository.save(userSettings);

       telegramService.execute( EditMessageText.builder().chatId(message.getChatId().toString()).messageId(message.getMessageId())
                .replyMarkup((InlineKeyboardMarkup) getSettingsReplyKeyboard(userSettings)).text(
                        messages.getSettingName(userSettings.getUser().getUserName(), userSettings)).parseMode(ParseMode.MARKDOWN).build());
    }

    private void setDdos(String data, Integer messageId) throws TelegramApiException {
        String[] ids = data.replaceAll(messages.getDdosButtonPrefix(), "").split("_");
        int actionId = Integer.parseInt(ids[0]);
        Long id = Long.valueOf(ids[1]);
        if(actionId == -1){
           telegramService.execute( DeleteMessage.builder()
                    .chatId(adminChatId).messageId(messageId).build());
           return;
        }
        Ddos ddos = ddosRepository.getById(id);
        ddos.setCopypaste(Copypaste.values()[actionId]);
        ddos.setActive(true);
        ddosRepository.save(ddos);
       telegramService.execute( EditMessageText.builder().chatId(adminChatId).messageId(messageId).replyMarkup(null).text(
                messages.getDdosActivatedMessage(getUserNameById(ddos.getTelegramUserId()))).build());
    }

    private void deleteMessage(Message message) throws TelegramApiException {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
       telegramService.execute( deleteMessage);
    }



    private void sendRandomCopypaste(Copypaste type, Message message, boolean reply) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        if(reply){
            sendMessage.setReplyToMessageId(message.getMessageId());
        } else if (message.isReply()) {
            sendMessage.setReplyToMessageId(message.getReplyToMessage().getMessageId());
            deleteMessage(message);
        }
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setText(copyPasteInteractor.getRandomCopyPaste(type));
       telegramService.execute( sendMessage);
    }

    private StickerSet getFightStickers() throws TelegramApiException {
        return telegramService.execute(new GetStickerSet(Randomizer.getRandomValueFromList(messages.getFightPacks())));
    }

    private  StickerSet getStickerSet(String name) throws TelegramApiException {
        return telegramService.execute(new GetStickerSet(name));
    }

    private Sticker getSticker() throws TelegramApiException {
        return getSticker(getFightStickers());
    }

    private Sticker getSticker(StickerSet stickerSet){
        if(stickerSet == null) {
            return null;
        }
        return  Randomizer.getRandomValueFromList(stickerSet.getStickers());
    }

    private void sendFightSticker(Message message, boolean reply) throws TelegramApiException {
        sendSticker(message, getSticker(), reply);
    }

    private void sendSticker(Message message, Sticker sticker, boolean reply) throws TelegramApiException {
        if (sticker == null)
            return;
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(String.valueOf(message.getChatId()));
        if (reply)
            sendSticker.setReplyToMessageId(message.getMessageId());
        InputFile inputFile = new InputFile();
        inputFile.setMedia(sticker.getFileId());
        sendSticker.setSticker(inputFile);
       telegramService.execute( sendSticker);
    }

    public Chats registerChat(Message message) {
        Chats chat = new Chats();
        chat.setChatId(message.getChatId());
        chat.setChatName(message.getChat().getTitle());
        chat = chatRepository.save(chat);
        registerUser(message, chat, null);
        return chat;
    }

    private Users registerUser(Message message, Chats chat, Users currentUser) {
        if (chat == null)
            chat = chatRepository.getByChatId(message.getChatId());
        User telegramUser = message.getFrom();
        Users user;
        user = Objects.requireNonNullElseGet(currentUser, Users::new);
        user.setChat(chat);
        user.setUserId(telegramUser.getId());
        user.setUserName(telegramUser.getUserName());
        user.setFirstName(telegramUser.getFirstName());
        user.setLastName(telegramUser.getLastName());
        if(user.getUserName() == null){
            user.setUserName(telegramUser.getFirstName());
        }
        if(user.getUserName() == null){
            user.setUserName(Randomizer.getRandomValueFromList(messages.getRandomUserNames()));
        }
        return userRepository.save(user);
    }


    public Users getUserByMessage(Message message){
        Chats chat = chatRepository.getByChatId(message.getChatId());
        if (chat == null)
            chat = registerChat(message);
        Users user = userRepository.getByChatIdAndUserId(chat.getId(), message.getFrom().getId());
        if (user == null)
            user = registerUser(message, chat, null);
        if(user.getUpdatedAt() == null || user.getUpdatedAt().isAfter(user.getUpdatedAt().plusDays(1))){
            registerUser(message, chat, user);
        }
        return user;
    }


    private void roll(Message message) throws TelegramApiException {
        for(RollerInteractor roller : rollers)
            roller.roll(message, getUserByMessage(message));
    }

    private void getTop(Message message) throws TelegramApiException {
        Chats chat = chatRepository.getByChatId(message.getChatId());
        if(chat == null)
            sendMessage(message, "Никто ещё ничего не роллил", false);
        for(RollerInteractor roller : rollers) {
            log.debug("Get top from roller {}", roller);
            sendMessage(message, roller.getTop(userRepository.getAllByChat(chat)), false);
        }
    }

    @Async
    void saveUserMessageAsync(Message message){
        Users user = getUserByMessage(message);
        UserMessage userMessage = new UserMessage();
        userMessage.setUser(user);
        userMessage.setChat(user.getChat());
        userMessage.setMessageId(String.valueOf(message.getMessageId()));
        userMessageRepository.save(userMessage);
    }

}
