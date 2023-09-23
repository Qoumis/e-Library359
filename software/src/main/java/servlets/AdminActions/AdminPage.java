package servlets.AdminActions;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet(name = "adminActions", value = "/adminActions")
public class AdminPage extends HttpServlet {

    /**This function reads a html file and returns it to the client*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        File htmlFile = new File("C:/Users/30698/Desktop/hy359/hy359_project/src/main/webapp/Admin.html");
        Scanner myReader = new Scanner(htmlFile);
        PrintWriter out = response.getWriter();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            out.println(data);
        }
        myReader.close();
    }
}

