package healthcare.records;

public class Prescription extends MedicalRecord {
    public Prescription(String recordId, String patientId, String doctorId,
                        String diagnosis, String prescription, String visitDate) {
        super(recordId, patientId, doctorId, diagnosis, prescription, visitDate, "Prescription");
    }

    @Override
    public boolean requiresSpecialAuthorization() {
        return false; // Prescriptions are generally accessible by the patient
    }
}
