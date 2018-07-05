package DroidEye.Util;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

import DroidEye.Bean.HttpRequest;
import DroidEye.Bean.HttpResponse;
import DroidEye.Servlet.HttpServlet;

public class RequestHandle {
    public static void socketHandleService(Socket socket) throws Exception{
        PrintStream printStream = new PrintStream(socket.getOutputStream());

        //获取资源名
        Object resource = ServerUtil.getResource(socket);

        // 通过反射获取request和response对象
        Class<HttpRequest> requestClass = (Class<HttpRequest>) Class.forName("DroidEye.Bean.HttpRequest");
        HttpRequest httpRequest = requestClass.newInstance();
        Method setPrintStream = requestClass.getDeclaredMethod("setPrintStream", PrintStream.class);
        setPrintStream.invoke(httpRequest, printStream);

        Class<HttpResponse> responseClass = (Class<HttpResponse>) Class.forName("DroidEye.Bean.HttpResponse");
        HttpResponse httpResponse = responseClass.newInstance();
        Method setResourceName = responseClass.getDeclaredMethod("setResourceName", String.class);

        //返回的是字符串类,请求静态资源名称
        if (resource.getClass() == String.class) {
            String msg = (String) resource;
            if (msg.equals("")) {
                ResponseHandle.printIndexService(httpRequest, httpResponse);
            } else if (ServerUtil.fileExists(msg)) {
                setResourceName.invoke(httpResponse, msg);
                ResponseHandle.printResponseService(httpRequest, httpResponse);
            } else {
                ResponseHandle.printErrorService(httpRequest, httpResponse);
            }
            //否则返回的List集合,其中保存动态请求资源,集合最后一个元素为请求处理的Servlet
        } else {
            //List<String> values = (List) resource;
            //System.out.println(values);
            //
            //boolean isRight = false;
            //for (String t :
            //        values) {
            //    String[] entry = t.split("=");
            //    if (ServerUtil.getProperty(entry[0]).equals(entry[1])) {
            //        isRight = true;
            //    } else {
            //        isRight = false;
            //    }
            //}
            //
            //
            //if (isRight) {
            //    ResponseHandle.printLoginService(httpRequest, httpResponse);
            //} else {
            //    ResponseHandle.printLoginErrorService(httpRequest, httpResponse);
            //}
            List<String> values = (List) resource;
            //获取请求跳转的Servlet
            String servletName = values.get(values.size() - 1);
            //获取请求Servlet的字节码对象
            Class<?> servletClass = Class.forName("DroidEye.Servlet." + servletName);
            //通过反射创建一个servlet对象
            HttpServlet servlet = (HttpServlet) servletClass.newInstance();
            //获得servlet对象的service的方法对象
            Method service = servletClass.getDeclaredMethod("service",
                    HttpRequest.class, HttpResponse.class);


            //获取Request设定参数的方法对象
            Method setParameter = requestClass.getMethod("setParameter", String.class);
            for (int i = 0; i < values.size() - 1; i++) {
                //将传入的参数写入Request
                setParameter.invoke(httpRequest, values.get(i));
            }

            //调用Servlet的Service执行业务逻辑
            service.invoke(servlet, httpRequest, httpResponse);
        }
    }
}
