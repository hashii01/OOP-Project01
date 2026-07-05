package healthcare.records;

public class DiagnosisReport extends MedicalRecord {
    public DiagnosisReport(String recordId, String patientId, String doctorId,
                           String diagnosis, String prescription, String visitDate) {
        super(recordId, patientId, doctorId, diagnosis, prescription, visitDate, "DiagnosisReport");
    }

    @Override
    public boolean requiresSpecialAuthorization() {
        return true; // Diagnosis reports may contain sensitive info
    }
}
