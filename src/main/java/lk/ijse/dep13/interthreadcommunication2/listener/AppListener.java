package lk.ijse.dep13.interthreadcommunication2.listener;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import lk.ijse.dep13.interthreadcommunication2.db.MyCP;

import java.io.IOException;
import java.util.Set;

public class AppListener implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        try {
            MyCP myCP = new MyCP();
            servletContext.setAttribute("myCP", myCP);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
