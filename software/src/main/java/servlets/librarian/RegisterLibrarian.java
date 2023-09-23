package servlets.librarian;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import database.tables.EditLibrarianTable;
import mainClasses.CheckDB;
import mainClasses.JSON_Converter;
import mainClasses.filterInput;


@WebServlet(name = "RegisterLibrarian", value = "/RegisterLibrarian")
public class RegisterLibrarian extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /**Get HttpServletRequest body and convert it to String*/
        JSON_Converter jc = new JSON_Converter();
        String JsonString = jc.getJSONFromAjax(request.getReader());
        JsonString = filterInput.filter(JsonString);    //filter user's input for ass3
        System.out.println(JsonString);

        /**Save in database*/
           EditLibrarianTable est = new EditLibrarianTable();
            try {
                est.addLibrarianFromJSON(JsonString);
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
