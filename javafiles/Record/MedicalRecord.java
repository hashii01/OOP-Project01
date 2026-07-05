package healthcare.records;
public abstract class MedicalRecord {

    private String recordId;
    private String patientId;
    private String doctorId;
    private String diagnosis;
    private String prescription;
    private String visitDate;
    private String recordType; // "Prescription" or "DiagnosisReport"

    // Constructor
    public MedicalRecord(String recordId, String patientId, String doctorId,
                         String diagnosis, String prescription, String visitDate, String recordType) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.visitDate = visitDate;
        this.recordType = recordType;
    }

    // ---- Getters ----
    public String getRecordId()      { return recordId; }
    public String getPatientId()     { return patientId; }
    public String getDoctorId()      { return doctorId; }
    public String getDiagnosis()     { return diagnosis; }
    public String getPrescription()  { return prescription; }
    public String getVisitDate()     { return visitDate; }
    public String getRecordType()    { return recordType; }

    // ---- Setters ----
    public void setRecordId(String recordId)         { this.recordId = recordId; }
    public void setPatientId(String patientId)       { this.patientId = patientId; }
    public void setDoctorId(String doctorId)         { this.doctorId = doctorId; }
    public void setDiagnosis(String diagnosis)       { this.diagnosis = diagnosis; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    public void setVisitDate(String visitDate)       { this.visitDate = visitDate; }
    public void setRecordType(String recordType)     { this.recordType = recordType; }

    // Abstraction: method to be overridden
    public abstract boolean requiresSpecialAuthorization();

    // Format: recordId,patientId,doctorId,diagnosis,prescription,visitDate,recordType
    public String toFileString() {
        return recordId + "," + patientId + "," + doctorId + "," +
               diagnosis + "," + prescription + "," + visitDate + "," + recordType;
    }

    public static MedicalRecord fromFileString(String line) {
        if (line == null || line.trim().startsWith("#")) return null;
        String[] parts = line.split(",");
        if (parts.length < 7) {
            if (parts.length == 6) {
                return new Prescription(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
            }
            return null;
        }
        
        String type = parts[6].trim();
        if ("DiagnosisReport".equalsIgnoreCase(type)) {
            return new DiagnosisReport(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        } else {
            return new Prescription(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        }
    }
}
