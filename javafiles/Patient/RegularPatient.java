package healthcare.patient;
// Demonstrates: INHERITANCE
public class RegularPatient extends Patient {

    // Constructor - passes values up to the parent using super()
    public RegularPatient(String patientId, String name, String email, String phone, String password) {
        super(patientId, name, email, phone, password, "Regular");
    }

    // A method specific to Regular Patients
    public String getServiceLevel() {
        return "Standard waiting room and queue scheduling.";
    }

    @Override
    public String getMembershipBenefits() {
        return "Standard Access.";
    }
}
