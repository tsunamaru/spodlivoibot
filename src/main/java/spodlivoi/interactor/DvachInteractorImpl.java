package spodlivoi.interactor;

import io.github.furstenheim.CopyDown;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import spodlivoi.utils.Randomizer;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
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

    private int videoStats = 0;

    @Value("${dvach.url}")
    private String dvachUrl;

    @Override
    public SendPhoto getThread(String chatId) throws IOException {
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
        sendPhoto.setChatId(chatId);
        sendPhoto.setParseMode(ParseMode.MARKDOWN);
        sendPhoto.setCaption(post);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(photo);
        sendPhoto.setPhoto(inputFile);
        return sendPhoto;
    }

    @Override
    public SendVideo getVideo(String chatId) throws IOException, EncoderException {
        videoStats++;
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
                FileUtils.copyURLToFile(
                        new URL(video),
                        new File(source));
                String target = "/tmp/" + filename + "(target).mp4";
                File sourceVideo = new File(source);
                File targetVideo = new File(target);

                VideoAttributes videoAttributes = new VideoAttributes();
                videoAttributes.setCodec("libx264");
                videoAttributes.setX264Profile(X264_PROFILE.BASELINE);
                videoAttributes.setQuality(20);

                AudioAttributes audioAttributes = new AudioAttributes();
                audioAttributes.setCodec("aac");
                audioAttributes.setQuality(20);

                //Encoding attributes
                EncodingAttributes attrs = new EncodingAttributes();
                attrs.setInputFormat(format);
                attrs.setOutputFormat("mp4");
                attrs.setVideoAttributes(videoAttributes);

                //Encode
                Encoder encoder = new Encoder();
                encoder.encode(new MultimediaObject(sourceVideo), targetVideo, attrs);

                if(targetVideo.exists()) {

                    SendVideo sendVideo = new SendVideo();
                    sendVideo.setChatId(chatId);
                    InputFile inputFile = new InputFile();
                    inputFile.setMedia(targetVideo);
                    sendVideo.setVideo(inputFile);
                    try {
                        sourceVideo.delete();
                    } catch (Exception ignored) {
                    }
                    try {
                        targetVideo.delete();
                    } catch (Exception ignored) {
                    }

                    return sendVideo;
                }
            }
            return null;
        } finally {
            videoStats--;
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

        URL url = new URL("https://2ch.hk/makaba/mobile.fcgi?task=get_thread&board=b&thread="
                + threadNumber + "&post=0");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(connection.getInputStream());
        JSONArray videoPosts = new JSONArray(StreamUtils.copyToString(in, StandardCharsets.UTF_8));
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
