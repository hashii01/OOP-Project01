package healthcare.patient;
// Demonstrates: INHERITANCE
public class PremiumPatient extends Patient {

    private String vipCode; // Extra field only Premium patients have

    // Constructor
    public PremiumPatient(String patientId, String name, String email, String phone, String password) {
        super(patientId, name, email, phone, password, "Premium");
        this.vipCode = "VIP-" + patientId;
    }

    // Getter for VIP code
    public String getVipCode() { return vipCode; }

    // A method specific to Premium Patients
    public String getServiceLevel() {
        return "Priority appointments + VIP Lounge access. Code: " + vipCode;
    }

    @Override
    public String getMembershipBenefits() {
        return "20% discount on billing and Priority Queue.";
    }
}
