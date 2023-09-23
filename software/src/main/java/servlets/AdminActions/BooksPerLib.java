package servlets.AdminActions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

@WebServlet(name = "BooksPerLib", value = "/BooksPerLib")
public class BooksPerLib extends HttpServlet{

    /**This function returns to the client a json string containing every library id, name and the number
     * of books in that library*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            JsonArray jsonArr = getLibraries();
            response.setStatus(200);
            response.getWriter().write(jsonArr.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
        }

    }

    /**This function returns an array of json objects. Each object contains 3 fields.
     * The library's id, name and number of books in it.
     */
    public JsonArray getLibraries() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT library_id,libraryname FROM librarians"); //get the name and id of all libs
            JsonObject jsonObj = new JsonObject();
            JsonArray  jsonArr = new JsonArray();

            while(rs.next()){       //for each library
                jsonObj = DB_Connection.getResultsToJSONObject(rs); //put the id and name in jsonobj

                int id = jsonObj.get("library_id").getAsInt();
                int number = CountBooksInLibrary(id);
                jsonObj.addProperty("NoBooks",number);    //add "number of books" field to the object

                jsonArr.add(jsonObj);                            //add object to the array
            }
            return jsonArr;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**This function is used to count and return the total number of books in library with given id*/
    public int CountBooksInLibrary(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries WHERE library_id= '" + id + "'");
            int n = 0;
            while(rs.next())
                n++;
            return n;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return 0;
    }

}