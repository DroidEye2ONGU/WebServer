package DroidEye.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ServerUtil {
    public static final String RESOURCE_PATH = "C:\\Users\\DroidEye\\Desktop\\Programme\\IdeaProjects\\WebServer\\src\\DroidEye\\Resource\\";
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

        String[] requestInfo = getRequestInfo(socket);

        String msg = requestInfo[0];

        String requestWay = requestWay(msg).trim().toLowerCase();

        // 判断请求方式是GET还是POST
        if (requestWay.equals("get")) {
            System.out.println("GET请求");
            if (!isDynamicRequest(msg)) {
                return getStaticResourceNameInGet(msg);
            } else {
                return getDynamicResourceNameInGet(msg);
            }
        } else if (requestWay.equals("post")) {
            System.out.println("POST请求");
            String requestBody = requestInfo[2];
            System.out.println("信息" + requestBody);

            return getDynamicResourceNameInPost(requestBody);

        } else {
            return null;
        }


    }

    //获得包含有请求行 请求头 (Post时)请求体的字符串数组
    private static String[] getRequestInfo(Socket socket) throws IOException {

        InputStream inputStream = socket.getInputStream();

        byte[] buff = new byte[1024];
        int hasRead;
        //hasRead = inputStream.read(buff);
        //String info = new String(buff, 0, hasRead);
        String info = "";
        int count = 1;
        while (count != 0) {
            hasRead = inputStream.read(buff);
            count = inputStream.available();
            info += new String(buff, 0, hasRead);
        }

        String[] split = info.split("\r\n");

        String line = split[0];
        String head = "";
        String body = "";
        if (split[0].trim().toLowerCase().contains("get")) {
            for (int i = 1; i < split.length; i++) {
                head += split[i];
            }
        } else {
            int bodyPosition = 0;
            for (int i = 1; i< split.length ; i++) {
                if (split[i].equals("")) {
                    bodyPosition = i;
                }
                head += split[i];
            }
            for (int i = bodyPosition + 1; i < split.length; i++) {
                body += split[i];
            }
        }
        System.out.println(line);
        System.out.println(head);
        System.out.println(body);
        String[] values = {line, head, body};


        return values;

    }

    public static boolean fileExists(String resourceName) {
        return new File(RESOURCE_PATH + resourceName).exists();
    }

    private static String requestWay(String msg) {
        if (msg != null && msg.length() >= 4) {
            return msg.substring(0, 4);
        } else {
            return "";
        }
    }

    //判断是否是动态资源请求(Get方式)
    private static boolean isDynamicRequest(String msg) {
        return msg.indexOf("?") != -1;
    }

    private static String getStaticResourceNameInGet(String msg) {

        int first = msg.indexOf("/");
        int last = msg.indexOf(" ", first);

        msg = msg.substring(first + 1, last);
        return msg;
    }

    //获取Get方式请求中的资源,将请求的键值对保存在list集合中返回
    private static List<String> getDynamicResourceNameInGet(String msg) {
        //GET /loginPost.html?user=123&password=abc HTTP/1.1


        int requestPosition = msg.indexOf("?");
        int endPosition = msg.indexOf(" ", requestPosition + 1);
        String requestContent = msg.substring(requestPosition + 1, endPosition);

        String[] values = requestContent.split("&");

        System.out.println(Arrays.asList(values));

        return Arrays.asList(values);
    }

    //获取POST方式请求中的资源,将请求的键值对保存在list集合中返回
    private static List<String> getDynamicResourceNameInPost(String msg) {
        String[] values = msg.split("&");

        return Arrays.asList(values);
    }
}
