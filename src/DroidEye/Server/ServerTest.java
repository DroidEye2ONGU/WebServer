package DroidEye.Server;

public class ServerTest {
    public static void main(String[] args) throws Exception {
        //搭建启动一个服务器[封装一个服务器类]
        Server server = new Server();
        server.getConnect();
        //浏览器输入URL连接到服务器

        //服务器将所有的请求信息输出

        //解析出来请求资源 协议类型

        //去项目资源目录中查看 请求的资源是否存在
        //如果存在 IO流提取,发送回去浏览器[按照格式发送[
        //如果不存在,error.html错误提示页面
    }
}
