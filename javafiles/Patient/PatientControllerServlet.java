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
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/patient/*")
public class PatientControllerServlet extends HttpServlet {

    private FileHandler fileHandler;

    @Override
    public void init() throws ServletException {
        fileHandler = new FileHandler();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/delete":
                    deletePatient(request, response);
                    break;
                case "/search":
                    searchPatient(request, response);
                    break;
                case "/list":
                default:
                    listPatients(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";

        try {
            switch (action) {
                case "/insert":
                    insertPatient(request, response);
                    break;
                case "/update":
                    updatePatient(request, response);
                    break;
                default:
                    listPatients(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void listPatients(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Patient> listPatient = fileHandler.getAllPatients();
        request.setAttribute("listPatient", listPatient);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    private void searchPatient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Patient> listPatient = fileHandler.getAllPatients();
        if (keyword != null && !keyword.trim().isEmpty()) {
            listPatient = listPatient.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()) || 
                             p.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                             p.getPatientId().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        }
        request.setAttribute("listPatient", listPatient);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Patient existingPatient = fileHandler.getPatientById(id);
        request.setAttribute("patient", existingPatient);
        request.getRequestDispatcher("/update.jsp").forward(request, response);
    }

    private void insertPatient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String type = request.getParameter("patientType");

        String patientId = "P" + String.format("%03d", fileHandler.getAllPatients().size() + 1);

        Patient newPatient;
        if ("Premium".equalsIgnoreCase(type)) {
            newPatient = new PremiumPatient(patientId, name, email, phone, password);
        } else {
            newPatient = new RegularPatient(patientId, name, email, phone, password);
        }

        fileHandler.savePatient(newPatient);
        response.sendRedirect(request.getContextPath() + "/patient/list");
    }

    private void updatePatient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("patientId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String type = request.getParameter("patientType");

        Patient patient;
        if ("Premium".equalsIgnoreCase(type)) {
            patient = new PremiumPatient(id, name, email, phone, password);
        } else {
            patient = new RegularPatient(id, name, email, phone, password);
        }

        fileHandler.updatePatient(id, patient);
        response.sendRedirect(request.getContextPath() + "/patient/list");
    }

    private void deletePatient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        fileHandler.deletePatient(id);
        response.sendRedirect(request.getContextPath() + "/patient/list");
    }
}
