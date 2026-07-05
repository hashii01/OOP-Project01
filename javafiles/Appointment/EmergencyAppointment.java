package healthcare.appointment;

public class EmergencyAppointment extends Appointment {
    public EmergencyAppointment(String appointmentId, String patientId, String doctorId,
                                String date, String time, String status, String reason) {
        super(appointmentId, patientId, doctorId, date, time, status, reason, "Emergency");
    }

    @Override
    public String getWorkflow() {
        return "Emergency Appointment: Bypassing standard queue. Alerting staff immediately.";
    }
}
