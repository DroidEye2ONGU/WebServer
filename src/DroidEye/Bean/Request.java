package DroidEye.Bean;

import java.io.PrintStream;
import java.net.Socket;

public class Request {
    private PrintStream printStream;
    private Socket socket;

    public PrintStream getPrintStream() {
        return printStream;
    }

    public void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
