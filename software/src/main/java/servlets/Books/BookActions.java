package servlets.Books;

import database.tables.EditBooksTable;
import mainClasses.Book;
import mainClasses.CheckDB;
import mainClasses.JSON_Converter;
import mainClasses.filterInput;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "BookActions", value = "/BookActions")
public class BookActions extends HttpServlet {

    /**This function is used to get all the books with the specified genre from the database*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int fromYear = 0 , toYear = 0, fromPage = 0, toPage = 0;
        if(request.getParameter("fromYear") != ""){           //Check optional parameter fromYear
            fromYear = Integer.parseInt(request.getParameter("fromYear"));
            if(fromYear < 1200){
                response.setStatus(406);
                response.getWriter().write("Publication year must be 1200 or later");
                return;
            }
        }
        if(request.getParameter("toYear") != ""){         //Check optional parameter toYear
            toYear = Integer.parseInt(request.getParameter("toYear"));
            if(toYear < 1200){
                response.setStatus(406);
                response.getWriter().write("Publication year must be 1200 or later");
                return;
            }
        }
        if(fromYear != 0 && toYear != 0) { //(if given)
            if(fromYear > toYear){         // check that fromYear is less than toYear
                response.setStatus(406);
                response.getWriter().write("From year cannot exceed To year");
                return;
            }
        }
        // check that fromYear is less than toYear
        if(request.getParameter("fromPage") != "")
            fromPage = Integer.parseInt(request.getParameter("fromPage"));
        if(request.getParameter("toPage") != "")
            toPage = Integer.parseInt(request.getParameter("toPage"));
        if(fromPage != 0 && toPage != 0) { //(if both are given)
            if(fromPage > toPage){         // check that fromPage is less than toPage
                response.setStatus(406);
                response.getWriter().write("From page Number cannot exceed To page Number");
                return;
            }
        }

        /**all good lets return the books*/
        CheckDB cdb = new CheckDB();
        EditBooksTable ebt = new EditBooksTable();
        ArrayList<Book> books = new ArrayList<Book>();
        try {
            /**Get the books from db*/
            books = cdb.databaseToBooksOfGenre(request.getParameter("genre"), request.getParameter("fromYear"),
                request.getParameter("toYear"), request.getParameter("title"), request.getParameter("authors"),
                    request.getParameter("fromPage"), request.getParameter("toPage") );

            if(books.size() > 0){
                String json = ebt.bookToJSON(books.get(0));
                /**Convert to json*/
                for(int i = 1 ; i < books.size(); i++)
                    json+= "," + ebt.bookToJSON(books.get(i));
                response.setStatus(200);
                response.getWriter().write(json);
            }
            else{
                response.setStatus(404);
                response.getWriter().write("No book with the specified filters could be found in the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**This function is used to add a new book to the database*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /**Get HttpServletRequest body and convert it to String*/
        JSON_Converter jc = new JSON_Converter();
        String JsonString = jc.getJSONFromAjax(request.getReader());
        JsonString = filterInput.filter(JsonString);    //filter user's input
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        /**Check User's input on server side*/
        EditBooksTable ebt = new EditBooksTable();
        Book newBook = ebt.jsonToBook(JsonString);

        String isbn = newBook.getIsbn();
        if(isbn.length() < 10 || isbn.length() > 13){  //Check isbn length
            response.setStatus(403);
            response.getWriter().write("ISBN should be between 10 and 13 digits");
            return;
        }
        /**Check if a book with that isbn already exists */
        CheckDB cdb = new CheckDB();
        boolean already_exists = false;
        try {
            already_exists = cdb.checkDBforISBN(isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(already_exists){
            response.setStatus(409);
            response.getWriter().write("A book with that ISBN already exists");
            return;
        }

        if(newBook.getPages() <= 0){                 //Check pages
            response.setStatus(406);
            response.getWriter().write("Number of pages should be greater than 0");
            return;
        }
        if(newBook.getPublicationyear() < 1200){                 //Check year
            response.setStatus(406);
            response.getWriter().write("Publication year must be 1200 or later");
            return;
        }

        /**all good Add book to database*/
        try {
            ebt.addBookFromJSON(JsonString);
            response.setStatus(200);
            response.getWriter().write(JsonString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**This function is used to update a books pages number to the database*/
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**Get HttpServletRequest body and convert it to String*/
        JSON_Converter jc = new JSON_Converter();
        String JsonString = jc.getJSONFromAjax(request.getReader());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        EditBooksTable ebt = new EditBooksTable();
        Book newBook = ebt.jsonToBook(JsonString);

        /**Check if a book with that isbn exists */
        CheckDB cdb = new CheckDB();
        boolean book_exists = false;
        try {
            book_exists = cdb.checkDBforISBN(newBook.getIsbn());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(!book_exists){
            response.setStatus(404);
            response.getWriter().write("That ISBN does not match an existing book");
        }
        else if(newBook.getPages() <= 0){                 //Check pages
            response.setStatus(406);
            response.getWriter().write("Number of pages should be greater than 0");
        }
        else{           /**All good update pages*/
            try {
                cdb.updateBookPages(newBook.getIsbn(), Integer.toString(newBook.getPages()));
                response.setStatus(200);
                response.getWriter().write(newBook.getIsbn());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**This function is used to delete a book from the database*/
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**Get isbn*/
        String isbn = request.getParameter("isbn");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        /**Check if a book with that isbn exists */
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
        else{                 //Book exists delete it
            try {
                EditBooksTable ebt = new EditBooksTable();
                ebt.deleteBook(isbn);
                response.setStatus(200);
                response.getWriter().write(isbn);
            } catch (SQLException e) {
                response.setStatus(500);
                response.getWriter().write(e.toString());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
