package servlets.Books;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.DB_Connection;
import database.tables.EditBooksTable;
import database.tables.EditReviewsTable;
import mainClasses.Book;
import mainClasses.Review;

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
import java.util.ArrayList;

@WebServlet(name = "GetReviews", value = "/GetReviews")
public class GetReviews extends HttpServlet {

    /**This function is used to get all the reviews for a book from the database*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String isbn = request.getParameter("isbn");

        EditReviewsTable ert = new EditReviewsTable();
        ArrayList<Review> reviews = new ArrayList<Review>();

        try {
            reviews = ert.databaseToReviews(isbn);  //get reviews from the DB
            if(reviews.size() > 0){  //if there are any reviews
                JsonArray  jsArr = new JsonArray();
                for(int i = 0; i <reviews.size(); i++){ //for each review
                    String name = getStudentName(reviews.get(i).getUser_id());
                    JsonObject jsObj = new JsonObject();
                    jsObj.addProperty("Reviewer's name", name);
                    jsObj.addProperty("Description", reviews.get(i).getReviewText());
                    jsObj.addProperty("Score", reviews.get(i).getReviewScore());

                    jsArr.add(jsObj);
                }
                response.setStatus(200);
                response.getWriter().write(jsArr.toString());
            }
            else                  //if there are no reviews for that book
                response.setStatus(204);
        } catch (SQLException e) {
            response.setStatus(500);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            response.setStatus(500);
            e.printStackTrace();
        }
    }

    /**This function is used to get a student's first and last name from the database, given their id*/
    public String getStudentName(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        JsonObject obj = new JsonObject();
        rs = stmt.executeQuery("SELECT firstname, lastname FROM students WHERE user_id='"+id+"'");
        while(rs.next()){
          obj = DB_Connection.getResultsToJSONObject(rs);
        }
        return obj.get("firstname").getAsString() + " " + obj.get("lastname").getAsString();
    }
}