package bot;

import database.DatabaseManager;
import database.models.Users;
import org.hibernate.Session;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        
        Logger logger = Logger.getLogger(Main.class.getName()); 
        logger.log(Level.INFO, System.getProperty("token"));
        DatabaseManager.init();

        // System.getProperties().put("proxySet", "true");
        // System.getProperties().put("socksProxyHost", "127.0.0.1");
        // System.getProperties().put("socksProxyPort", "9050");

        try(Session session = DatabaseManager.getSession()){
            ArrayList<Users> users = new ArrayList<>(session.createQuery("from Users").list());
        }catch (Exception e){
            DatabaseManager.createTables();
        }

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

}
