package servlets.AdminActions;

import com.google.gson.JsonObject;
import database.DB_Connection;
import database.tables.EditAdminMessageTable;
import mainClasses.AdminMessage;
import mainClasses.Competitor;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@WebServlet(name = "Giveaway", value = "/Giveaway")
public class Giveaway extends HttpServlet {

    /**When a user loads the page*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
           Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs;
            JsonObject obj;

            rs = stmt.executeQuery("SELECT * FROM adminmessages WHERE message_id=(SELECT max(message_id) FROM adminmessages)");
            rs.next();
            obj = DB_Connection.getResultsToJSONObject(rs);
            if(obj.get("message_id").getAsInt() >=2 ){
                response.setStatus(200);
                response.getWriter().write(obj.get("message").getAsString());
            }
            else
                response.setStatus(204);

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
        }

    }

    /**When admin runs the giveaway*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Random rand = new Random();
        ArrayList<Competitor> competitors = new ArrayList<Competitor>();
        int TotalEntries = 0;
        String message = "The winners of the giveaway for this month are : <br>";
        response.getWriter().write("Winners: <br>");

        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs;
            JsonObject obj;

            rs = stmt.executeQuery("SELECT username, borrow_count FROM students WHERE borrow_count > 0");
            while(rs.next()){
                obj = DB_Connection.getResultsToJSONObject(rs);
                Competitor c = new Competitor(obj.get("username").getAsString(), TotalEntries, obj.get("borrow_count").getAsInt());
                TotalEntries+= obj.get("borrow_count").getAsInt();

                competitors.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
        }

        /**Random number from 1 to Total number of entries*/
        int randomNumber = rand.nextInt(TotalEntries) + 1;
        for(Competitor c : competitors){
            if(c.checkForWinner(randomNumber)) {
                message +="1) " + c.getName() + " won a Harry Potter collector\\'s edition book with " + c.getEntry_count() + " entries. <br>";
                response.getWriter().write("1) " + c.getName() + " with " + c.getEntry_count() + " entries. <br>");
                competitors.remove(c);
                break;
            }
        }
        //Reassign entries for 2nd giveaway
        TotalEntries = 0;
        for(Competitor c : competitors){
            c.reAssignEntries(TotalEntries, c.getEntry_count());
            TotalEntries+= c.getEntry_count();
        }

        //run 2nd giveaway
        randomNumber = rand.nextInt(TotalEntries) + 1;
        for(Competitor c : competitors) {
            if (c.checkForWinner(randomNumber)) {
                message += "2) " + c.getName() + " won a HP printer with " + c.getEntry_count() + " entries.";
                response.getWriter().write("2) " + c.getName() + " with " + c.getEntry_count() + " entries. <br>");
                break;
            }
        }
        /**Add giveaway's results to adminMessages table so that we can display the message in user's Home page*/
        AdminMessage admin_msg = new AdminMessage();
        admin_msg.setMessage(message);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        admin_msg.setDate(date);

        EditAdminMessageTable eamt = new EditAdminMessageTable();
        try {
            eamt.createNewAdminMessage(admin_msg);
            response.setStatus(200);
        } catch (ClassNotFoundException e) {
            response.setStatus(500);
            e.printStackTrace();
        }
    }

}