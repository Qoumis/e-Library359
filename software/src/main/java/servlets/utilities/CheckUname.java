package servlets.utilities;

import mainClasses.CheckDB;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet(name = "CheckUname", value = "/CheckUname")
public class CheckUname extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        /**Check if a user already exists with that username*/
        CheckDB cdb = new CheckDB();
        boolean already_exists = false;
        try {
            already_exists = cdb.checkDBforUnames_emails(username, "username");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        /**Send corresponding response to the client*/
        if(already_exists){
            response.setStatus(409);
        }
        else{
            response.setStatus(200);
        }
    }

}
