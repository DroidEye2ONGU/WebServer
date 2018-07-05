package DroidEye.Servlet;

import DroidEye.Bean.HttpRequest;
import DroidEye.Bean.HttpResponse;

public interface Servlet {
    public void service(HttpRequest httpRequest, HttpResponse httpResponse);
}
