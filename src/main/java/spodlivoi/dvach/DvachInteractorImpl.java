package spodlivoi.dvach;

import io.github.furstenheim.CopyDown;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.service.TelegramService;
import spodlivoi.utils.Randomizer;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.encode.enums.X264_PROFILE;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class DvachInteractorImpl implements DvachInteractor {

    private final TelegramService telegramService;

    private int videoStats = 0;

    @Value("${dvach.url}")
    private String dvachUrl;

    @Override
    @Async
    public void sendThreadAsync(Message message) throws IOException, TelegramApiException {
        String post, photo = "";
        JSONObject postJson = null;
        String link = null;
        while (!(photo.contains(".jpg") || photo.contains(".png") || photo.contains(".jpeg"))) {
            JSONArray answerArray = getBThreads();
            int r = Randomizer.getRandomNumberInRange(0, answerArray.length() - 1);
            postJson = answerArray.getJSONObject(r);
            link = "https://2ch.hk/b/res/" + answerArray.getJSONObject(r).getString("num")
                    + ".html";
            JSONArray files = postJson.getJSONArray("files");
            if (!files.isEmpty()) {
                photo = "https://2ch.hk/" + files.getJSONObject(0).getString("path");
            }
        }
        post = postJson.getString("comment");
        CopyDown converter = new CopyDown();
        post = converter.convert(post);
        post = post.replaceAll("\\*\\*", "*");
        post = post.replaceAll("__", "_");
        post = post.replaceAll("\\\\([$&+,:;=?@#|'<>.\\-^*()%!])", "$1");
        if (post.length() > 900) {
            post = post.substring(0, 900) + "...";
        }
        post += "\n\nПерейти в тред: " + link;
        post = "_" + postJson.getString("date") + "_\n\n" + post;


        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(message.getChatId()));
        sendPhoto.setReplyToMessageId(message.getMessageId());
        sendPhoto.setParseMode(ParseMode.MARKDOWN);
        sendPhoto.setCaption(post);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(photo);
        sendPhoto.setPhoto(inputFile);
       telegramService.execute( sendPhoto);
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
                if (video.contains(".mp4"))
                    format = "mp4";
                else
                    format = "webm";
                source = "/tmp/" + filename + "." + format;
                File sourceVideo = new File(source);
                FileUtils.copyURLToFile(new URL(video), sourceVideo);
                String target = "/tmp/" + filename + "(target).mp4";

                File targetVideo = new File(target);
                convertAndSendVideoAsync(message, sourceVideo, targetVideo, format);
            }else{
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(String.valueOf(message.getChatId()))
                        .replyToMessageId(message.getMessageId())
                        .text("Абу спиздил все шебм с двачей...")
                        .build();
               telegramService.execute( sendMessage);
            }
        }catch (IOException | TelegramApiException e){
            sendVideoAsync(message);
        } finally {
            if(videoStats > 0)
                videoStats--;
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
            videoAttributes.setX264Profile(X264_PROFILE.BASELINE);
            videoAttributes.setQuality(15);

            AudioAttributes audioAttributes = new AudioAttributes();
            audioAttributes.setCodec("aac");
            audioAttributes.setQuality(15);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setInputFormat(format);
            attrs.setOutputFormat("mp4");
            attrs.setVideoAttributes(videoAttributes);
            attrs.setAudioAttributes(audioAttributes);

            //Encode
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(sourceVideo), targetVideo, attrs);

            if (targetVideo.exists()) {

                SendVideo sendVideo = new SendVideo();
                sendVideo.setChatId(String.valueOf(message.getChatId()));
                sendVideo.setReplyToMessageId(message.getMessageId());
                InputFile inputFile = new InputFile();
                inputFile.setMedia(targetVideo);
                sendVideo.setVideo(inputFile);

               telegramService.execute( sendVideo);
                if (sourceVideo.exists())
                    sourceVideo.delete();
                if (targetVideo.exists())
                    targetVideo.delete();
                deleteVideo(sendVideo);
            }
        }catch (Exception e) {
            log.error("Ошибка при обработке видео", e);
        }finally {
            if (videoStats > 0)
                videoStats--;
        }
    }

    private void deleteVideo(SendVideo sendVideo){
        File file = sendVideo.getVideo().getNewMediaFile();
        try {
            file.delete();
        } catch (Exception ignored) {
        }
    }

    private JSONArray getBThreads() throws IOException {
        URL url = new URL(dvachUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(connection.getInputStream());
        JSONObject answer = new JSONObject(StreamUtils.copyToString(in, StandardCharsets.UTF_8));
        JSONArray answerArray = answer.getJSONArray("threads");
        in.close();
        return answerArray;
    }



    private String getWebmUrl() throws IOException {
        String video = "";
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
        if(threadNumber.isEmpty() || threadNumber.isBlank()){
            return null;
        }

        URL url = new URL("https://2ch.hk/makaba/mobile.fcgi?task=get_thread&board=b&thread="
                + threadNumber + "&post=0");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(connection.getInputStream());
        String json = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        JSONArray videoPosts = new JSONArray(json);
        in.close();

        while (video.equals("")) {
            int r = Randomizer.getRandomNumberInRange(0, videoPosts.length() - 1);
            JSONObject postJson = videoPosts.getJSONObject(r);
            JSONArray files = postJson.getJSONArray("files");
            if (!files.isEmpty())
                video = "https://2ch.hk" + files.getJSONObject(0).getString("path");
            if (!video.contains(".webm") && !video.contains(".mp4"))
                video = "";

        }

        return video;
    }

    @Override
    public int getVideoStats() {
        return videoStats;
    }
}
