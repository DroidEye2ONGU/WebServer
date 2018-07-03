package DroidEye.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerHandle {
    public static final String RESOURCEPATH = getResourcePath();

    private static String getResourcePath() {
        String path = "";
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream("C:\\Users\\DroidEye\\Desktop\\Programme\\IdeaProjects\\WebServer\\src\\Resources\\ResourcePath.properties")
                        ))
        ) {
            path = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String getResourceName(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

    /*    String t;
        while ((t = br.readLine()) != null) {
            if (t.trim().equals("")) {
                System.out.println("-------------------------");
            } else {
                System.out.println(t);
            }
        }*/

        String msg = br.readLine();

        System.out.println(msg);

        int first = msg.indexOf("/");
        int last = msg.indexOf(" ", first);

        msg = msg.substring(first + 1, last);
        return msg;
    }

    public static boolean fileExists(String resourceName) {
        return new File(RESOURCEPATH + resourceName).exists();
    }

}
