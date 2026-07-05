package healthcare.doctor;
// Demonstrates: INHERITANCE
public class SpecialistDoctor extends Doctor {

    private String clinicRoom; // Extra field only Specialists have

    public SpecialistDoctor(String doctorId, String name, String email, String password, String phone, String specialization) {
        super(doctorId, name, email, password, phone, specialization, "Specialist");
        this.clinicRoom = "Room-" + doctorId;
    }

    public String getClinicRoom() { return clinicRoom; }

    // A method specific to Specialist Doctors
    public String getConsultationNote() {
        return "Dr. " + getName() + " is a specialist in " + getSpecialization() + ". Clinic: " + clinicRoom;
    }
}
