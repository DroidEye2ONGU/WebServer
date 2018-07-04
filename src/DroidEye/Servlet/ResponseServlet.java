package DroidEye.Servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;

import DroidEye.Bean.Request;
import DroidEye.Bean.Response;
import DroidEye.Util.ServerUtil;

public class ResponseServlet {

    //返回首页
    public static void printIndexService(Request request, Response response) {
        printOKStatus(request.getPrintStream());
        printContent(request.getPrintStream(), ServerUtil.getProperty("indexPage"));
    }

    //返回请求资源信息
    public static void printResponseService(Request request, Response response) {
        printOKStatus(request.getPrintStream());
        printContent(request.getPrintStream(), response.getResourceName());
    }

    //返回资源请求错误页面
    public static void printErrorService(Request request, Response response) {
        printErrorStatus(request.getPrintStream());
        printContent(request.getPrintStream(), ServerUtil.getProperty("errorPage"));
    }

    //返回登陆成功页面
    public static void printLoginService(Request request, Response response) {
        printOKStatus(request.getPrintStream());
        printContent(request.getPrintStream(), ServerUtil.getProperty("loginPage"));
    }

    //返回登陆失败页面
    public static void printLoginErrorService(Request request, Response response) {
        printErrorStatus(request.getPrintStream());
        printContent(request.getPrintStream(), ServerUtil.getProperty("loginErrorPage"));
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
        File responseFile = new File(ServerUtil.RESOURCE_PATH + resourceName);

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
