package spodlivoi.roll;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import spodlivoi.database.entity.*;
import spodlivoi.service.TelegramService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public abstract class RollerBase<T extends RollerModel> implements RollerInteractor {

    private final JpaRepository<T, Long> repository;

    private final TelegramService telegramService;


    @Value("${debug}")
    private boolean debug;


    @Override
    public void roll(Message message, Users user) throws TelegramApiException {
        if (user.getSettings() != null && !user.getSettings().isRollAnus())
            return;
        var first = false;
        var size = 0;
        var rollModel = getCurrentRollerModel(user);
        if (rollModel == null) {
            rollModel = getCurrentRollerModel();
            rollModel.setUser(user);
            first = true;
        } else {
            var current = LocalDateTime.now();
            var last = rollModel.getLastMeasurement();
            if (current.getDayOfMonth() == last.getDayOfMonth() &&
                    current.getMonthValue() == last.getMonthValue() && !debug) {
                sendMessage(message, getWaitText() + "\nВозвращайся через " +
                        (23 - current.getHour()) + "ч " + (59 - current.getMinute()) + "м", telegramService);
                return;
            } else
                size = rollModel.getSize();
        }
        int upSize = getPlusSize();
        size += upSize;
        if (size < 0 || upSize == 0)
            size = 0;
        sendMessage(message, getRollMessage(first, size, upSize), telegramService);
        rollModel.setSize(size);
        rollModel.setLastMeasurement(LocalDateTime.now());
        repository.save((T) rollModel);
    }

    @Override
    public String getTop(List<Users> users) {
        var message = new StringBuilder();
        var number = 1;
        users.removeIf(u -> u.getAnus() == null);
        users.sort((d1, d2) -> Integer.compare(d2.getAnus().getSize(), d1.getAnus().getSize()));
        for (Users user : users) {
            if (user.getSettings() != null && !user.getSettings().isRollAnus())
                continue;
            message.append(number).append(". ");
            if (user.getFirstName() == null)
                message.append(user.getUserName());
            else {
                message.append(user.getFirstName());
                if (user.getLastName() != null)
                    message.append(" ").append(user.getLastName());
            }
            message.append(" - ").append(user.getAnus().getSize()).append("см;\n");
            number++;
        }
        if (message.toString().equals(""))
            message = new StringBuilder("Пока ещё никто не измерил " + getName() + "!");
        else
            message.insert(0, "Топ " + getNames() + ":\n\n");
        return message.toString();
    }

    @Override
    public void sendMessage(Message message, String text, TelegramService telegramService) throws TelegramApiException {
        RollerInteractor.super.sendMessage(message, text, telegramService);
    }

    abstract String getRollMessage(boolean first, int size, int upSize);

    abstract int getPlusSize();

    abstract String getName();

    abstract String getNames();

    abstract String getWaitText();

    private RollerModel getCurrentRollerModel(Users user) {
        if (this instanceof AnusRoller)
            return user.getAnus();
        if (this instanceof DickRoller)
            return user.getDick();
        if (this instanceof VaginaRoller)
            return user.getVagina();
        throw new IllegalArgumentException();
    }

    private RollerModel getCurrentRollerModel() {
        if (this instanceof AnusRoller)
            return new Anus();
        if (this instanceof DickRoller)
            return new Dicks();
        if (this instanceof VaginaRoller)
            return new Vagina();
        throw new IllegalArgumentException();
    }

}
