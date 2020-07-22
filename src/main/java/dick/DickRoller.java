package dick;

import database.DatabaseManager;
import database.models.Dicks;
import database.models.Users;
import org.hibernate.Session;
import utils.Logs;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DickRoller {
    public static class Holder{
        private static DickRoller instance = new DickRoller();
        public static DickRoller getInstance() {
            return instance;
        }
    }

    private final static int TIME_CONVERT = 0;
    private static final String SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String roll(Users u, Session session){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SQL_DATE_FORMAT);
        try {
            if( u == null)
                throw new NullPointerException("User не может быть null");
            Users user = session.get(Users.class, u.getId());
            Dicks dick = null;
            boolean first = false;
            int size = 0;
            try {
                dick = new ArrayList<>(user.getDicks()).get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dick == null) {
                dick = new Dicks();
                dick.setUsers(user);
                first = true;
            }else {
                LocalDateTime current = LocalDateTime.now();
                LocalDateTime last = LocalDateTime.parse(dick.getLastMeasurement(), formatter);
                current = current.minusHours(TIME_CONVERT);
                last = last.minusHours(TIME_CONVERT);
               if(current.getDayOfMonth() == last.getDayOfMonth() &&
                  current.getMonthValue() == last.getMonthValue())
                    return "Ты уже измерял свой огрызок сегодня!\nПриходи через " +
                            (23 - current.getHour()) + "ч " + (59 - current.getMinute()) + "м";
                else
                    size = dick.getSize();
            }
            int upSizeN = Randomizer.getPlusDickSize();
            size += upSizeN;
            if (size < 0 || upSizeN == 0)
                size = 0;
            session.beginTransaction();
            dick.setSize(size);
            dick.setLastMeasurement(new Timestamp(System.currentTimeMillis()).toLocalDateTime().format(formatter));
            session.saveOrUpdate(dick);
            session.getTransaction().commit();
            int dickText = Randomizer.getRandomNumberInRange(1, 5);
            String upSize = String.valueOf(upSizeN).
                    replaceAll("-", "");
            if(first){
                if(size == 0)
                    return "На данный момент ты не имеешь писюна, неудачник!";
                else
                    return "Ого! Размер твоего писюна аж " + size +
                            "см!\nПриходи завтра. Посмотрим, изменился ли он";
            } else{
                if(size == 0)
                    return "Мои соболезнования. Сегодня у тебя произошла страшная трагедия: твой писюн отпал.";
                if(upSizeN < 0) {

                    switch (dickText) {
                        case 1:
                            return "Ахахахах, неудачник. Твой огрызок стал меньше на целых " + upSize +
                                    "см.\nТеперь его длина " + size + "см.";
                        case 2:
                            return "Твой и не без того маленький пенис стал меньше на " + upSize +
                                    "см.\nТеперь его длина " + size + "см.";
                        case 3:
                            return "Нуууу, что тут можно ещё сказать... Твоя пипирка уменьшилась на " + upSize +
                                    "см.\nТеперь её длина " + size + "см.";
                        case 4:
                            return "Так-так, что тут у нас? *УМЕНЬШЕНИЕ ПЕНИСА!* на целых " + upSize +
                                    "см.\nТеперь его длина " + size + "см.";
                        case 5:
                            return "Мои спутники зафиксировали уменьшение твоего полового органа на " + upSize +
                                    "см.\nТеперь его длина " + size + "см.\n*АХАХАХАХАХАХАХ.* Ржу с тебя.";
                    }
                }else
                    switch (dickText) {
                        case 1:
                            return "Поздравляю! Твой член сегодня вырос на целых " + upSize + "см. \n" +
                                    "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                        case 2:
                            return "*Ах ты читер!* Каким-то образом ты смог увеличить свой писюн на " + upSize + "см. \n" +
                                    "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                        case 3:
                            return "Твой пенис скоро можно будет фиксировать со спутников. Он вырос на " + upSize + "см. \n" +
                                    "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                        case 4:
                            return "Ох, ну и завидую же я тебе, потому что твои причиндалы стали больше на " + upSize + "см. \n" +
                                    "Теперь их длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                        case 5:
                            return "Все женщины в шоке! Твой гигантский половой орган стал больше на " + upSize + "см. \n" +
                                    "Теперь его длина " + size + "см. Скоро ты станешь настоящим мужчиной!";
                    }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "Произошла какая-то ошибка...";
    }
}
