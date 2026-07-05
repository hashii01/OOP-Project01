package healthcare.admin;

import healthcare.admin.LoggedInUser;
import healthcare.admin.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
// Handles GET  → Show the login page
// Handles POST → Check credentials, create session, redirect
@WebServlet("/login")
public class LoginControllerServlet extends HttpServlet {

    // AuthService does the actual file-reading work
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthService();
    }

    // --- GET /login → Just show the login page ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // If someone is already logged in, send them to the dashboard
        HttpSession existing = request.getSession(false);
        if (existing != null && existing.getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // --- POST /login → Verify credentials ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Read form fields
        String email    = request.getParameter("email")    != null ? request.getParameter("email").trim()    : "";
        String password = request.getParameter("password") != null ? request.getParameter("password").trim() : "";
        String role     = request.getParameter("role")     != null ? request.getParameter("role").trim()     : "PATIENT";

        // 2. Ask AuthService to check the specific text file for the selected role
        LoggedInUser user = authService.login(email, password, role);

        if (user != null) {
            // 3. Login SUCCESS → store the user in the session
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);   // the LoggedInUser object
            session.setAttribute("userRole",    user.getRole()); // "ADMIN", "DOCTOR", or "PATIENT"
            session.setAttribute("userName",    user.getName()); // Friendly name for navbar

            // 4. Send them to the main dashboard
            response.sendRedirect(request.getContextPath() + "/");

        } else {
            // 3. Login FAILED → show error on login page
            request.setAttribute("error", "Incorrect email or password. Please try again.");
            request.setAttribute("emailTyped", email); // Keep their email in the box
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
