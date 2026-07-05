package healthcare.billing;

import healthcare.billing.Billing;
import healthcare.admin.FileHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/billing/*")
public class BillingControllerServlet extends HttpServlet {

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
                    request.getRequestDispatcher("/billing-register.jsp").forward(request, response);
                    break;
                case "/edit":
                    String id = request.getParameter("id");
                    request.setAttribute("billing", fileHandler.getBillingById(id));
                    request.getRequestDispatcher("/billing-update.jsp").forward(request, response);
                    break;
                case "/delete":
                    fileHandler.deleteBilling(request.getParameter("id"));
                    response.sendRedirect(request.getContextPath() + "/billing/list");
                    break;
                case "/search":
                    String keyword = request.getParameter("keyword");
                    List<Billing> listBilling = fileHandler.getAllBillings();
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        listBilling = listBilling.stream()
                            .filter(b -> b.getPatientId().toLowerCase().contains(keyword.toLowerCase()))
                            .collect(Collectors.toList());
                    }
                    request.setAttribute("listBilling", listBilling);
                    request.setAttribute("keyword", keyword);
                    request.getRequestDispatcher("/billing-dashboard.jsp").forward(request, response);
                    break;
                case "/list":
                default:
                    request.setAttribute("listBilling", fileHandler.getAllBillings());
                    request.getRequestDispatcher("/billing-dashboard.jsp").forward(request, response);
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
                    String id = "B" + String.format("%03d", fileHandler.getAllBillings().size() + 1);
                    double amount = Double.parseDouble(request.getParameter("amount"));
                    String method = request.getParameter("paymentMethod");
                    Billing newBill = "InsurancePayment".equalsIgnoreCase(method) ? 
                        new healthcare.billing.InsurancePayment(id, request.getParameter("patientId"), request.getParameter("appointmentId"), amount, request.getParameter("paymentStatus"), request.getParameter("billDate")) : 
                        new healthcare.billing.DirectPayment(id, request.getParameter("patientId"), request.getParameter("appointmentId"), amount, request.getParameter("paymentStatus"), request.getParameter("billDate"));
                    fileHandler.saveBilling(newBill);
                    response.sendRedirect(request.getContextPath() + "/billing/list");
                    break;
                case "/update":
                    String uId = request.getParameter("billId");
                    double uAmount = Double.parseDouble(request.getParameter("amount"));
                    String uMethod = request.getParameter("paymentMethod");
                    Billing uBill = "InsurancePayment".equalsIgnoreCase(uMethod) ? 
                        new healthcare.billing.InsurancePayment(uId, request.getParameter("patientId"), request.getParameter("appointmentId"), uAmount, request.getParameter("paymentStatus"), request.getParameter("billDate")) : 
                        new healthcare.billing.DirectPayment(uId, request.getParameter("patientId"), request.getParameter("appointmentId"), uAmount, request.getParameter("paymentStatus"), request.getParameter("billDate"));
                    fileHandler.updateBilling(uId, uBill);
                    response.sendRedirect(request.getContextPath() + "/billing/list");
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
