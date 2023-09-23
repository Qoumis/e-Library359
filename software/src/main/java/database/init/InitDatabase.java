/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.init;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import database.tables.EditBooksTable;
import static database.DB_Connection.getInitialConnection;
import database.tables.EditAdminMessageTable;
import database.tables.EditReviewsTable;
import database.tables.EditBorrowingTable;
import database.tables.EditLibrarianTable;
import database.tables.EditStudentsTable;
import database.tables.EditBooksInLibraryTable;
import database.tables.GeneralQueries;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import mainClasses.Book;
import mainClasses.Librarian;
import mainClasses.Review;
import mainClasses.Student;

/*
 *
 * @author micha
 */
public class InitDatabase {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        InitDatabase init = new InitDatabase();
        init.initDatabase();
        init.initTables();
        init.addToDatabaseExamples();
        init.updateRecords();
        init.databaseToJSON();

        // init.deleteRecords();
    }

    public void initDatabase() throws SQLException, ClassNotFoundException {
        Connection conn = getInitialConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE DATABASE HY359_2022");
        stmt.close();
        conn.close();
    }

    public void initTables() throws SQLException, ClassNotFoundException {
        EditStudentsTable eut = new EditStudentsTable();
        eut.createStudentsTable();

        EditLibrarianTable edt = new EditLibrarianTable();
        edt.createLibrariansTable();

        EditBooksTable ett = new EditBooksTable();
        ett.createBooksTable();

        EditBooksInLibraryTable etr = new EditBooksInLibraryTable();
        etr.createBooksInLibrary();

        EditBorrowingTable er = new EditBorrowingTable();
        er.createBorrowingTable();

        EditReviewsTable emt = new EditReviewsTable();
        emt.createReviewTable();

        EditAdminMessageTable eam = new EditAdminMessageTable();
        eam.createAdminMessageTable();

    }

    public void addToDatabaseExamples() throws ClassNotFoundException, SQLException {
        //Users

        EditStudentsTable eut = new EditStudentsTable();
        eut.addStudentFromJSON(Resources.studentJSON);
        eut.addStudentFromJSON(Resources.student2JSON);
        eut.addStudentFromJSON(Resources.student3JSON);

        //  Librarians
        EditLibrarianTable edt = new EditLibrarianTable();
        edt.addLibrarianFromJSON(Resources.vikelaia);
        edt.addLibrarianFromJSON(Resources.uocLibraryHER);
        edt.addLibrarianFromJSON(Resources.uocLibraryRETH);
        edt.addLibrarianFromJSON(Resources.helmepaLibrary);
        edt.addLibrarianFromJSON(Resources.tucLibrary);

        //Book
        EditBooksTable ebt = new EditBooksTable();

        ebt.addBookFromJSON(Resources.book1);
        ebt.addBookFromJSON(Resources.book2);
        ebt.addBookFromJSON(Resources.book3);
        ebt.addBookFromJSON(Resources.book4);
        ebt.addBookFromJSON(Resources.book5);
        ebt.addBookFromJSON(Resources.book6);
        ebt.addBookFromJSON(Resources.book7);
        ebt.addBookFromJSON(Resources.book8);
        ebt.addBookFromJSON(Resources.book9);
        ebt.addBookFromJSON(Resources.book10);

        //Add book to Libraries
        this.addBookToLibraries();

        // Borrowing
        String borrowingJSON = "{\"bookcopy_id\":\"1\",\"user_id\":\"1\",\"library_id\":\"1\","
                + "\"fromDate\":\"2022-11-18\"," + "\"toDate\":\"2022-12-18\","
                + "\"status\":\"requested\"}";
        EditBorrowingTable er = new EditBorrowingTable();
        er.addBorrowingFromJSON(borrowingJSON);
        EditBooksInLibraryTable ebl = new EditBooksInLibraryTable();
        ebl.updateBookInLibrary(1, "false");

        //Reviews
        String borrowingJSON2 = "{\"bookcopy_id\":\"1\",\"user_id\":\"2\",\"library_id\":\"1\","
                + "\"fromDate\":\"2022-08-18\"," + "\"toDate\":\"2022-09-18\","
                + "\"status\":\"successEnd\"}";
        er.addBorrowingFromJSON(borrowingJSON2);

        EditReviewsTable ett = new EditReviewsTable();
        String reviewjson = "{\"user_id\":\"2\",\"isbn\":\"9780439064873\","
                + "\"reviewText\":\"Vivlio me poiotita!!!\","
                + "\"reviewScore\":\"5\"}";

        ett.addReviewFromJSON(reviewjson);

        EditAdminMessageTable eat = new EditAdminMessageTable();
        String messagejson = "{\"message\":\"Hello to everyone\",\"date\":\"2022-10-18\"}";

        eat.addAdminMessageFromJSON(messagejson);
    }

    public void databaseToJSON() throws ClassNotFoundException, SQLException {
        //Users
        EditStudentsTable eut = new EditStudentsTable();
        Student su = eut.databaseToStudent("mountanton", "ab$12345");
        String json = eut.studentToJSON(su);
        System.out.println("Student\n" + json + "\n");

        //All the libraries
        EditLibrarianTable elt = new EditLibrarianTable();
        ArrayList<Librarian> libs = elt.databaseToLibrarians();
        Gson gson1 = new Gson();
        JsonArray jsonlibs = gson1.toJsonTree(libs).getAsJsonArray();
        System.out.println("Librarians\n" + jsonlibs + "\n");

        //All the Books
        EditBooksTable edt = new EditBooksTable();
        ArrayList<Book> books = edt.databaseToBooks();
        Gson gson2 = new Gson();
        JsonArray jsonDoc = gson2.toJsonTree(books).getAsJsonArray();
        System.out.println("Books\n" + jsonDoc + "\n");

        //All the Books of a given genre
        ArrayList<Book> books2 = edt.databaseToBooks("Fantasy");
        Gson gson3 = new Gson();
        JsonArray jsonDoc2 = gson3.toJsonTree(books2).getAsJsonArray();
        System.out.println("Books of Fantasy\n" + jsonDoc2 + "\n");

        //All the Books of a given library
        GeneralQueries general = new GeneralQueries();
        JsonArray allBooks = general.allBooksOfALibrary(3);
        System.out.println("All Books of Library 3 \n" + allBooks + "\n");

        //All the libraries having a book
        ArrayList<Librarian> libs2 = general.allLibrariesHavingABookAvailable("9780439064873");
        Gson gson4 = new Gson();
        JsonArray jsonlibs2 = gson1.toJsonTree(libs2).getAsJsonArray();
        System.out.println("Librarians\n" + jsonlibs2 + "\n");

        //All the borrowings of a given library
        JsonArray ja = general.allBorrowingsOfALibrary(1);
        System.out.println("All borrowings of a library with ID 1:\n" + ja+ "\n");

        //All the open borrowings of a given student
        JsonArray ja2=general.allOpenBorrowingsOfAStudent(1);
        System.out.println("All Open borrowings of student with ID 1:\n"+ja2+ "\n");
        
         //All the ended borrowings of a given student
        JsonArray ja3=general.allEndedBorrowingsOfAStudent(2);
        System.out.println("All Ended borrowings of student with ID 2:\n"+ja3+ "\n");
        

        //ALL THE REVIEWS OF A BOOK
        EditReviewsTable ertab = new EditReviewsTable();
        ArrayList<Review> revs = ertab.databaseToReviews("9780439064873");
        Gson gson5 = new Gson();
        JsonArray jsonrevs = gson1.toJsonTree(revs).getAsJsonArray();
        System.out.println("Reviews\n" + jsonrevs + "\n");

        //All the books of a Library.
    }

    public void updateRecords() throws ClassNotFoundException, SQLException {
        EditStudentsTable es = new EditStudentsTable();
        es.updateStudent("mountanton", "http://users.ics.forth/mountant");

    }

    public void deleteRecords() throws ClassNotFoundException, SQLException {

        EditBooksTable eb = new EditBooksTable();
        String isbn = "";
        //eb.deleteBook("");
    }

    public void addBookToLibraries() throws ClassNotFoundException {
        EditBooksInLibraryTable ebl = new EditBooksInLibraryTable();
        String addBook1Lib1 = "{\"isbn\":\"9780064471046\",\"library_id\":\"1\",\"available\":\"true\"}";
        String addBook1Lib2 = "{\"isbn\":\"9780064471046\",\"library_id\":\"2\",\"available\":\"true\"}";
        String addBook1Lib3 = "{\"isbn\":\"9780064471046\",\"library_id\":\"3\",\"available\":\"true\"}";
        String addBook1Lib4 = "{\"isbn\":\"9780064471046\",\"library_id\":\"4\",\"available\":\"true\"}";
        String addBook1Lib5 = "{\"isbn\":\"9780064471046\",\"library_id\":\"5\",\"available\":\"true\"}";

        ebl.addBookInLibraryFromJSON(addBook1Lib1);
        ebl.addBookInLibraryFromJSON(addBook1Lib2);
        ebl.addBookInLibraryFromJSON(addBook1Lib3);
        ebl.addBookInLibraryFromJSON(addBook1Lib4);
        ebl.addBookInLibraryFromJSON(addBook1Lib5);

        String addBook2Lib1 = "{\"isbn\":\"9781606801482\",\"library_id\":\"1\",\"available\":\"true\"}";
        String addBook2Lib3 = "{\"isbn\":\"9781606801482\",\"library_id\":\"3\",\"available\":\"true\"}";
        String addBook2Lib5 = "{\"isbn\":\"9781606801482\",\"library_id\":\"5\",\"available\":\"true\"}";

        ebl.addBookInLibraryFromJSON(addBook2Lib1);
        ebl.addBookInLibraryFromJSON(addBook2Lib3);
        ebl.addBookInLibraryFromJSON(addBook2Lib5);

        String addBook3Lib2 = "{\"isbn\":\"9780439064873\",\"library_id\":\"2\",\"available\":\"true\"}";
        String addBook3Lib4 = "{\"isbn\":\"9780439064873\",\"library_id\":\"4\",\"available\":\"true\"}";
        String addBook3Lib5 = "{\"isbn\":\"9780439064873\",\"library_id\":\"5\",\"available\":\"true\"}";

        ebl.addBookInLibraryFromJSON(addBook3Lib2);
        ebl.addBookInLibraryFromJSON(addBook3Lib4);
        ebl.addBookInLibraryFromJSON(addBook3Lib5);

        String addBook4Lib1 = "{\"isbn\":\"9780439136365\",\"library_id\":\"1\",\"available\":\"true\"}";
        String addBook4Lib2 = "{\"isbn\":\"9780439136365\",\"library_id\":\"2\",\"available\":\"true\"}";

        ebl.addBookInLibraryFromJSON(addBook4Lib1);
        ebl.addBookInLibraryFromJSON(addBook4Lib2);

        String addBook5Lib1 = "{\"isbn\":\"9780439139601\",\"library_id\":\"1\",\"available\":\"true\"}";
        String addBook5Lib5 = "{\"isbn\":\"9780439139601\",\"library_id\":\"5\",\"available\":\"true\"}";

        ebl.addBookInLibraryFromJSON(addBook5Lib1);
        ebl.addBookInLibraryFromJSON(addBook5Lib5);

        String addBook6Lib4 = "{\"isbn\":\"9781451681758\",\"library_id\":\"4\",\"available\":\"true\"}";

        ebl.addBookInLibraryFromJSON(addBook6Lib4);

        String addBook7Lib1 = "{\"isbn\":\"9780743273565\",\"library_id\":\"1\",\"available\":\"true\"}";
        String addBook7Lib2 = "{\"isbn\":\"9780743273565\",\"library_id\":\"2\",\"available\":\"true\"}";
        String addBook7Lib3 = "{\"isbn\":\"9780743273565\",\"library_id\":\"3\",\"available\":\"true\"}";

        ebl.addBookInLibraryFromJSON(addBook7Lib1);
        ebl.addBookInLibraryFromJSON(addBook7Lib2);
        ebl.addBookInLibraryFromJSON(addBook7Lib3);

        String addBook8Lib4 = "{\"isbn\":\"9781538330456\",\"library_id\":\"4\",\"available\":\"true\"}";
        String addBook8Lib5 = "{\"isbn\":\"9781538330456\",\"library_id\":\"5\",\"available\":\"true\"}";

        ebl.addBookInLibraryFromJSON(addBook8Lib4);
        ebl.addBookInLibraryFromJSON(addBook8Lib5);

        String addBook9Lib2 = "{\"isbn\":\"9780446676090\",\"library_id\":\"2\",\"available\":\"true\"}";
        String addBook9Lib3 = "{\"isbn\":\"9780446676090\",\"library_id\":\"3\",\"available\":\"true\"}";

        ebl.addBookInLibraryFromJSON(addBook9Lib2);
        ebl.addBookInLibraryFromJSON(addBook9Lib3);

        String addBook10Lib3 = "{\"isbn\":\"9780451457813\",\"library_id\":\"3\",\"available\":\"true\"}";
        ebl.addBookInLibraryFromJSON(addBook10Lib3);

    }
}
