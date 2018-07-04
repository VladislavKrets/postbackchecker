package online.omnia.checker;


import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 12.07.2017.
 */
public class FileWorkingUtils {
    private static BufferedReader fileReader;

    public static synchronized Map<String, String> iniFileReader() {
        Map<String, String> properties = new HashMap<String, String>();
        try {
            fileReader = new BufferedReader(new FileReader("configuration.ini"));
            String property;
            String[] propertyArray;
            while ((property = fileReader.readLine()) != null) {
                propertyArray = property.split("=");
                properties.put(propertyArray[0], propertyArray[1]);
            }
        } catch (IOException e) {
        }
        return properties;
    }

    private static String buildLine(Date date, Time time, String fullUrl) {
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(date.toString())
                .append(" ")
                .append(time.toString())
                .append(" ")
                .append(fullUrl)
                .append("\n");
        return lineBuilder.toString();
    }
}
