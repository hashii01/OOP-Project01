package healthcare.doctor;

import healthcare.doctor.Doctor;
import healthcare.doctor.GeneralDoctor;
import healthcare.doctor.SpecialistDoctor;
import healthcare.admin.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/doctor/*")
public class DoctorControllerServlet extends HttpServlet {

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
                    request.getRequestDispatcher("/doctor-register.jsp").forward(request, response);
                    break;
                case "/edit":
                    String id = request.getParameter("id");
                    request.setAttribute("doctor", fileHandler.getDoctorById(id));
                    request.getRequestDispatcher("/doctor-update.jsp").forward(request, response);
                    break;
                case "/delete":
                    fileHandler.deleteDoctor(request.getParameter("id"));
                    response.sendRedirect(request.getContextPath() + "/doctor/list");
                    break;
                case "/search":
                    String keyword = request.getParameter("keyword");
                    List<Doctor> listDoctor = fileHandler.getAllDoctors();
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        listDoctor = listDoctor.stream()
                            .filter(d -> d.getName().toLowerCase().contains(keyword.toLowerCase()) || 
                                         d.getSpecialization().toLowerCase().contains(keyword.toLowerCase()))
                            .collect(Collectors.toList());
                    }
                    request.setAttribute("listDoctor", listDoctor);
                    request.setAttribute("keyword", keyword);
                    request.getRequestDispatcher("/doctor-dashboard.jsp").forward(request, response);
                    break;
                case "/list":
                default:
                    request.setAttribute("listDoctor", fileHandler.getAllDoctors());
                    request.getRequestDispatcher("/doctor-dashboard.jsp").forward(request, response);
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
                    String doctorId = "D" + String.format("%03d", fileHandler.getAllDoctors().size() + 1);
                    String type = request.getParameter("doctorType");
                    String pass = request.getParameter("password");
                    Doctor newDoctor = "Specialist".equalsIgnoreCase(type) ?
                        new SpecialistDoctor(doctorId, request.getParameter("name"), request.getParameter("email"), pass, request.getParameter("phone"), request.getParameter("specialization")) :
                        new GeneralDoctor(doctorId, request.getParameter("name"), request.getParameter("email"), pass, request.getParameter("phone"), request.getParameter("specialization"));
                    fileHandler.saveDoctor(newDoctor);
                    response.sendRedirect(request.getContextPath() + "/doctor/list");
                    break;
                case "/update":
                    String uId = request.getParameter("doctorId");
                    String uType = request.getParameter("doctorType");
                    String uPass = request.getParameter("password");
                    Doctor uDoctor = "Specialist".equalsIgnoreCase(uType) ?
                        new SpecialistDoctor(uId, request.getParameter("name"), request.getParameter("email"), uPass, request.getParameter("phone"), request.getParameter("specialization")) :
                        new GeneralDoctor(uId, request.getParameter("name"), request.getParameter("email"), uPass, request.getParameter("phone"), request.getParameter("specialization"));
                    fileHandler.updateDoctor(uId, uDoctor);
                    response.sendRedirect(request.getContextPath() + "/doctor/list");
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
