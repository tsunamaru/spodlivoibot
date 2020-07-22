package database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DatabaseManager {
    private static SessionFactory mainSessionFactory = new Configuration()
            .configure(DatabaseManager.class.getResource("hibernate.cfg.xml"))
            .buildSessionFactory();

    public static Session getSession(){
        return mainSessionFactory.openSession();
    }
    public static SessionFactory getSessionFactory(){
        return  mainSessionFactory;
    }
    public static void close(){
        mainSessionFactory.close();
    }
    public static void init(){
        getSession().close();
    }

    public static void createTables() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:db/spodlivoi.sqlite");
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(getSqlFileToString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getSqlFileToString()
    {
        return new Scanner(DatabaseManager.class.getResourceAsStream("podliva.sql"), "UTF-8").useDelimiter("\\A").next();
    }
}
