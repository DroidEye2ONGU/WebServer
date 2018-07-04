package DroidEye.Server;

import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class RequestHandle {
    public static void socketHandle(Socket socket) throws Exception{
        PrintStream printStream = new PrintStream(socket.getOutputStream());

        //获取资源名
        Object resource =  ServerHandle.getResource(socket);

        //返回的是字符串类,请求静态资源
        if (resource.getClass() == String.class) {
            String msg = (String) resource;
            if (msg.equals("")) {
                ResponseHandle.printIndex(printStream);
            } else if (ServerHandle.fileExists(msg)) {
                ResponseHandle.printResponse(printStream, msg);
            } else {
                ResponseHandle.printError(printStream);
            }
        } else {//否则返回的List集合,其中保存在动态请求资源
            List<String> values = (List) resource;
            System.out.println(values);

            boolean isRight = false;
            for (String t :
                    values) {
                String[] entry = t.split("=");
                if (ServerHandle.getProperty(entry[0]).equals(entry[1])) {
                    isRight = true;
                } else {
                    isRight = false;
                }
            }

            if (isRight) {
                ResponseHandle.printLogin(printStream);
            } else {
                ResponseHandle.printLoginError(printStream);
            }
        }
    }
}
