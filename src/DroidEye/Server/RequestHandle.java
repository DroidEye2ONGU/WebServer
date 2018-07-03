package DroidEye.Server;


import java.io.PrintStream;
import java.net.Socket;

public class RequestHandle {

    public static void socketHandle(Socket socket) throws Exception{
        PrintStream printStream = new PrintStream(socket.getOutputStream());

        String msg = ServerHandle.getResourceName(socket);


        if (msg.equals("")) {
            ResponseHandle.printIndex(printStream);
        }else if (ServerHandle.fileExists(msg)) {
            ResponseHandle.printResponse(printStream, msg);
        } else {
            ResponseHandle.printError(printStream);
        }

    }
}
