package servlets.librarian;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.DB_Connection;
import database.tables.EditBooksInLibraryTable;
import database.tables.EditBorrowingTable;
import mainClasses.BookInLibrary;
import mainClasses.CheckDB;


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

@WebServlet(name = "LibrarianActions", value = "/LibrarianActions")
public class LibrarianActions extends HttpServlet {

    /**This function returns to the client information about borrow requests*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /**First we find the library id*/
        HttpSession session = request.getSession();
        String username = session.getAttribute("loggedIn").toString();
        try {
            int id = findLibID(username);
            response.setStatus(200);
            response.getWriter().write(getBRrequestInfo(id).toString());  //get information and return it
        } catch (SQLException e) {
            response.setStatus(500);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            response.setStatus(500);
            e.printStackTrace();
        }
    }


    /**This function is used to make a book available to a library* (Adds a new entry to the booksinlibraries table)*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");

        /**First check if a book with that isbn exists */
        CheckDB cdb = new CheckDB();
        boolean book_exists = false;
        try {
            book_exists = cdb.checkDBforISBN(isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(!book_exists){       //Book doesnt exist return error
            response.setStatus(404);
            response.getWriter().write("That ISBN does not match an existing book");
        }
        else {
            /**Then we need to find library_id*/
            HttpSession session = request.getSession();
            String username = session.getAttribute("loggedIn").toString();
            int id = 0;
            try {
                id = findLibID(username);
                book_exists = cdb.checkDBforBookInLib(isbn, id);
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(500);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.setStatus(500);
            }
            if(book_exists){       //Book already exists in that library
                response.setStatus(409);
                response.getWriter().write("A book with that ISBN already exists in your library!");
            }
            else{               //else we make the book avilable
                EditBooksInLibraryTable ebl = new EditBooksInLibraryTable();
                BookInLibrary newBook = new BookInLibrary();
                newBook.setIsbn(isbn);
                newBook.setLibrary_id(id);
                newBook.setAvailable("true");
                try {
                    response.setStatus(200);
                    response.getWriter().write(isbn);
                    ebl.createNewBookInLibrary(newBook);
                } catch (ClassNotFoundException e) {
                    response.setStatus(500);
                    e.printStackTrace();
                }
            }
        }
    }

    /**This function is used to update the status of a book in the database*/
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        int id = Integer.parseInt(request.getParameter("borrowing_id"));
        try {
            updateStatus(id);
            response.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
        }

    }

    /**This function is used to update the status of a book in the database*/
    public void updateStatus(int borrowing_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {   //get current status
            rs = stmt.executeQuery("SELECT status FROM borrowing WHERE borrowing_id = '" + borrowing_id + "'");
            rs.next();
            JsonObject json = DB_Connection.getResultsToJSONObject(rs);
            String status = json.get("status").getAsString();
            EditBorrowingTable ebt = new EditBorrowingTable();
            if(status.equals("requested"))
                ebt.updateBorrowing(borrowing_id, "borrowed");
            else {
                ebt.updateBorrowing(borrowing_id, "successEnd");
                /**When student returns the book we have to make it available in booksinlibraries table,
                 * so that it can be borrowed again
                 */
                rs = stmt.executeQuery("SELECT bookcopy_id FROM borrowing WHERE borrowing_id = '" + borrowing_id + "'");
                rs.next();
                json = DB_Connection.getResultsToJSONObject(rs);
                int bookcopy_id = json.get("bookcopy_id").getAsInt();
                EditBooksInLibraryTable ebilb = new EditBooksInLibraryTable();
                ebilb.updateBookInLibrary(bookcopy_id,"true");
            }

        }
        catch (Exception e) {
        System.err.println("Got an exception! ");
        System.err.println(e.getMessage());
    }

    }

    /**This function is used to find the library_id of the librarian with the given username*/
    public static int findLibID(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT library_id FROM librarians WHERE username = '" + username + "'");
            rs.next();
            JsonObject json=DB_Connection.getResultsToJSONObject(rs);
            return json.get("library_id").getAsInt();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return 0;
    }

    /**This function finds the ISBN of a book, the name-email of the student that is borrowing the book along with the
     * borrowing id and status.Then it creates a JsonObject with those properties for each borrowed book of the library,
     * adds its to a JsonArray and returns it to the client.
     */
    public JsonArray getBRrequestInfo(int library_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        Statement stmt1 = con.createStatement();

        ResultSet rs,rs1;
        try {
            rs = stmt.executeQuery("SELECT borrowing_id, bookcopy_id, user_id, status FROM " +
                    "borrowing WHERE library_id='"+library_id+"'"); //we need bookcopy_id to find ISBN and user_id to find name etc.
            JsonObject mainObj = new JsonObject();
            JsonArray  jsonArr = new JsonArray();

            while(rs.next()){       //for each book
                mainObj = DB_Connection.getResultsToJSONObject(rs);

                /**Get ISBN*/
                int id = mainObj.get("bookcopy_id").getAsInt();
                rs1 = stmt1.executeQuery("SELECT isbn FROM booksinlibraries WHERE bookcopy_id='"+id+"'");
                rs1.next();
                JsonObject tmpObj = DB_Connection.getResultsToJSONObject(rs1);
                mainObj.addProperty("isbn", tmpObj.get("isbn").getAsString());
                mainObj.remove("bookcopy_id");

                /**Get name-email*/
                id = mainObj.get("user_id").getAsInt();
                rs1 = stmt1 .executeQuery("SELECT firstname, lastname, email FROM students WHERE user_id='"+id+"'");
                rs1.next();
                tmpObj = DB_Connection.getResultsToJSONObject(rs1);
                mainObj.addProperty("firstname", tmpObj.get("firstname").getAsString());
                mainObj.addProperty("lastname", tmpObj.get("lastname").getAsString());
                mainObj.addProperty("email", tmpObj.get("email").getAsString());
                mainObj.remove("user_id");

                jsonArr.add(mainObj);                            //add object to the array
            }
            return jsonArr;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return null;
    }
}
