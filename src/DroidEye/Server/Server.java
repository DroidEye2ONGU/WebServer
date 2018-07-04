package DroidEye.Server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public void getConnect() throws Exception {
        //final ServerSocket serverSocket = new ServerSocket(23333);
        final ServerSocket serverSocket = new ServerSocket(
                Integer.parseInt(ServerHandle.getProperty("port")));
        while (true) {
            Socket socket = serverSocket.accept();

            new Thread(()->{
                System.out.println("一个新的连接");
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
