package DroidEye.Server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void getConnect() throws Exception {
        final ServerSocket serverSocket = new ServerSocket(23333);
        while (true) {
            Socket socket = serverSocket.accept();

            new Thread(()->{
                try {
                    RequestHandle.socketHandle(socket);
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();

        }
    }
}
