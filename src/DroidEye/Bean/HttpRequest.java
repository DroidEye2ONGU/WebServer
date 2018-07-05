package DroidEye.Bean;

import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;

public class HttpRequest {
    private PrintStream printStream;
    private Socket socket;

    private HashMap parameters = new HashMap();

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

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public void setParameter(String entry) {
        String[] entrys = entry.split("=");
        parameters.put(entrys[0], entrys[1]);
    }
}
