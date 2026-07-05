package healthcare.billing;

public class DirectPayment extends Billing {
    public DirectPayment(String billId, String patientId, String appointmentId,
                         double amount, String paymentStatus, String billDate) {
        super(billId, patientId, appointmentId, amount, paymentStatus, billDate, "DirectPayment");
    }

    @Override
    public double calculateFinalAmount() {
        // Direct pay has no discount
        return getAmount();
    }
}
