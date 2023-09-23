package servlets.Student;

import database.tables.EditStudentsTable;
import mainClasses.CheckDB;
import mainClasses.JSON_Converter;
import mainClasses.Student;
import mainClasses.filterInput;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "StudentData", value = "/StudentData")
public class StudentData extends HttpServlet {

    /**This function is used to return the student's date to the client*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        try {
            Student su = CheckDB.databaseToStudent(session.getAttribute("loggedIn").toString()); //get student from db
            EditStudentsTable eut = new EditStudentsTable();  //conver to json
            String json = eut.studentToJSON(su);
            response.setStatus(200);
            response.getWriter().write(json);               //return
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**This function is used to update the student's data in the database*/
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        String JsonString = jc.getJSONFromAjax(request.getReader());
        JsonString = filterInput.filter(JsonString);    //filter user's input for ass3

        try {
            CheckDB.UpdateStudentData(JsonString);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            response.getWriter().write(JsonString);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(403);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(403);
        }

    }
}
