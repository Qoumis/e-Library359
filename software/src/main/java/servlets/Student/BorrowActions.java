package servlets.Student;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.DB_Connection;
import database.tables.EditBooksInLibraryTable;
import database.tables.EditBorrowingTable;
import mainClasses.Borrowing;

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
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@WebServlet(name = "BorrowActions", value = "/BorrowActions")
public class BorrowActions extends HttpServlet {

    /**This function is used to return information about all the books that the user
     * is borrowing or has borrowed in the past.*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = session.getAttribute("loggedIn").toString();

        try {
            int user_id = findUserID(username);
            response.setStatus(200);
            response.getWriter().write(getBorrowedBooks(user_id).toString());
        } catch (SQLException e) {
            response.setStatus(500);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            response.setStatus(500);
            e.printStackTrace();
        } catch (ParseException e) {
            response.setStatus(500);
            e.printStackTrace();
        }
    }

    /**This function is used to create a new borrowing*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String bookId   = request.getParameter("bookcopy_id");
        String username = session.getAttribute("loggedIn").toString();

        try {
            updateBorrowTables(Integer.parseInt(bookId), username);
            response.setStatus(200);
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

    /**This function is used to update the status of a book in the database*/
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        int id = Integer.parseInt(request.getParameter("borrowing_id"));

        EditBorrowingTable ebt = new EditBorrowingTable();
        try {
            ebt.updateBorrowing(id, "returned");
            response.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    /**This function is used to create a new entry in borrowing table and also makes the book unavailable for other students*/
    public void updateBorrowTables(int bookcopy_id, String username) throws SQLException, ClassNotFoundException, ParseException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        JsonObject obj;

        /**Make the book in that library unavailable*/
        EditBooksInLibraryTable eblb = new EditBooksInLibraryTable();
        eblb.updateBookInLibrary(bookcopy_id, "false");

        /**Create new entry to borrowing table*/
        Borrowing new_bor = new Borrowing();
            //BookNo & status
        new_bor.setBookcopy_id(bookcopy_id);
        new_bor.setStatus("requested");
            //student_id
        new_bor.setUser_id(findUserID(username));
            //library_id
        rs = stmt.executeQuery("SELECT library_id FROM booksinlibraries WHERE bookcopy_id = '" + bookcopy_id + "'");
        rs.next();
        obj = DB_Connection.getResultsToJSONObject(rs);
        new_bor.setLibrary_id(obj.get("library_id").getAsInt());
            //dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromdate = sdf.parse(sdf.format(new Date()));
        LocalDate todate = LocalDate.parse(sdf.format(new Date())).plusMonths(1);
        new_bor.setFromDate(sdf.format(fromdate));
        new_bor.setToDate(todate.toString());

        EditBorrowingTable ebtb = new EditBorrowingTable();
        ebtb.createNewBorrowing(new_bor);

        /**Increase giveaway counter for that student*/
        stmt.executeUpdate("UPDATE students SET borrow_count = borrow_count + 1 WHERE username = '" + username + "'");

    }

    /**This function is used to find a student's id given their username*/
    public static int findUserID(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        JsonObject obj;
        rs = stmt.executeQuery("SELECT user_id FROM students WHERE username = '" + username + "'");
        rs.next();
        obj = DB_Connection.getResultsToJSONObject(rs);

        return obj.get("user_id").getAsInt();
    }

    /**This function returns a json Array containing information about all the books that the user
     * is borrowing or has borrowed in the past.
     */
    public JsonArray getBorrowedBooks(int user_id) throws SQLException, ClassNotFoundException, ParseException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();
        ResultSet rs,rs1;

        JsonArray  jsonArr = new JsonArray();
        rs = stmt.executeQuery("SELECT * FROM borrowing WHERE user_id='"+user_id+"'");
        while(rs.next()){
            JsonObject mainObj = new JsonObject();
            JsonObject tmpObj = DB_Connection.getResultsToJSONObject(rs);

            /**Find and add ISBN/tittle*/
            int bookcopy_id = tmpObj.get("bookcopy_id").getAsInt();
            rs1 = stmt1.executeQuery("SELECT isbn FROM booksinlibraries WHERE bookcopy_id='"+bookcopy_id+"'");
            rs1.next();
            JsonObject bookTmp = DB_Connection.getResultsToJSONObject(rs1);

            String isbn = bookTmp.get("isbn").getAsString();
            rs1 = stmt1.executeQuery("SELECT title FROM books WHERE isbn='"+isbn+"'");
            rs1.next();
            bookTmp = DB_Connection.getResultsToJSONObject(rs1);

            /**Add the rest of the properties*/
            mainObj.addProperty("Title",bookTmp.get("title").getAsString());
            mainObj.addProperty("ISBN", isbn);
            mainObj.addProperty("borrowing_id", tmpObj.get("borrowing_id").getAsString());
            mainObj.addProperty("Status", tmpObj.get("status").getAsString());

            /**Calculate and add days_unit_return property*/
            if(tmpObj.get("status").getAsString().equals("successEnd") || tmpObj.get("status").getAsString().equals("returned"))
                mainObj.addProperty("Days until return", "Book has been returned");
            else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date currDate = sdf.parse(sdf.format(new Date()));
                Date todate = sdf.parse(tmpObj.get("todate").getAsString());

                long diff = todate.getTime() - currDate.getTime();
                long days = TimeUnit.MILLISECONDS.toDays(diff);

                if(days < 0)
                    mainObj.addProperty("Days until return","Due date exceeded!");
                else
                    mainObj.addProperty("Days until return",days);
            }

            jsonArr.add(mainObj);
        }

        return jsonArr;
    }
}
