package servlets.librarian;

import database.tables.EditLibrarianTable;
import database.tables.EditStudentsTable;
import mainClasses.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LibrarianData", value = "/LibrarianData")
public class LibrarianData extends HttpServlet {

    /**This function is used to return the student's date to the client*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        try {
            Librarian lb = CheckDB.databaseToLibrarian(session.getAttribute("loggedIn").toString()); //get librarian from db
            EditLibrarianTable eut = new EditLibrarianTable();  //conver to json
            String json = eut.librarianToJSON(lb);
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
            CheckDB.UpdateLibrarianData(JsonString);
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