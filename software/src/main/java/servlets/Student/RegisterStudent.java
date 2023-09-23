package servlets.Student;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

import database.tables.EditStudentsTable;
import mainClasses.JSON_Converter;
import mainClasses.Student;
import mainClasses.filterInput;

@WebServlet(name = "RegisterStudent", value = "/RegisterStudent")
public class RegisterStudent extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /**Get HttpServletRequest body and convert it to String*/
        JSON_Converter jc = new JSON_Converter();
        String JsonString = jc.getJSONFromAjax(request.getReader());
        JsonString = filterInput.filter(JsonString);    //filter user's input for ass3
        System.out.println(JsonString);


        /**Save in database*/
        EditStudentsTable est = new EditStudentsTable();
        try {
            est.addStudentFromJSON(JsonString);
            /**Set response type, status and return Json string with user's given data*/
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            response.getWriter().write(JsonString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

