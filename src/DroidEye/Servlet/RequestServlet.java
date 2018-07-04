package DroidEye.Servlet;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

import DroidEye.Bean.Request;
import DroidEye.Bean.Response;
import DroidEye.Util.ServerUtil;

public class RequestServlet {
    public static void socketHandleService(Socket socket) throws Exception{
        PrintStream printStream = new PrintStream(socket.getOutputStream());

        //获取资源名
        Object resource = ServerUtil.getResource(socket);

        // 通过反射获取request和response对象
        Class<Request> requestClass = (Class<Request>) Class.forName("DroidEye.Bean.Request");
        Request request = requestClass.newInstance();
        Method setPrintStream = requestClass.getDeclaredMethod("setPrintStream", PrintStream.class);
        setPrintStream.invoke(request, printStream);

        Class<Response> responseClass = (Class<Response>) Class.forName("DroidEye.Bean.Response");
        Response response = responseClass.newInstance();
        Method setResourceName = responseClass.getDeclaredMethod("setResourceName", String.class);

        //返回的是字符串类,请求静态资源名称
        if (resource.getClass() == String.class) {
            String msg = (String) resource;
            if (msg.equals("")) {
                ResponseServlet.printIndexService(request,response);
            } else if (ServerUtil.fileExists(msg)) {
                setResourceName.invoke(response, msg);
                ResponseServlet.printResponseService(request,response);
            } else {
                ResponseServlet.printErrorService(request,response);
            }
        } else {//否则返回的List集合,其中保存动态请求资源
            List<String> values = (List) resource;
            System.out.println(values);

            boolean isRight = false;
            for (String t :
                    values) {
                String[] entry = t.split("=");
                if (ServerUtil.getProperty(entry[0]).equals(entry[1])) {
                    isRight = true;
                } else {
                    isRight = false;
                }
            }


            if (isRight) {
                ResponseServlet.printLoginService(request,response);
            } else {
                ResponseServlet.printLoginErrorService(request,response);
            }
        }
    }
}
