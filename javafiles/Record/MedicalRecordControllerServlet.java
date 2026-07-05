package healthcare.records;

import healthcare.records.MedicalRecord;
import healthcare.admin.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/record/*")
public class MedicalRecordControllerServlet extends HttpServlet {

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
                    request.getRequestDispatcher("/record-register.jsp").forward(request, response);
                    break;
                case "/edit":
                    String id = request.getParameter("id");
                    request.setAttribute("record", fileHandler.getAllMedicalRecords().stream().filter(r -> r.getRecordId().equals(id)).findFirst().orElse(null));
                    request.getRequestDispatcher("/record-update.jsp").forward(request, response);
                    break;
                case "/delete":
                    fileHandler.deleteMedicalRecord(request.getParameter("id"));
                    response.sendRedirect(request.getContextPath() + "/record/list");
                    break;
                case "/search":
                    String keyword = request.getParameter("keyword");
                    List<MedicalRecord> listRecord = fileHandler.getAllMedicalRecords();
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        listRecord = listRecord.stream()
                            .filter(r -> r.getPatientId().toLowerCase().contains(keyword.toLowerCase()) || 
                                         r.getDoctorId().toLowerCase().contains(keyword.toLowerCase()))
                            .collect(Collectors.toList());
                    }
                    request.setAttribute("listRecord", listRecord);
                    request.setAttribute("keyword", keyword);
                    request.getRequestDispatcher("/record-dashboard.jsp").forward(request, response);
                    break;
                case "/list":
                default:
                    request.setAttribute("listRecord", fileHandler.getAllMedicalRecords());
                    request.getRequestDispatcher("/record-dashboard.jsp").forward(request, response);
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
                    String id = "R" + String.format("%03d", fileHandler.getAllMedicalRecords().size() + 1);
                    String type = request.getParameter("recordType");
                    MedicalRecord newRecord = "DiagnosisReport".equalsIgnoreCase(type) ? 
                        new healthcare.records.DiagnosisReport(id, request.getParameter("patientId"), request.getParameter("doctorId"), request.getParameter("diagnosis"), request.getParameter("prescription"), request.getParameter("visitDate")) : 
                        new healthcare.records.Prescription(id, request.getParameter("patientId"), request.getParameter("doctorId"), request.getParameter("diagnosis"), request.getParameter("prescription"), request.getParameter("visitDate"));
                    fileHandler.saveMedicalRecord(newRecord);
                    response.sendRedirect(request.getContextPath() + "/record/list");
                    break;
                case "/update":
                    String uId = request.getParameter("recordId");
                    String uType = request.getParameter("recordType");
                    MedicalRecord uRecord = "DiagnosisReport".equalsIgnoreCase(uType) ? 
                        new healthcare.records.DiagnosisReport(uId, request.getParameter("patientId"), request.getParameter("doctorId"), request.getParameter("diagnosis"), request.getParameter("prescription"), request.getParameter("visitDate")) : 
                        new healthcare.records.Prescription(uId, request.getParameter("patientId"), request.getParameter("doctorId"), request.getParameter("diagnosis"), request.getParameter("prescription"), request.getParameter("visitDate"));
                    fileHandler.updateMedicalRecord(uId, uRecord);
                    response.sendRedirect(request.getContextPath() + "/record/list");
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
