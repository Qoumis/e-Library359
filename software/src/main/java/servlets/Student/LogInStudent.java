package servlets.Student;

import database.tables.EditStudentsTable;
import mainClasses.CheckDB;
import mainClasses.Student;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "LogInStudent", value = "/LogInStudent")
public class LogInStudent extends HttpServlet {

    /**This function is used to log the user in*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=request.getParameter("username");
        String password=request.getParameter("password");

        try (PrintWriter out = response.getWriter()) {
            EditStudentsTable eut = new EditStudentsTable();
            Student su = eut.databaseToStudent(username, password);

            if(su==null){                                   //student with that password was not found...
                response.setStatus(403);
                if(CheckDB.student_Uname_exists(username))                          //if username exists it means that they gave wrong password
                    out.println("Wrong Password!");
                else
                    out.println("Username doesn't match an existing student!");       //else the student with that username doesn't exist at all
            }
            else{                                           //student found in DB we return their username as a string
                response.setStatus(200);
                out.println(username);

                HttpSession session=request.getSession();       //keep the user logged-in
                session.setAttribute("loggedIn",username);   //bind the username and type to this session
                session.setAttribute("user_type","student");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
