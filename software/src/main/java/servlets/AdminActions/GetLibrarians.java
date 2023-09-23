package servlets.AdminActions;

import database.DB_Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "GetLibrarians", value = "/GetLibrarians")
public class GetLibrarians extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = null;
        try {
            json = LibrariansToJSON();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(json != null){
            response.setStatus(200);
            response.getWriter().write(json);
        }

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        try {
            deleteLibrarians(username);
            response.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    /**This function is used to delete a librarian from the database*/
    public void deleteLibrarians(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String deleteQuery = "DELETE FROM librarians WHERE username='" + username + "'";
        stmt.executeUpdate(deleteQuery);
        stmt.close();
        con.close();
    }

    /**This function returns the uname,first and last names of all the librarians in the database*/
    public String LibrariansToJSON() throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT username,firstname,lastname FROM librarians");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            while(rs.next())
                json += "," + DB_Connection.getResultsToJSON(rs);
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
}
