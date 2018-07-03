package DroidEye.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;

public class ResponseHandle {

    public static void printIndex(PrintStream printStream) {
        printOKStatus(printStream);
        printContent(printStream,ServerHandle.getProperty("indexPage"));
    }

    public static void printResponse(PrintStream printStream, String resourceName) {
        printOKStatus(printStream);
        printContent(printStream, resourceName);
    }

    public static void printError(PrintStream printStream) {
        printErrorStatus(printStream);
        printContent(printStream,ServerHandle.getProperty("errorPage"));
    }

    private static void printOKStatus(PrintStream printStream) {
        printStream.println("HTTP/1.1 200 OK");
        printStream.println();
    }

    private static void printErrorStatus(PrintStream printStream) {
        printStream.println("HTTP/1.1 404 NOTFOUND");
        printStream.println();
    }

    private static void printContent(PrintStream printStream,String resourceName) {
        File responseFile = new File(ServerHandle.RESOURCE_PATH + resourceName);

        try (
                FileInputStream fileInputStream = new FileInputStream(responseFile)
        ) {
            byte[] buff = new byte[1024];
            int hasRead;

            while ((hasRead = fileInputStream.read(buff)) > 0) {
                printStream.write(buff, 0, hasRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
