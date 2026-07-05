package healthcare.appointment;

import healthcare.appointment.Appointment;
import healthcare.admin.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/appointment/*")
public class AppointmentControllerServlet extends HttpServlet {

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
                    request.getRequestDispatcher("/appointment-register.jsp").forward(request, response);
                    break;
                case "/edit":
                    String id = request.getParameter("id");
                    request.setAttribute("appointment", fileHandler.getAppointmentById(id));
                    request.getRequestDispatcher("/appointment-update.jsp").forward(request, response);
                    break;
                case "/delete":
                    fileHandler.deleteAppointment(request.getParameter("id"));
                    response.sendRedirect(request.getContextPath() + "/appointment/list");
                    break;
                case "/search":
                    String keyword = request.getParameter("keyword");
                    List<Appointment> listAppointment = fileHandler.getAllAppointments();
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        listAppointment = listAppointment.stream()
                            .filter(a -> a.getPatientId().toLowerCase().contains(keyword.toLowerCase()) || 
                                         a.getDoctorId().toLowerCase().contains(keyword.toLowerCase()))
                            .collect(Collectors.toList());
                    }
                    request.setAttribute("listAppointment", listAppointment);
                    request.setAttribute("keyword", keyword);
                    request.getRequestDispatcher("/appointment-dashboard.jsp").forward(request, response);
                    break;
                case "/list":
                default:
                    request.setAttribute("listAppointment", fileHandler.getAllAppointments());
                    request.getRequestDispatcher("/appointment-dashboard.jsp").forward(request, response);
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
                    String doctorId = request.getParameter("doctorId");
                    healthcare.doctor.Doctor doctor = fileHandler.getDoctorById(doctorId);
                    if (doctor == null) {
                        response.sendRedirect(request.getContextPath() + "/appointment/new?error=DoctorNotFound");
                        return;
                    }
                    String date = request.getParameter("date");
                    if (java.time.LocalDate.parse(date).isBefore(java.time.LocalDate.now())) {
                        response.sendRedirect(request.getContextPath() + "/appointment/new?error=PastDateNotAllowed");
                        return;
                    }
                    if (!doctor.isAvailable(date)) {
                        response.sendRedirect(request.getContextPath() + "/appointment/new?error=DoctorNotAvailable");
                        return;
                    }
                    String id = "A" + String.format("%03d", fileHandler.getAllAppointments().size() + 1);
                    String type = request.getParameter("appointmentType");
                    Appointment newAppt = "Emergency".equalsIgnoreCase(type) ? 
                        new healthcare.appointment.EmergencyAppointment(id, request.getParameter("patientId"), doctorId, date, request.getParameter("time"), request.getParameter("status"), request.getParameter("reason")) : 
                        new healthcare.appointment.RegularAppointment(id, request.getParameter("patientId"), doctorId, date, request.getParameter("time"), request.getParameter("status"), request.getParameter("reason"));
                    fileHandler.saveAppointment(newAppt);
                    response.sendRedirect(request.getContextPath() + "/appointment/list");
                    break;
                case "/update":
                    String uDoctorId = request.getParameter("doctorId");
                    healthcare.doctor.Doctor uDoctor = fileHandler.getDoctorById(uDoctorId);
                    if (uDoctor == null) {
                        response.sendRedirect(request.getContextPath() + "/appointment/edit?id=" + request.getParameter("appointmentId") + "&error=DoctorNotFound");
                        return;
                    }
                    String uDate = request.getParameter("date");
                    if (java.time.LocalDate.parse(uDate).isBefore(java.time.LocalDate.now())) {
                        response.sendRedirect(request.getContextPath() + "/appointment/edit?id=" + request.getParameter("appointmentId") + "&error=PastDateNotAllowed");
                        return;
                    }
                    if (!uDoctor.isAvailable(uDate)) {
                        response.sendRedirect(request.getContextPath() + "/appointment/edit?id=" + request.getParameter("appointmentId") + "&error=DoctorNotAvailable");
                        return;
                    }
                    String uId = request.getParameter("appointmentId");
                    String uType = request.getParameter("appointmentType");
                    Appointment uAppt = "Emergency".equalsIgnoreCase(uType) ? 
                        new healthcare.appointment.EmergencyAppointment(uId, request.getParameter("patientId"), uDoctorId, uDate, request.getParameter("time"), request.getParameter("status"), request.getParameter("reason")) : 
                        new healthcare.appointment.RegularAppointment(uId, request.getParameter("patientId"), uDoctorId, uDate, request.getParameter("time"), request.getParameter("status"), request.getParameter("reason"));
                    fileHandler.updateAppointment(uId, uAppt);
                    response.sendRedirect(request.getContextPath() + "/appointment/list");
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
