package servlets.Student;

import database.DB_Connection;
import database.tables.EditReviewsTable;
import mainClasses.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static servlets.Student.BorrowActions.findUserID;

@WebServlet(name = "PostReview", value = "/PostReview")
public class PostReview extends HttpServlet {

    /**This function is used to add a new entry to the review table (add students review)*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = session.getAttribute("loggedIn").toString();

        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            int user_id = findUserID(username);

            Review rev = new Review();
            rev.setIsbn(request.getParameter("isbn"));
            rev.setReviewScore(request.getParameter("reviewScore"));
            rev.setReviewText(request.getParameter("reviewText"));
            rev.setUser_id(user_id);

            EditReviewsTable ert = new EditReviewsTable();
            ert.createNewReview(rev);
            stmt.executeUpdate("UPDATE students SET borrow_count = borrow_count + 1 WHERE username = '" + username + "'");
            response.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
        }

    }
}
