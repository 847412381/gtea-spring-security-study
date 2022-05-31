package cn.gtea.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session == null) {
            writeContent(response, "请登录");
            return false;
        }else {
            Object jsession = session.getAttribute("JSESSION");
            if (jsession == null) {
                writeContent(response, "请登录");
                return false;
            }else {
                writeContent(response, ("你好" + jsession));
                return true;
            }
        }
    }

    //响应输出
    private void writeContent(HttpServletResponse resp, String msg) throws IOException {
        resp.setContentType("text/html;charset=utf8");
        PrintWriter writer = resp.getWriter();
        writer.print(msg);
        writer.close();
    }
}
