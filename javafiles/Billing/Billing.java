package healthcare.billing;
public abstract class Billing {

    private String billId;
    private String patientId;
    private String appointmentId;
    private double amount;
    private String paymentStatus; // "Paid" or "Unpaid"
    private String billDate;
    private String paymentMethod; // "InsurancePayment" or "DirectPayment"

    // Constructor
    public Billing(String billId, String patientId, String appointmentId,
                   double amount, String paymentStatus, String billDate, String paymentMethod) {
        this.billId = billId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.billDate = billDate;
        this.paymentMethod = paymentMethod;
    }

    // ---- Getters ----
    public String getBillId()          { return billId; }
    public String getPatientId()       { return patientId; }
    public String getAppointmentId()   { return appointmentId; }
    public double getAmount()          { return amount; }
    public String getPaymentStatus()   { return paymentStatus; }
    public String getBillDate()        { return billDate; }
    public String getPaymentMethod()   { return paymentMethod; }

    // ---- Setters ----
    public void setBillId(String billId)               { this.billId = billId; }
    public void setPatientId(String patientId)         { this.patientId = patientId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public void setAmount(double amount)               { this.amount = amount; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setBillDate(String billDate)           { this.billDate = billDate; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    // Polymorphism: method to be overridden
    public abstract double calculateFinalAmount();

    // Format: billId,patientId,appointmentId,amount,paymentStatus,billDate,paymentMethod
    public String toFileString() {
        return billId + "," + patientId + "," + appointmentId + "," +
               amount + "," + paymentStatus + "," + billDate + "," + paymentMethod;
    }

    public static Billing fromFileString(String line) {
        if (line == null || line.trim().startsWith("#")) return null;
        String[] parts = line.split(",");
        if (parts.length < 7) {
            if (parts.length == 6) {
                return new DirectPayment(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), parts[4], parts[5]);
            }
            return null;
        }
        
        String method = parts[6].trim();
        if ("InsurancePayment".equalsIgnoreCase(method)) {
            return new InsurancePayment(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), parts[4], parts[5]);
        } else {
            return new DirectPayment(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), parts[4], parts[5]);
        }
    }
}
