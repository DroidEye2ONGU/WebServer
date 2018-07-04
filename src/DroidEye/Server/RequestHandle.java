package DroidEye.Server;


import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

public class RequestHandle {

    public static void socketHandle(Socket socket) throws Exception{
        PrintStream printStream = new PrintStream(socket.getOutputStream());

        //String msg = ServerHandle.getResource(socket);
        Object resource =  ServerHandle.getResource(socket);

        if (resource.getClass() == String.class) {
            String msg = (String) resource;
            if (msg.equals("")) {
                ResponseHandle.printIndex(printStream);
            } else if (ServerHandle.fileExists(msg)) {
                ResponseHandle.printResponse(printStream, msg);
            } else {
                ResponseHandle.printError(printStream);
            }
        } else {
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
