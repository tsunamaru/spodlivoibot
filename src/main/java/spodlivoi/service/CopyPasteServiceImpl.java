package spodlivoi.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import spodlivoi.database.enums.Copypaste;
import spodlivoi.utils.Randomizer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class CopyPasteServiceImpl implements CopyPasteService {

    private JSONArray baby;
    private JSONArray dota;
    private JSONArray kolchan;
    private JSONArray olds;
    private JSONArray shizik;
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
    }

    public String getRandomCopyPaste(Copypaste type) {
        JSONArray jsonArray = getJSONCopyPaste(type);
        if (jsonArray != null) {
            int rand = Randomizer.getRandomNumberInRange(0, jsonArray.length() - 1);
            return jsonArray.getString(rand);
        } else {
            return "Копипасту спиздили армяне!";
        }
    }

    @Override
    public JSONArray getJSONCopyPaste(Copypaste copypaste) {
        switch (copypaste) {
            case BABY:
                return baby;
            case DOTA:
                return dota;
            case OLDS:
                return olds;
            case SHIZIK:
                return shizik;
            case KOLCHAN:
                return kolchan;
            default:
                return null;
        }
    }

}
