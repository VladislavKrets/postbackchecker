package online.omnia.checker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.PersistenceException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lollipop on 23.08.2017.
 */
public class MySQLDaoImpl {
    private static Configuration configuration;
    private static SessionFactory sessionFactory;
    private static MySQLDaoImpl instance;

    static {
        configuration = new Configuration()
                .addAnnotatedClass(PostBackEntity.class)
                .configure("/hibernate.cfg.xml");
        Map<String, String> properties = FileWorkingUtils.iniFileReader();
        configuration.setProperty("hibernate.connection.password", properties.get("password"));
        configuration.setProperty("hibernate.connection.username", properties.get("username"));
        configuration.setProperty("hibernate.connection.url", properties.get("url"));
        while (true) {
            try {
                sessionFactory = configuration.buildSessionFactory();
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public boolean isPostbackInDB(String url, String date, String time) {
        Session session = sessionFactory.openSession();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

        Date currentDate = null;
        try {
            currentDate = dateFormat.parse(date + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<PostBackEntity> postBackEntities = session.createQuery("from PostBackEntity where fullurl=:url and date=:date", PostBackEntity.class)
                .setParameter("url", url).setParameter("date", new java.sql.Date(currentDate.getTime())).getResultList();
        return !postBackEntities.isEmpty();
    }
    public boolean isPostbackInDB(String urlPath) {
        Session session = sessionFactory.openSession();


        List<PostBackEntity> postBackEntities = session.createQuery("from PostBackEntity where clickid=:url", PostBackEntity.class)
                .setParameter("url", urlPath).getResultList();
        return !postBackEntities.isEmpty();
    }
    public static MySQLDaoImpl getInstance() {
        if (instance == null) instance = new MySQLDaoImpl();
        return instance;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
