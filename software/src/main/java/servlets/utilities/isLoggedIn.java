package servlets.utilities;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "isLoggedIn", value = "/isLoggedIn")
public class isLoggedIn extends HttpServlet {

    /**This function is used to check if the user is already logged in*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        if(session.getAttribute("loggedIn") != null) {  //if there is an object with the name "loggedIn" bound to the session (if the user is has logged-in)
            response.setStatus(200);
            JsonObject obj = new JsonObject();
            obj.addProperty("loggedIn", session.getAttribute("loggedIn").toString());  //return user name
            obj.addProperty("user_type",session.getAttribute("user_type").toString()); //and user type
            response.getWriter().write(obj.toString());
        }
        else
            response.setStatus(401);
    }
}
