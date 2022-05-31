package cn.gtea.listener;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class AuthenticationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        String serverInfo = servletContext.getServerInfo();
        System.out.println("serverInfo = " + serverInfo);
        String servletContextName = servletContext.getServletContextName();
        System.out.println("servletContextName = " + servletContextName);
        servletContext.addListener("testListener");
        System.out.println("加载监听器...");
    }
}
