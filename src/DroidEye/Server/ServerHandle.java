package DroidEye.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

        return getProperty("resourcePath");
    }

    public static Object getResource(Socket socket) throws Exception {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        String msg = br.readLine();
        System.out.println(msg);
        if (!isDynamicRequest(msg)) {
            return getStaticResourceNameInGet(msg);
        } else {
            return getDynamicResourceNameInGet(msg);
        }
}

    public static String getStaticResourceNameInGet(String msg) {

        int first = msg.indexOf("/");
        int last = msg.indexOf(" ", first);

        msg = msg.substring(first + 1, last);
        return msg;
    }

    public static boolean fileExists(String resourceName) {
        return new File(RESOURCE_PATH + resourceName).exists();
    }

    //判断是否是动态资源请求(Get方式)
    public static boolean isDynamicRequest(String msg) {
        return msg.indexOf("?") != -1;
    }

    //获取Get方式请求中的资源,将请求的键值对保存在list集合中返回
    public static List<String> getDynamicResourceNameInGet(String msg) {
        //GET /login.html?user=123&password=abc HTTP/1.1


        int requestPosition = msg.indexOf("?");
        int endPosition = msg.indexOf(" ", requestPosition + 1);
        String requestContent = msg.substring(requestPosition + 1,endPosition);

        String[] values = requestContent.split("&");

        System.out.println(Arrays.asList(values));

        return Arrays.asList(values);
    }

}
