package DroidEye.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Properties;

public class ServerHandle {
    public static final String RESOURCE_PATH = "C:\\Users\\DroidEye\\Desktop\\Programme\\IdeaProjects\\WebServer\\src\\Resources\\";

    private static final String PROPERTY_NAME = "server.properties";

    public static String getProperty(String key) {
        String value = null;
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(RESOURCE_PATH + PROPERTY_NAME))
        )) {
            Properties properties = new Properties();
            properties.load(bufferedReader);
            value = properties.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private static String getResourcePath() {
        //String path = "";
        //try (
        //        BufferedReader br = new BufferedReader(
        //                new InputStreamReader(
        //                        new FileInputStream("C:\\Users\\DroidEye\\Desktop\\Programme\\IdeaProjects\\WebServer\\src\\Resources\\ResourcePath.properties")
        //                ))
        //) {
        //    path = br.readLine();
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        //return path;
        return getProperty("resourcePath");
    }

    public static String getResourceName(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        String msg = br.readLine();

        System.out.println(msg);

        int first = msg.indexOf("/");
        int last = msg.indexOf(" ", first);

        msg = msg.substring(first + 1, last);
        return msg;
    }

    public static boolean fileExists(String resourceName) {
        return new File(RESOURCE_PATH + resourceName).exists();
    }

}
