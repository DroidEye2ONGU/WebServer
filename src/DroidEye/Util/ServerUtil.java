package DroidEye.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

        //拿到请求行信息 数组大小为3 分别为请求方式 请求路径 Http协议版本号
        String[] requestLine = getRequestLineInfo(requestInfo[0]);


        //请求方式
        String requestWay = requestLine[0];


        // 判断请求方式是GET还是POST
        if (requestWay.trim().toLowerCase().equals("get")) {
            System.out.println("GET请求");
            //如果不是动态资源
            if (!isDynamicRequestInGet(requestLine[1])) {
                return getStaticResourceNameInGet(requestLine[1]);//返回请求的静态资源路径
            } else {//如果是动态资源

                //拿到请求的Servlet
                String servletPath = getServlet(requestLine[1], true);

                //返回请求的动态资源
                List<String> dynamicResourceNameInGet = new ArrayList<>();
                dynamicResourceNameInGet.addAll(getDynamicResourceNameInGet(requestLine[1]));
                //总是在包含请求资源集合的最后一个元素上写入Servlet路径
                dynamicResourceNameInGet.add(servletPath);
                return dynamicResourceNameInGet;
            }
        } else if (requestWay.trim().toLowerCase().equals("post")) {
            System.out.println("POST请求");
            String requestBody = requestInfo[2];
            System.out.println("信息" + requestBody);

            //拿到请求的Servlet
            String servletPath = getServlet(requestLine[1], false);

            List<String> dynamicResourceNameInPost = new ArrayList<>();
            dynamicResourceNameInPost.addAll(getDynamicResourceNameInPost(requestBody));

            //总是在包含请求资源集合的最后一个元素上写入Servlet路径
            dynamicResourceNameInPost.add(servletPath);
            return dynamicResourceNameInPost;

        } else {
            return null;
        }


    }

    //进一步分解请求行
    private static String[] getRequestLineInfo(String requestLine) {
        String[] requestLineInfo = requestLine.split(" ");
        return requestLineInfo;
    }

    //获得包含有请求行 请求头 (Post时)请求体的字符串数组
    private static String[] getRequestInfo(Socket socket) throws IOException {

        InputStream inputStream = socket.getInputStream();

        byte[] buff = new byte[1024];
        int hasRead;

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
            for (int i = 1; i < split.length; i++) {
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

    @Deprecated //getResource()方法现在可以直接拿到请求方式,该方法弃用
    private static String requestWay(String msg) {
        //旧版本 直接拿到全部请求行解析请求方式
        if (msg != null && msg.length() >= 4) {
            return msg.substring(0, 4);
        } else {
            return "";
        }

    }

    //判断是否是动态资源请求(Get方式)
    private static boolean isDynamicRequestInGet(String msg) {
        return msg.indexOf("?") != -1;//判断Get请求的是否是动态资源(通过判断是否包含?)
    }

    private static String getStaticResourceNameInGet(String msg) {

        //int first = msg.indexOf("/");
        //int last = msg.indexOf(" ", first);


        msg = msg.substring(1);
        return msg;
    }

    //获取Get方式请求中的资源,将请求的键值对保存在list集合中返回
    private static List<String> getDynamicResourceNameInGet(String msg) {
        //GET /loginPost.html?user=123&password=abc HTTP/1.1

        //msg = 127.0.0.1:23333/LoginServlet?user=root&password=admin
        int requestPosition = msg.indexOf("?");

        String requestContent = msg.substring(requestPosition + 1);

        String[] values = requestContent.split("&");

        System.out.println(Arrays.asList(values));

        return Arrays.asList(values);
    }

    //获取POST方式请求中的资源,将请求的键值对保存在list集合中返回
    private static List<String> getDynamicResourceNameInPost(String msg) {
        String[] values = msg.split("&");

        return Arrays.asList(values);
    }

    private static String getServlet(String path, boolean isGet) {
        //    POST /127.0.0.1:23333/Servlet/LoginService.service
        //    GET /127.0.0.1:23333/Servlet/LoginService.service?user=root&password=admin HTTP/1.1
        System.out.println("GetServlet:" + path);
        String[] paths = path.split("/");

        if (isGet) { //Get请求方式 资源名跟在?后
            String t = paths[paths.length - 1];
            int resourcePosition = t.indexOf("?");
            String servlet = t.substring(0, resourcePosition);
            return servlet;
        } else { //POST方式
            return paths[paths.length - 1];
        }

    }
}
