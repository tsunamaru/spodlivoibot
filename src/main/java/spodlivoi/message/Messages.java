package spodlivoi.message;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import spodlivoi.database.entity.UserSettings;
import spodlivoi.database.enums.Gender;
import spodlivoi.utils.Randomizer;

import java.util.ArrayList;

@PropertySource(value = "classpath:/messages.yaml", factory = YamlPropertySourceFactory.class)
@Component
public class Messages {

    @Value("${user.sticker-packs}")
    @Getter
    private ArrayList<String> fightPacks;

    @Value("${user.random-usernames}")
    @Getter
    private ArrayList<String> randomUserNames;

    @Value("${user.anus-items}")
    @Getter
    private ArrayList<String> anusItems;

    @Value("${admin.ddos.deactivated-command}")
    @Getter
    private String ddosdeactivatedCommand;

    @Value("${admin.ddos.activated-command}")
    @Getter
    private String ddosActivatedCommand;

    @Value("${admin.ddos.select-copypastes}")
    @Getter
    private String ddosSelectCopypastes;

    @Value("${admin.ddos.button-prefix}")
    @Getter
    private String ddosButtonPrefix;

    @Value("${admin.ddos.already-active}")
    @Getter
    private String ddosAlreadyActiveMessage;

    @Value("${admin.ddos.already-diactive}")
    @Getter
    private String ddosAlreadyDiactiveMessage;

    @Value("${random-message.text}")
    private String randomMessageText;
    @Value("${random-message.text-deleted}")
    private String randomMessageTextDeleted;
    @Value("${settings.name}")
    private String settingName;
    @Value("${settings.button-prefix}")
    @Getter
    private String settingsButtonPrefix;
    @Value("${settings.cancel}")
    @Getter
    private String cancel;
    @Value("${settings.gender}")
    private String genderText;
    @Value("${settings.dick-size}")
    private String dickSizeText;
    @Value("${settings.anus-size}")
    private String anusSizeText;
    @Value("${settings.vagina-size}")
    private String vaginaSizeText;
    @Value("${settings.da}")
    @Getter
    private String yes;
    @Value("${settings.net}")
    @Getter
    private String no;
    @Value("${settings.amputate-dick}")
    @Getter
    private String amputateDick;
    @Value("${settings.sew-anus}")
    @Getter
    private String sewAnus;
    @Value("${settings.sew-vagina}")
    @Getter
    private String sewVagina;
    @Value("${settings.change-gender}")
    @Getter
    private String changeGender;
    @Value("${settings.vagina-roll}")
    private String vaginaRollSetting;
    @Value("${settings.dick-roll}")
    private String dickRollSetting;
    @Value("${settings.anus-roll}")
    private String anusRollSetting;
    @Value("${admin.ddos.activated}")
    private String ddosActivatedMessage;
    @Value("${admin.ddos.deactivated}")
    private String ddosdeactivatedMessage;
    @Value("${copypaste.commands}")
    @Getter
    private ArrayList<String> copypasteCommands;

    public String getRandomMessageText(String username) {
        return String.format(randomMessageText, username);
    }

    public String getRandomMessageTextDeleted(String username) {
        return String.format(randomMessageTextDeleted, username);
    }

    public String getSettingName(String username, UserSettings userSettings) {
        StringBuilder info = new StringBuilder();
        Gender gender = userSettings.getGender();
        if (gender == null) {
            gender = Gender.MALE;
        }
        info.append(String.format(this.genderText, gender.getName())).append("\n");
        if (gender != Gender.FEMALE) {
            info.append(String.format(dickSizeText,
                            userSettings.getUser().getDick() == null ? 0 : userSettings.getUser().getDick().getSize()))
                    .append("\n");
        }
        if (gender == Gender.FEMALE || gender == Gender.FIGHT_HELICOPTER) {
            info.append(String.format(vaginaSizeText, userSettings.getUser().getVagina() == null ? 0 :
                            userSettings.getUser().getVagina().getSize())).append("\n");
        }
        info.append(String.format(anusSizeText,
                        userSettings.getUser().getAnus() == null ? 0 : userSettings.getUser().getAnus().getSize()))
                .append("\n");
        return String.format(settingName, username, info);
    }

    public String getVaginaRollSetting(boolean active) {
        return String.format(vaginaRollSetting, active ? yes : no);
    }

    public String getDickRollSetting(boolean active) {
        return String.format(dickRollSetting, active ? yes : no);
    }

    public String getAnusRollSetting(boolean active) {
        return String.format(anusRollSetting, active ? yes : no);
    }

    public String getDdosActivatedMessage(String username) {
        return String.format(ddosActivatedMessage, username);
    }

    public String getDdosdeactivatedMessage(String username) {
        return String.format(ddosdeactivatedMessage, username);
    }

    public String getRandomAnusItem() {
        return Randomizer.getRandomValueFromList(anusItems);
    }

}
