package healthcare.patient;

import healthcare.patient.Patient;
import healthcare.patient.PremiumPatient;
import healthcare.patient.RegularPatient;
import healthcare.admin.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
// Handles PUBLIC patient self-registration.
// NO login required to reach this page.
@WebServlet("/register")
public class PatientRegisterServlet extends HttpServlet {

    private FileHandler fileHandler;

    @Override
    public void init() throws ServletException {
        fileHandler = new FileHandler();
    }

    // GET /register → show the registration form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/patient-self-register.jsp").forward(request, response);
    }

    // POST /register → save new patient
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name     = request.getParameter("name")     != null ? request.getParameter("name").trim()     : "";
        String email    = request.getParameter("email")    != null ? request.getParameter("email").trim()    : "";
        String phone    = request.getParameter("phone")    != null ? request.getParameter("phone").trim()    : "";
        String password = request.getParameter("password") != null ? request.getParameter("password").trim() : "";
        String confirm  = request.getParameter("confirm")  != null ? request.getParameter("confirm").trim()  : "";

        // --- Basic server-side validation ---
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/patient-self-register.jsp").forward(request, response);
            return;
        }
        if (!password.equals(confirm)) {
            request.setAttribute("error", "Passwords do not match. Please try again.");
            request.setAttribute("nameVal",  name);
            request.setAttribute("emailVal", email);
            request.setAttribute("phoneVal", phone);
            request.getRequestDispatcher("/patient-self-register.jsp").forward(request, response);
            return;
        }
        // Check if email is already taken
        if (fileHandler.getPatientByEmail(email) != null) {
            request.setAttribute("error", "An account with this email already exists. Please log in instead.");
            request.setAttribute("emailVal", email);
            request.getRequestDispatcher("/patient-self-register.jsp").forward(request, response);
            return;
        }

        // Auto-generate patient ID
        String patientId = "P" + String.format("%03d", fileHandler.getAllPatients().size() + 1);

        // All new self-registered patients start as Regular
        Patient newPatient = new RegularPatient(patientId, name, email, phone, password);
        fileHandler.savePatient(newPatient);

        // Redirect to login with success message
        response.sendRedirect(request.getContextPath() + "/login?registered=true");
    }
}
