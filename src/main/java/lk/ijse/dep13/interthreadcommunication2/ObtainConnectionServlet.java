package lk.ijse.dep13.interthreadcommunication2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep13.interthreadcommunication2.db.MyCP;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "obtain-connection-servlet",urlPatterns = "/connections/")
public class ObtainConnectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MyCP connectionPool = (MyCP) getServletContext().getAttribute("datasource");
        MyCP.ConnectionWrapper cWrapper = connectionPool.getConnection();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.printf("<h1>ID: %s</h1>", cWrapper.id());
        out.printf("<h1>Connection Ref: %s</h1>", cWrapper.connection());
    }
}
