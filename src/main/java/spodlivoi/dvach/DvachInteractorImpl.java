package spodlivoi.dvach;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.github.furstenheim.CopyDown;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import spodlivoi.service.TelegramService;
import spodlivoi.utils.Randomizer;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.ArgType;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.ValueArgument;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.encode.enums.X264_PROFILE;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DvachInteractorImpl implements DvachInteractor {

    private final TelegramService telegramService;
    private final DvachClient dvachClient;

    private int videoStats = 0;

    @Override
    @Async
    public void sendThreadAsync(Message message) throws IOException, TelegramApiException {
        String post, photo = "";
        JsonNode postJson = null;
        String link = null;
        while (!(photo.contains(".jpg") || photo.contains(".png") || photo.contains(".jpeg"))) {
            var answerArray = getBThreads();
            int r = Randomizer.getRandomNumberInRange(0, answerArray.size() - 1);
            postJson = answerArray.get(r);
            link = "https://2ch.hk/b/res/" + answerArray.get(r).get("num").asText()
                    + ".html";
            var files = (ArrayNode) postJson.get("files");
            if (!files.isEmpty()) {
                photo = "https://2ch.hk/" + files.get(0).get("path").asText();
            }
        }
        post = postJson.get("comment").asText();
        CopyDown converter = new CopyDown();
        post = converter.convert(post);
        post = post.replaceAll("\\*\\*", "*");
        post = post.replaceAll("__", "_");
        post = post.replaceAll("\\\\([$&+,:;=?@#|'<>.\\-^*()%!])", "$1");
        if (post.length() > 900) {
            post = post.substring(0, 900) + "...";
        }
        post += "\n\nПерейти в тред: " + link;
        post = "_" + postJson.get("date").asText() + "_\n\n" + post;


        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(message.getChatId()));
        sendPhoto.setReplyToMessageId(message.getMessageId());
        sendPhoto.setParseMode(ParseMode.MARKDOWN);
        sendPhoto.setCaption(post);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(photo);
        sendPhoto.setPhoto(inputFile);
        telegramService.execute(sendPhoto);
    }

    @Override
    @Async
    public void sendVideoAsync(Message message) throws IOException {
        try {
            String filename = String.valueOf(Randomizer.getRandomNumberInRange(0, 100000));
            String video = getWebmUrl();
            if (video != null) {
                String source;
                String format;
                if (video.contains(".mp4")) {
                    format = "mp4";
                } else {
                    format = "webm";
                }
                source = "/tmp/" + filename + "." + format;
                File sourceVideo = new File(source);
                FileUtils.copyURLToFile(new URL(video), sourceVideo);
                String target = "/tmp/" + filename + "(target).mp4";

                File targetVideo = new File(target);
                convertAndSendVideoAsync(message, sourceVideo, targetVideo, format);
            } else {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(String.valueOf(message.getChatId()))
                        .replyToMessageId(message.getMessageId())
                        .text("Абу спиздил все шебм с двачей...")
                        .build();
                telegramService.execute(sendMessage);
            }
        } catch (IOException | TelegramApiException e) {
            sendVideoAsync(message);
        } finally {
            if (videoStats > 0) {
                videoStats--;
            }
        }
    }

    @Override
    @Async
    public void convertAndSendVideoAsync(Message message, File sourceVideo, File targetVideo, String format) {
        try {
            while (videoStats >= Runtime.getRuntime().availableProcessors()) {
                log.info("Все потоки заняты. Ждём 5 секунд");
                Thread.sleep(5000);
            }
            videoStats++;
            VideoAttributes videoAttributes = new VideoAttributes();
            videoAttributes.setCodec("libx264");
            videoAttributes.setX264Profile(X264_PROFILE.MAIN);
            videoAttributes.setQuality(15);
            videoAttributes.setFaststart(true);

            AudioAttributes audioAttributes = new AudioAttributes();
            audioAttributes.setCodec("aac");
            audioAttributes.setChannels(2);
            audioAttributes.setQuality(15);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setInputFormat(format);
            attrs.setMapMetaData(false);
            attrs.setOutputFormat("mp4");
            attrs.setVideoAttributes(videoAttributes);
            attrs.setAudioAttributes(audioAttributes);

            //Encode
            Encoder.addOptionAtIndex(new ValueArgument(ArgType.OUTFILE, "-vf", ea ->
                    Optional.of("pad=ceil(iw/2)*2:ceil(ih/2)*2")), 30);
            Encoder encoder = new Encoder();

            encoder.encode(new MultimediaObject(sourceVideo), targetVideo, attrs);

            if (targetVideo.exists()) {

                SendVideo sendVideo = new SendVideo();
                sendVideo.setChatId(String.valueOf(message.getChatId()));
                sendVideo.setReplyToMessageId(message.getMessageId());
                InputFile inputFile = new InputFile();
                inputFile.setMedia(targetVideo);
                sendVideo.setVideo(inputFile);

                telegramService.execute(sendVideo);
                if (sourceVideo.exists()) {
                    sourceVideo.delete();
                }
                if (targetVideo.exists()) {
                    targetVideo.delete();
                }
                deleteVideo(sendVideo);
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке видео", e);

            var errorMessage = new SendMessage();
            errorMessage.setChatId(String.valueOf(message.getChatId()));
            errorMessage.setReplyToMessageId(message.getMessageId());
            if (e instanceof TelegramApiRequestException) {
                if (e.getMessage().contains("Request Entity Too Large")) {
                    errorMessage.setText(
                            new String("Выходное видео слишком большое. Ты что собрался пихать его в зад?".getBytes(),
                                    StandardCharsets.UTF_8));
                } else {
                    errorMessage.setText(new String("Телеграм забанил  bnjujdjt видео, поэтому иди нахуй".getBytes(),
                            StandardCharsets.UTF_8));
                }
            } else {
                errorMessage.setText(
                        new String("А за щеку тебе не перекодировать?".getBytes(), StandardCharsets.UTF_8));
            }
            try {
                telegramService.execute(errorMessage);
            } catch (Exception ignore) {
            }
        } finally {
            if (videoStats > 0) {
                videoStats--;
            }
        }
    }

    private void deleteVideo(SendVideo sendVideo) {
        File file = sendVideo.getVideo().getNewMediaFile();
        try {
            file.delete();
        } catch (Exception ignored) {
        }
    }

    private ArrayNode getBThreads() throws IOException {
        JsonNode answer = dvachClient.getThreads();
        return ((ArrayNode) answer.get("threads"));
    }


    private String getWebmUrl() throws IOException {
        String video = "";
        var answerArray = getBThreads();

        String threadNumber = "";
        for (int i = 0; i < answerArray.size(); i++) {
            JsonNode postJson = answerArray.get(i);
            String subject = postJson.get("subject").asText();
            if (subject.toLowerCase().contains("webm thread") || subject.toLowerCase().contains("webm-thread")
                    || subject.toLowerCase().contains("цуиь thread") || subject.toLowerCase().contains("цуиь-thread")
                    || subject.toLowerCase().contains("webm тред") || subject.toLowerCase().contains("webm-тред")
                    || subject.toLowerCase().contains("цуиь тред") || subject.toLowerCase().contains("цуиь-тред")) {
                threadNumber = postJson.get("num").asText();
                break;
            }
        }
        if (threadNumber.isEmpty() || threadNumber.isBlank()) {
            return null;
        }

        var videoPosts = dvachClient.getThread(threadNumber);

        while (video.equals("")) {
            int r = Randomizer.getRandomNumberInRange(0, videoPosts.size() - 1);
            var postJson = videoPosts.get(r);
            var files = (ArrayNode) postJson.get("files");
            if (!files.isEmpty()) {
                video = "https://2ch.hk" + files.get(0).get("path").asText();
            }
            if (!video.contains(".webm") && !video.contains(".mp4")) {
                video = "";
            }

        }

        return video;
    }

    @Override
    public int getVideoStats() {
        return videoStats;
    }
}
