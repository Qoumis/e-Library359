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

@WebServlet(name = "BooksPerGenre", value = "/BooksPerGenre")
public class BooksPerGenre extends HttpServlet {

    /**This function returns to the client a json string containing every genre along with the number of books of that genre*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonArray jsonArr = getGenres();
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

    /**This function returns an array of json objects. Each object contains 2 fields.
     * The genre along with the number of books of that genre
     */
    public JsonArray getGenres() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT DISTINCT genre FROM books"); //get all the genres
            JsonObject jsonObj = new JsonObject();
            JsonArray  jsonArr = new JsonArray();

            while(rs.next()){       //for each genre
                jsonObj = DB_Connection.getResultsToJSONObject(rs); //put the genre in jsonobj

                String genre = jsonObj.get("genre").getAsString();
                int number = CountBooksOfGenre(genre);
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

    /**This function is used to count and return the total number of books with the given genre*/
    public int CountBooksOfGenre(String genre) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM books WHERE genre LIKE '%" + genre + "%'");
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
