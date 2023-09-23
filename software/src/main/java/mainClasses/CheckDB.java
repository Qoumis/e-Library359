package mainClasses;

import com.google.gson.Gson;
import database.DB_Connection;
import database.tables.EditLibrarianTable;
import database.tables.EditStudentsTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class CheckDB {

    /**This function is used to check if a book with that isbn already exists */
    public boolean checkDBforISBN(String isbn) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        /**Check in books' table */
        try {
            rs = stmt.executeQuery("SELECT isbn FROM books WHERE isbn = '" + isbn + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            if(json.contains(isbn))
                return true;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**This function is used to check if a book already exists in the library with the given id*/
    public boolean checkDBforBookInLib(String isbn, int library_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT isbn FROM booksinlibraries WHERE isbn = '" + isbn + "' AND library_id='" + library_id + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            if(json.contains(isbn))
                return true;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**This function is used to check if a user already exists with that username or email (depends on the value of option parameter)
     * @param option the value of that String is either 'username' or 'email'
     * @param value  This String contains the value of the username or the value of the email accordingly
     * */
    public boolean checkDBforUnames_emails(String value, String option) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs, rs1;
        /**Check in librarians' table */
        try {
            rs = stmt.executeQuery("SELECT " + option + " FROM librarians WHERE " + option +" = '" + value + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            if(json.contains(value))
                return true;
        } catch (Exception e) {
            System.err.println("1Got an exception! ");
            System.err.println(e.getMessage());
        }
        /**Check in students' table */
        try {
            rs1 = stmt.executeQuery("SELECT " + option + " FROM students WHERE " + option +" = '" + value + "'");
            rs1.next();
            String json = DB_Connection.getResultsToJSON(rs1);
            if(json.contains(value))
                return true;
        } catch (Exception e) {
            System.err.println("2Got an exception! ");
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**This function is used to check if a user with that academic id already exists */
    public boolean checkDBforIDs(String id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        /**Check in students' table */
        try {
            rs = stmt.executeQuery("SELECT student_id FROM students WHERE student_id = '" + id + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            if(json.contains(id))
                return true;
        } catch (Exception e) {
            System.err.println("1Got an exception! ");
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**This function is used for login only, to check if a *STUDENT* with that username exists
     *
     * We use this function only after databaseToStudent() returns null.
     * So if this function returns true it means that the user entered a wrong password.
     * Else if it returns true it means that the username doesn't exist in database
     * */
    public static boolean student_Uname_exists(String username)throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        /**Check in students' table */
        try {
            rs = stmt.executeQuery("SELECT username FROM students WHERE username = '" + username + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            if(json.contains(username))
                return true;
        } catch (Exception e) {
            System.err.println("1Got an exception! ");
            System.err.println(e.getMessage());
        }

        return false;
    }

    /**This function returns a student object given a username that exists in the database*/
    public static Student databaseToStudent(String username) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM students WHERE username = '" + username + "'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Student user = gson.fromJson(json, Student.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**This function returns a librarian object given a username that exists in the database*/
    public static Librarian databaseToLibrarian(String username) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians WHERE username = '" + username + "'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Librarian user = gson.fromJson(json, Librarian.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**This function is used to update the data of an existing librarian in the database*/
    public static void UpdateLibrarianData(String json) throws SQLException, ClassNotFoundException {

        /**Create a student object with the updated data*/
        EditLibrarianTable elt = new EditLibrarianTable();
        Librarian lb = elt.jsonToLibrarian(json);

        /**Create connection to db*/
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        /**Update each field*/
        String update = "UPDATE librarians SET birthdate='"+lb.getBirthdate()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET lat='"+lb.getLat()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET lon='"+lb.getLon()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);
        update = "UPDATE librarians SET password='"+lb.getPassword()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET firstname='"+lb.getFirstname()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET lastname='"+lb.getLastname()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET gender='"+lb.getGender()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET country='"+lb.getCountry()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET city='"+lb.getCity()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET address='"+lb.getAddress()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET telephone='"+lb.getTelephone()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET personalpage='"+lb.getPersonalpage()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET libraryinfo='"+lb.getLibraryinfo()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE librarians SET libraryname='"+lb.getLibraryname()+"' WHERE username = '"+lb.getUsername()+"'";
        stmt.executeUpdate(update);
    }

    /**This function is used to update the data of an existing student in the database*/
    public static void UpdateStudentData(String json) throws SQLException, ClassNotFoundException {

        /**Create a student object with the updated data*/
        EditStudentsTable est = new EditStudentsTable();
        Student su = est.jsonToStudent(json);

        /**Create connection to db*/
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        /**Update each field*/
        String update = "UPDATE students SET birthdate='"+su.getBirthdate()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE students SET lat='"+su.getLat()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE students SET lon='"+su.getLon()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);
        update = "UPDATE students SET password='"+su.getPassword()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE students SET firstname='"+su.getFirstname()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE students SET lastname='"+su.getLastname()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE students SET gender='"+su.getGender()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE students SET country='"+su.getCountry()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE students SET city='"+su.getCity()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE students SET address='"+su.getAddress()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE students SET telephone='"+su.getTelephone()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);

        update = "UPDATE students SET personalpage='"+su.getPersonalpage()+"' WHERE username = '"+su.getUsername()+"'";
        stmt.executeUpdate(update);
    }

    /**This function is used to update the number of pages of a book in the database*/
    public void updateBookPages(String isbn, String pages) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String update = "UPDATE books SET pages='" + pages + "'" + "WHERE isbn = '" + isbn + "'";
        stmt.executeUpdate(update);
    }

    /**This function is used to return all the books in the database with a specific genre/title/author etc*/
    public ArrayList<Book> databaseToBooksOfGenre(String genre, String fromYear, String toYear, String title, String authors,
                                                  String fromPage, String toPage) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Book> books = new ArrayList<Book>();
        ResultSet rs;
        try {
            /**Find books of genre and optional year-> from assignment 3*/
            if(fromYear.equals("") && toYear.equals("")){             //user didn't give optional date at all
                if(genre.equalsIgnoreCase("all"))
                    rs = stmt.executeQuery("SELECT * FROM books");
                else
                    rs = stmt.executeQuery("SELECT * FROM books WHERE genre LIKE '" + genre + "%'");
            }
            else if(!fromYear.equals("") && !toYear.equals("")){    //user gave both from and to date
                if(genre.equalsIgnoreCase("all"))
                    rs = stmt.executeQuery("SELECT * FROM books WHERE publicationyear BETWEEN " + fromYear + " AND " + toYear);
                else
                    rs = stmt.executeQuery("SELECT * FROM books WHERE genre LIKE '" + genre + "%' AND publicationyear BETWEEN " + fromYear + " AND " + toYear);
            }
            else if(!toYear.equals("")){                          //user gave only toYear
                if(genre.equalsIgnoreCase("all"))
                    rs = stmt.executeQuery("SELECT * FROM books WHERE publicationyear <= " + toYear);
                else
                    rs = stmt.executeQuery("SELECT * FROM books WHERE genre LIKE '" + genre + "%' AND publicationyear <= " + toYear);
            }
            else{                                                 //user gave only fromYear
                if(genre.equalsIgnoreCase("all"))
                    rs = stmt.executeQuery("SELECT * FROM books WHERE publicationyear >= " + fromYear);
                else
                    rs = stmt.executeQuery("SELECT * FROM books WHERE genre LIKE '" + genre + "%' AND publicationyear >= " + fromYear);
            }
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Book book = gson.fromJson(json, Book.class);
                books.add(book);      //this is the main list
            }
            /**Find books with that title*/
            if(!title.equals("")){ //(if the user gave a title of course)
                ArrayList<Book> title_books = new ArrayList<Book>();
                rs = stmt.executeQuery("SELECT * FROM books WHERE title LIKE '%" + title + "%'");
                while(rs.next()){
                    String json = DB_Connection.getResultsToJSON(rs);
                    Gson gson = new Gson();
                    Book book = gson.fromJson(json, Book.class);
                    title_books.add(book);
                }
                books.retainAll(title_books); //remove from the main list all the books that don't contain that title keyword
            }
            /**Find books of that authors*/
            if(!authors.equals("")){
                ArrayList<Book> author_books = new ArrayList<Book>();
                rs = stmt.executeQuery("SELECT * FROM books WHERE authors LIKE '%" + authors + "%'");
                while(rs.next()){
                    String json = DB_Connection.getResultsToJSON(rs);
                    Gson gson = new Gson();
                    Book book = gson.fromJson(json, Book.class);
                    author_books.add(book);
                }
                books.retainAll(author_books); //remove from the main list all the books that don't contain that author keyword
            }
            /**Find books of the given page number range*/
            if(!fromPage.equals("") || !toPage.equals("")) {
                ArrayList<Book> page_books = new ArrayList<Book>();
                if (!fromPage.equals("") && !toPage.equals("")) //user gave pages number in range
                    rs = stmt.executeQuery("SELECT * FROM books WHERE pages BETWEEN " + fromPage + " AND " + toPage);
                else if(!fromPage.equals(""))    //user gave only fromPage number
                    rs = stmt.executeQuery("SELECT * FROM books WHERE pages >= " + fromPage);
                else                            //user gave only toPage number
                    rs = stmt.executeQuery("SELECT * FROM books WHERE pages <= " + toPage);

                while (rs.next()) {
                    String json = DB_Connection.getResultsToJSON(rs);
                    Gson gson = new Gson();
                    Book book = gson.fromJson(json, Book.class);
                    page_books.add(book);
                }
                books.retainAll(page_books);
            }
            return books;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return null;
    }
}
