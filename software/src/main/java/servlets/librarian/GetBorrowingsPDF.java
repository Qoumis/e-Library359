package servlets.librarian;

import com.google.gson.JsonArray;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@WebServlet(name = "GetBorrowingsPDF", value = "/GetBorrowingsPDF")
public class GetBorrowingsPDF extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /**Find library's id*/
        HttpSession session = request.getSession();
        String username = session.getAttribute("loggedIn").toString();
        try {
            int id = LibrarianActions.findLibID(username);
            JsonArray jsarr = getBorrowingsInfo(id);
            if(jsarr != null) {
                response.setStatus(200);
                response.getWriter().write(jsarr.toString());
            }
            else{
                response.setStatus(204);
                response.getWriter().write("No active borrowings in your library");
            }
        } catch (SQLException e) {
            response.setStatus(500);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            response.setStatus(500);
            e.printStackTrace();
        }

    }

    /**This function is used to get information about all the books that are borrowed from the library the given id*/
    public JsonArray getBorrowingsInfo(int library_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();

        ResultSet rs,rs1;
        try {
            rs = stmt.executeQuery("SELECT * FROM borrowing WHERE library_id='"+library_id+"'");

            JsonArray  jsonArr = new JsonArray();

            while(rs.next()){       //for each book
                JsonObject mainObj = new JsonObject();
                JsonObject BookTmpObj = DB_Connection.getResultsToJSONObject(rs);

                String status = BookTmpObj.get("status").getAsString();
                if(status.equals("borrowed") || status.equals("returned")){  //if the books is borrowed at that time
                    /**Get ISBN and Title*/
                    int id = BookTmpObj.get("bookcopy_id").getAsInt();
                    rs1 = stmt1.executeQuery("SELECT * FROM booksinlibraries WHERE bookcopy_id='"+id+"'");
                    rs1.next();
                    JsonObject tmpObj = DB_Connection.getResultsToJSONObject(rs1);
                    String isbn = tmpObj.get("isbn").getAsString();
                    mainObj.addProperty("ISBN",isbn);

                    rs1 = stmt1.executeQuery("SELECT title FROM books WHERE isbn='"+isbn+"'");
                    rs1.next();
                    tmpObj = DB_Connection.getResultsToJSONObject(rs1);
                    mainObj.addProperty("title",tmpObj.get("title").getAsString());

                    /**Get name-email-university-phone**/
                    id = BookTmpObj.get("user_id").getAsInt();
                    rs1 = stmt1 .executeQuery("SELECT firstname, lastname, email, university, telephone FROM students WHERE user_id='"+id+"'");
                    rs1.next();
                    tmpObj = DB_Connection.getResultsToJSONObject(rs1);
                    mainObj.addProperty("Student's First name", tmpObj.get("firstname").getAsString());
                    mainObj.addProperty("Student's Last name", tmpObj.get("lastname").getAsString());
                    mainObj.addProperty("University",tmpObj.get("university").getAsString());
                    mainObj.addProperty("email", tmpObj.get("email").getAsString());
                    mainObj.addProperty("telephone",tmpObj.get("telephone").getAsString());

                    /**Add status and days till return*/
                    mainObj.addProperty("Status",status);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date todate = sdf.parse(BookTmpObj.get("todate").getAsString());
                    Date currDate = sdf.parse(sdf.format(new Date()));

                    long diff = todate.getTime() - currDate.getTime();
                    long days = TimeUnit.MILLISECONDS.toDays(diff);
                    if(days < 0)
                        mainObj.addProperty("Days until return","Due date exceeded!");
                    else
                        mainObj.addProperty("Days until return",days);

                    jsonArr.add(mainObj);                            //add object to the array
                }
            }
            return jsonArr;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return null;
    }
}
