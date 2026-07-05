package healthcare.appointment;
public class Appointment {

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String date;        // e.g. "2025-06-15"
    private String time;        // e.g. "10:30 AM"
    private String status;      // "Pending", "Confirmed", "Cancelled"
    private String reason;      // Reason for visit
    private String appointmentType; // "Regular" or "Emergency"

    // Constructor
    public Appointment(String appointmentId, String patientId, String doctorId,
                       String date, String time, String status, String reason, String appointmentType) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.reason = reason;
        this.appointmentType = appointmentType;
    }

    // ---- Getters ----
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId()     { return patientId; }
    public String getDoctorId()      { return doctorId; }
    public String getDate()          { return date; }
    public String getTime()          { return time; }
    public String getStatus()        { return status; }
    public String getReason()        { return reason; }
    public String getAppointmentType() { return appointmentType; }

    // ---- Setters ----
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public void setPatientId(String patientId)         { this.patientId = patientId; }
    public void setDoctorId(String doctorId)           { this.doctorId = doctorId; }
    public void setDate(String date)                   { this.date = date; }
    public void setTime(String time)                   { this.time = time; }
    public void setStatus(String status)               { this.status = status; }
    public void setReason(String reason)               { this.reason = reason; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

    // Abstraction/Polymorphism: method to be overridden
    public String getWorkflow() {
        return "Standard booking workflow.";
    }

    // Format: appointmentId,patientId,doctorId,date,time,status,reason,appointmentType
    public String toFileString() {
        return appointmentId + "," + patientId + "," + doctorId + "," +
               date + "," + time + "," + status + "," + reason + "," + appointmentType;
    }

    public static Appointment fromFileString(String line) {
        if (line == null || line.trim().startsWith("#")) return null;
        String[] parts = line.split(",");
        if (parts.length < 8) {
            // Backwards compatibility
            if (parts.length == 7) {
                return new RegularAppointment(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
            }
            return null;
        }
        
        String type = parts[7].trim();
        if ("Emergency".equalsIgnoreCase(type)) {
            return new EmergencyAppointment(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
        } else {
            return new RegularAppointment(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
        }
    }
}
