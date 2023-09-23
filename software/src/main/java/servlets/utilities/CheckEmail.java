package servlets.utilities;

import mainClasses.CheckDB;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CheckEmail", value = "/CheckEmail")
public class CheckEmail extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        /**Check if a user already exists with that email*/
        CheckDB cdb = new CheckDB();
        boolean already_exists = false;
        try {
            already_exists = cdb.checkDBforUnames_emails(email, "email");
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
