package servlets.Student;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.DB_Connection;
import mainClasses.Librarian;

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
import java.util.ArrayList;

@WebServlet(name = "GetLibraries", value = "/GetLibraries")
public class GetLibraries extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");

        try {
            ArrayList<Integer> lib_ids =  getAvailableLibraries(isbn);
            if(lib_ids.size() > 0){
                JsonArray jsArr = new JsonArray();
                for(int i = 0; i < lib_ids.size(); i++)     //Add each available lib to the json Array
                    jsArr.add(getLibraryInfo(lib_ids.get(i), isbn));

                response.setStatus(200);
                response.getWriter().write(jsArr.toString());
            }
            else        //if there are no available libraries for that book.
                response.setStatus(204);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    /**This function is used to find and return all the library_ids that the book with the given isbn is available*/
    public ArrayList<Integer> getAvailableLibraries(String isbn) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        rs = stmt.executeQuery("SELECT library_id FROM booksinlibraries WHERE isbn='"+isbn+"' AND available='"+"true"+"'");
        ArrayList<Integer> lib_ids = new ArrayList<Integer>();
        while(rs.next()){
            JsonObject obj = new JsonObject();
            obj = DB_Connection.getResultsToJSONObject(rs);
            lib_ids.add(obj.get("library_id").getAsInt());
        }

        return lib_ids;
    }

    /**This function gets all the library info we need for the library with the given id*/
    public JsonObject getLibraryInfo(int id, String isbn) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        JsonObject obj = new JsonObject(), tmp = new JsonObject();
        rs = stmt.executeQuery("SELECT libraryname, libraryinfo, city, address, email, telephone, lat, lon FROM librarians WHERE library_id='"+id+"'");
        rs.next();
        obj = DB_Connection.getResultsToJSONObject(rs);

        /**Get book copy id as well (we need it later on when user borrows the book from this library)*/
        rs = stmt.executeQuery("SELECT bookcopy_id FROM booksinlibraries WHERE library_id='"+id+"' AND isbn='"+isbn+"'");
        rs.next();
        tmp = DB_Connection.getResultsToJSONObject(rs);
        obj.addProperty("bookcopy_id", tmp.get("bookcopy_id").getAsInt());

        return obj;
    }
}