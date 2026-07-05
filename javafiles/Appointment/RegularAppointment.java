package healthcare.appointment;

public class RegularAppointment extends Appointment {
    public RegularAppointment(String appointmentId, String patientId, String doctorId,
                              String date, String time, String status, String reason) {
        super(appointmentId, patientId, doctorId, date, time, status, reason, "Regular");
    }

    @Override
    public String getWorkflow() {
        return "Regular Appointment: Add to standard waiting queue.";
    }
}
