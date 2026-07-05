package healthcare.doctor;
// Demonstrates: INHERITANCE
public class GeneralDoctor extends Doctor {

    public GeneralDoctor(String doctorId, String name, String email, String password, String phone, String specialization) {
        super(doctorId, name, email, password, phone, specialization, "General");
    }

    // A method specific to General Doctors
    public String getConsultationNote() {
        return "Dr. " + getName() + " handles general health consultations.";
    }
}
