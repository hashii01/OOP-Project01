package healthcare.billing;

public class InsurancePayment extends Billing {
    public InsurancePayment(String billId, String patientId, String appointmentId,
                            double amount, String paymentStatus, String billDate) {
        super(billId, patientId, appointmentId, amount, paymentStatus, billDate, "InsurancePayment");
    }

    @Override
    public double calculateFinalAmount() {
        // Insurance covers 80%
        return getAmount() * 0.20;
    }
}
