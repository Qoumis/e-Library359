package servlets.Books;

import database.tables.EditBooksTable;
import mainClasses.Book;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "GetBookList", value = "/GetBookList")
public class GetBookList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditBooksTable ebt = new EditBooksTable();
        ArrayList<Book> books = new ArrayList<Book>();
        try {
            /**Get all the books from db*/
            books = ebt.databaseToBooks();
            String json = ebt.bookToJSON(books.get(0));
            /**Convert to json*/
            for(int i = 1 ; i < books.size(); i++)
                json += "," + ebt.bookToJSON(books.get(i));

            response.setStatus(200);
            response.getWriter().write(json);               //return all
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(200);
        response.getWriter().write("test");
    }
}
