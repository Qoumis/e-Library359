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

@WebServlet(name = "GetStudentStats", value = "/GetStudentStats")
public class GetStudentStats extends HttpServlet {

    /**This function returns to the client a json string containing every student type along with the number of that type*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonArray jsonArr = getStudents();
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
     * The student type along with the number of that type
     */
    public JsonArray getStudents() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT DISTINCT student_type FROM students"); //get all the types
            JsonObject jsonObj = new JsonObject();
            JsonArray  jsonArr = new JsonArray();

            while(rs.next()){       //for each student type
                jsonObj = DB_Connection.getResultsToJSONObject(rs); //put the type in jsonobj

                String type = jsonObj.get("student_type").getAsString();
                int number = CountStudents(type);
                jsonObj.addProperty("Number",number);    //add "number of students" field to the object

                jsonArr.add(jsonObj);                            //add object to the array
            }
            return jsonArr;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**This function is used to count and return the total number of students of the given type*/
    public int CountStudents(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM students WHERE student_type= '" + type + "'");
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
