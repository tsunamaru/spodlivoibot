package spodlivoi.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import spodlivoi.entity.Users;

import java.util.List;

public interface Roller {

    void roll(Message message, Users user);

    String getTop(List<Users> users);

}
