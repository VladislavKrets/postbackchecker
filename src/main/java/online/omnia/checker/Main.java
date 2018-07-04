package online.omnia.checker;

import java.io.*;
import java.util.Map;

/**
 * Created by lollipop on 23.08.2017.
 */
public class Main{

    public static void main(String[] args) throws IOException {
        if (args.length == 0) return;

        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        File file = new File("missed_postbacks.log");
        if (!file.exists()) file.createNewFile();
        FileWriter writer = new FileWriter(file, true);
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        String logLine;
        String[] splittedLine;
        String[] clickidParameters;
        PostbackHandler postbackHandler = new PostbackHandler();
        Map<String, String> parameters;
        while ((logLine = reader.readLine()) != null) {
            try {
                //splittedLine = logLine.split(" ");
                /*if (!logLine.isEmpty() && !logLine.contains(" answer=") && splittedLine.length >= 3) {
                    if (!mySQLDao.isPostbackInDB(splittedLine[2], splittedLine[0], splittedLine[1])) {
                        writer.write(logLine + "\n");
                        writer.flush();
                    }
                }*/
                if (logLine.startsWith("GET ") && logLine.endsWith(" HTTP/1.1") && !logLine.contains("&clickid=11111111")) {
                    splittedLine = logLine.split(" ");
                        if (splittedLine.length >= 3) {
                            parameters = postbackHandler.getPostbackParameters(splittedLine[1]);
                            if (parameters.containsKey("clickid")) {
                                clickidParameters = parameters.get("clickid").split("_");
                                if (clickidParameters.length == 2 && !MySQLDaoImpl.getInstance().isPostbackInDB(clickidParameters[1])) {
                                    writer.write(splittedLine[1] + "\n");
                                    writer.flush();
                                }
                                else if (clickidParameters.length == 1 && !MySQLDaoImpl.getInstance().isPostbackInDB(clickidParameters[0])) {
                                    writer.write(splittedLine[1] + "\n");
                                    writer.flush();
                                }
                            }
                        }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.close();
        reader.close();
        MySQLDaoImpl.getSessionFactory().close();
    }

}
