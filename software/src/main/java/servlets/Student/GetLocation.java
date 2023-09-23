package servlets.Student;

import com.google.gson.JsonObject;
import database.DB_Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "GetLocation", value = "/GetLocation")
public class GetLocation extends HttpServlet {

    /**Returns the student's lat/lon to the client*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = session.getAttribute("loggedIn").toString();

        try {
            response.setStatus(200);
            response.getWriter().write(get_lat_lon(username).toString());
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    /**This function is used to find the logged-in student's lat and lon from the database*/
    public JsonObject get_lat_lon(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        rs = stmt.executeQuery("SELECT lat,lon FROM students WHERE username='"+username+"'");
        rs.next();

        return DB_Connection.getResultsToJSONObject(rs);
    }
}
