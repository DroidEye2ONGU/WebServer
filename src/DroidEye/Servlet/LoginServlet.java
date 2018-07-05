package DroidEye.Servlet;

import DroidEye.Bean.HttpRequest;
import DroidEye.Bean.HttpResponse;
import DroidEye.Util.ResponseHandle;
import DroidEye.Util.ServerUtil;

public class LoginServlet extends HttpServlet{
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        String user = (String) httpRequest.getParameter("user");
        String pass = (String) httpRequest.getParameter("password");

        if (user.trim().equals(ServerUtil.getProperty("user")) &&
                pass.trim().equals(ServerUtil.getProperty("password"))) {
            //httpResponse.setRedirectName(ServerUtil.getProperty("loginPage"));
            ResponseHandle.printLoginService(httpRequest, httpResponse);
        } else {
            //httpResponse.setRedirectName(ServerUtil.getProperty("loginErrorPage"));
            ResponseHandle.printLoginErrorService(httpRequest,httpResponse);
        }

    }
}
