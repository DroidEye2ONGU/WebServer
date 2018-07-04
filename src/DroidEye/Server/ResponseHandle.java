package DroidEye.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;

public class ResponseHandle {

    //返回首页
    public static void printIndex(PrintStream printStream) {
        printOKStatus(printStream);
        printContent(printStream, ServerHandle.getProperty("indexPage"));
    }

    //返回请求资源信息
    public static void printResponse(PrintStream printStream, String resourceName) {
        printOKStatus(printStream);
        printContent(printStream, resourceName);
    }

    //返回资源请求错误页面
    public static void printError(PrintStream printStream) {
        printErrorStatus(printStream);
        printContent(printStream, ServerHandle.getProperty("errorPage"));
    }

    //返回登陆成功页面
    public static void printLogin(PrintStream printStream) {
        printOKStatus(printStream);
        printContent(printStream, ServerHandle.getProperty("loginPage"));
    }

    //返回登陆失败页面
    public static void printLoginError(PrintStream printStream) {
        printErrorStatus(printStream);
        printContent(printStream, ServerHandle.getProperty("loginErrorPage"));
    }

    //打印正确状态
    private static void printOKStatus(PrintStream printStream) {
        printStream.println("HTTP/1.1 200 OK");
        printStream.println();
    }

    //打印页面未找到状态
    private static void printErrorStatus(PrintStream printStream) {
        printStream.println("HTTP/1.1 404 NOTFOUND");
        printStream.println();
    }

    //打印资源
    private static void printContent(PrintStream printStream, String resourceName)  {
        File responseFile = new File(ServerHandle.RESOURCE_PATH + resourceName);

        try {

            FileInputStream fileInputStream = new FileInputStream(responseFile);
            byte[] buff = new byte[1024];
            int hasRead;

            while ((hasRead = fileInputStream.read(buff)) > 0) {
                printStream.write(buff, 0, hasRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
