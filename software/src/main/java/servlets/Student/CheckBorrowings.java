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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static servlets.Student.BorrowActions.findUserID;

@WebServlet(name = "CheckBorrowings", value = "/CheckBorrowings")
public class CheckBorrowings extends HttpServlet {

    /**This function is used to check if the user has any books that need to be returned in 3 days or less and returns
     *corresponding messages to the client
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = session.getAttribute("loggedIn").toString();
        String message = "";

        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs;
            int id = findUserID(username);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currDate = sdf.parse(sdf.format(new Date()));

            rs = stmt.executeQuery("SELECT * FROM borrowing WHERE user_id = '" + id + "' AND status = 'borrowed'");
            while(rs.next()){
                JsonObject obj = new JsonObject();
                obj = DB_Connection.getResultsToJSONObject(rs);
                Date todate = sdf.parse(obj.get("todate").getAsString());

                long diff = todate.getTime() - currDate.getTime();
                long days = TimeUnit.MILLISECONDS.toDays(diff);

                if(days < 0)
                    message += "Warning: One of your borrowed books has exceeded it's return date! Return it at once!<br>";
                else if(days <= 3)
                    message += "Reminder: One of your borrowed books is due to return in " + days + " days.<br>";
            }
            if(message.equals(""))      //no books found
                response.setStatus(204);
            else{
                response.setStatus(200);
                response.getWriter().write(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ParseException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
}

