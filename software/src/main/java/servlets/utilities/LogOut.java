package servlets.utilities;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LogOut", value = "/LogOut")
public class LogOut extends HttpServlet {

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session=request.getSession();
        if(session.getAttribute("loggedIn") != null){  //just invalidate the session
            session.invalidate();

            response.setStatus(200);
        }
        else
            response.setStatus(403);
    }
}
