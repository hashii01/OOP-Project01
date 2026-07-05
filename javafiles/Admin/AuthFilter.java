package healthcare.admin;

import healthcare.admin.LoggedInUser;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
// Intercepts EVERY request and checks:
//   - Is the user logged in?  → Let them through
//   - Are they on the login page? → Let them through
//   - Otherwise? → Redirect to /login
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void destroy() { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Always allow: static files, login page, logout page, and patient self-registration
        boolean isPublic = path.startsWith("/css/")
                        || path.startsWith("/js/")
                        || path.startsWith("/images/")
                        || path.equals("/login")
                        || path.equals("/logout")
                        || path.equals("/register")
                        || path.endsWith("login.jsp")
                        || path.endsWith("patient-self-register.jsp");

        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

        // Check session for a logged-in user
        HttpSession session = req.getSession(false);
        LoggedInUser user   = (session != null)
                ? (LoggedInUser) session.getAttribute("currentUser")
                : null;

        if (user != null) {
            // --- Role-Based Access Control ---
            // Patients cannot access admin, billing, or doctor pages
            if (user.isPatient()) {
                boolean forbidden = path.startsWith("/admin")
                                 || path.startsWith("/billing")
                                 || path.startsWith("/doctor");
                if (forbidden) {
                    res.sendRedirect(req.getContextPath() + "/?error=AccessDenied");
                    return;
                }
            }

            // Doctors cannot access admin, billing, or patient pages
            if (user.isDoctor()) {
                boolean forbidden = path.startsWith("/admin")
                                 || path.startsWith("/billing")
                                 || path.startsWith("/patient");
                if (forbidden) {
                    res.sendRedirect(req.getContextPath() + "/?error=AccessDenied");
                    return;
                }
            }

            chain.doFilter(request, response);
        } else {
            // Not logged in → go to login page
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
