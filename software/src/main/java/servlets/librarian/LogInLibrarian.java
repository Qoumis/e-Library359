package servlets.librarian;

import database.tables.EditLibrarianTable;
import mainClasses.CheckDB;
import mainClasses.Librarian;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "LogInLibrarian", value = "/LogInLibrarian")
public class LogInLibrarian extends HttpServlet {

    /**This function is used to log the user in*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=request.getParameter("username");
        String password=request.getParameter("password");

        try (PrintWriter out = response.getWriter()) {
            EditLibrarianTable eut = new EditLibrarianTable();
            Librarian lb = eut.databaseToLibrarian(username, password);

            if(lb==null){                                   //librarian with that username/password was not found...
                response.setStatus(403);
                out.println("Wrong Credentials!");
            }
            else{                                           //librarian found in DB we return their username as a string
                response.setStatus(200);
                out.println(username);

                HttpSession session=request.getSession();       //keep the user logged-in
                session.setAttribute("loggedIn",username);   //bind the username and type to this session
                session.setAttribute("user_type","librarian");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
