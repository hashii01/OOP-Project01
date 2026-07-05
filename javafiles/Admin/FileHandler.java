package healthcare.admin;

import healthcare.patient.*;
import healthcare.doctor.*;
import healthcare.appointment.*;
import healthcare.records.*;
import healthcare.billing.*;
import healthcare.admin.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
// This is the "database" of the project using .txt files
public class FileHandler {

    // ---- File Paths ----
    // All .txt files will be stored in a 'data' folder
    private static final String DATA_FOLDER    = "data/";
    private static final String PATIENTS_FILE  = DATA_FOLDER + "patients.txt";
    private static final String DOCTORS_FILE   = DATA_FOLDER + "doctors.txt";
    private static final String APPT_FILE      = DATA_FOLDER + "appointments.txt";
    private static final String RECORDS_FILE   = DATA_FOLDER + "medical_records.txt";
    private static final String BILLING_FILE   = DATA_FOLDER + "billing.txt";
    private static final String ADMINS_FILE    = DATA_FOLDER + "admins.txt";
    // HELPER: Read all lines from any file
    // Returns a list of strings (one per line)
    private List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return lines; // Return empty list if file doesn't exist yet

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
        }
        return lines;
    }
    // HELPER: Write all lines to any file (overwrites)
    private void writeAllLines(String filePath, List<String> lines) {
        // Make sure the data folder exists
        new File(DATA_FOLDER).mkdirs();

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filePath, false));
            for (String line : lines) {
                writer.println(line);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filePath);
        }
    }
    // HELPER: Append one line to any file
    private void appendLine(String filePath, String line) {
        new File(DATA_FOLDER).mkdirs();
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filePath, true)); // true = append mode
            writer.println(line);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error appending to file: " + filePath);
        }
    }
    //  PATIENT CRUD OPERATIONS

    // CREATE - Add a new patient to patients.txt
    public void savePatient(Patient patient) {
        appendLine(PATIENTS_FILE, patient.toFileString());
        System.out.println("Patient saved: " + patient.getName());
    }

    // READ ALL - Get all patients as a list
    public List<Patient> getAllPatients() {
        List<String> lines = readAllLines(PATIENTS_FILE);
        List<Patient> patients = new ArrayList<>();
        for (String line : lines) {
            Patient p = Patient.fromFileString(line);
            if (p != null) patients.add(p);
        }
        return patients;
    }

    // READ ONE - Find a patient by their ID
    public Patient getPatientById(String patientId) {
        for (Patient p : getAllPatients()) {
            if (p.getPatientId().equals(patientId)) {
                return p;
            }
        }
        return null; // Not found
    }

    // READ ONE - Find a patient by email (for login)
    public Patient getPatientByEmail(String email) {
        for (Patient p : getAllPatients()) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                return p;
            }
        }
        return null;
    }

    // UPDATE - Change a patient's details by ID
    public boolean updatePatient(String patientId, Patient updatedPatient) {
        List<Patient> patients = getAllPatients();
        boolean found = false;

        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatientId().equals(patientId)) {
                patients.set(i, updatedPatient); // Replace old with new
                found = true;
                break;
            }
        }

        if (found) {
            // Rewrite the whole file with updated data
            List<String> lines = new ArrayList<>();
            for (Patient p : patients) lines.add(p.toFileString());
            writeAllLines(PATIENTS_FILE, lines);
        }
        return found;
    }

    // DELETE - Remove a patient by ID
    public boolean deletePatient(String patientId) {
        List<Patient> patients = getAllPatients();
        List<String> newLines = new ArrayList<>();
        boolean found = false;

        for (Patient p : patients) {
            if (!p.getPatientId().equals(patientId)) {
                newLines.add(p.toFileString()); // Keep everyone except the deleted one
            } else {
                found = true;
            }
        }

        if (found) writeAllLines(PATIENTS_FILE, newLines);
        return found;
    }
    //  DOCTOR CRUD OPERATIONS

    public void saveDoctor(Doctor doctor) {
        appendLine(DOCTORS_FILE, doctor.toFileString());
        System.out.println("Doctor saved: " + doctor.getName());
    }

    public List<Doctor> getAllDoctors() {
        List<String> lines = readAllLines(DOCTORS_FILE);
        List<Doctor> doctors = new ArrayList<>();
        for (String line : lines) {
            Doctor d = Doctor.fromFileString(line);
            if (d != null) doctors.add(d);
        }
        return doctors;
    }

    public Doctor getDoctorById(String doctorId) {
        for (Doctor d : getAllDoctors()) {
            if (d.getDoctorId().equals(doctorId)) return d;
        }
        return null;
    }

    public boolean updateDoctor(String doctorId, Doctor updatedDoctor) {
        List<Doctor> doctors = getAllDoctors();
        boolean found = false;
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getDoctorId().equals(doctorId)) {
                doctors.set(i, updatedDoctor);
                found = true;
                break;
            }
        }
        if (found) {
            List<String> lines = new ArrayList<>();
            for (Doctor d : doctors) lines.add(d.toFileString());
            writeAllLines(DOCTORS_FILE, lines);
        }
        return found;
    }

    public boolean deleteDoctor(String doctorId) {
        List<Doctor> doctors = getAllDoctors();
        List<String> newLines = new ArrayList<>();
        boolean found = false;
        for (Doctor d : doctors) {
            if (!d.getDoctorId().equals(doctorId)) {
                newLines.add(d.toFileString());
            } else {
                found = true;
            }
        }
        if (found) writeAllLines(DOCTORS_FILE, newLines);
        return found;
    }
    //  APPOINTMENT CRUD OPERATIONS

    public void saveAppointment(Appointment appointment) {
        appendLine(APPT_FILE, appointment.toFileString());
    }

    public List<Appointment> getAllAppointments() {
        List<String> lines = readAllLines(APPT_FILE);
        List<Appointment> appointments = new ArrayList<>();
        for (String line : lines) {
            Appointment a = Appointment.fromFileString(line);
            if (a != null) appointments.add(a);
        }
        return appointments;
    }

    public Appointment getAppointmentById(String appointmentId) {
        for (Appointment a : getAllAppointments()) {
            if (a.getAppointmentId().equals(appointmentId)) return a;
        }
        return null;
    }

    // Get all appointments for a specific patient
    public List<Appointment> getAppointmentsByPatientId(String patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : getAllAppointments()) {
            if (a.getPatientId().equals(patientId)) result.add(a);
        }
        return result;
    }

    public boolean updateAppointment(String appointmentId, Appointment updated) {
        List<Appointment> appointments = getAllAppointments();
        boolean found = false;
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(appointmentId)) {
                appointments.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) {
            List<String> lines = new ArrayList<>();
            for (Appointment a : appointments) lines.add(a.toFileString());
            writeAllLines(APPT_FILE, lines);
        }
        return found;
    }

    public boolean deleteAppointment(String appointmentId) {
        List<Appointment> appointments = getAllAppointments();
        List<String> newLines = new ArrayList<>();
        boolean found = false;
        for (Appointment a : appointments) {
            if (!a.getAppointmentId().equals(appointmentId)) {
                newLines.add(a.toFileString());
            } else {
                found = true;
            }
        }
        if (found) writeAllLines(APPT_FILE, newLines);
        return found;
    }
    //  MEDICAL RECORD CRUD OPERATIONS

    public void saveMedicalRecord(MedicalRecord record) {
        appendLine(RECORDS_FILE, record.toFileString());
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        List<String> lines = readAllLines(RECORDS_FILE);
        List<MedicalRecord> records = new ArrayList<>();
        for (String line : lines) {
            MedicalRecord r = MedicalRecord.fromFileString(line);
            if (r != null) records.add(r);
        }
        return records;
    }

    public List<MedicalRecord> getRecordsByPatientId(String patientId) {
        List<MedicalRecord> result = new ArrayList<>();
        for (MedicalRecord r : getAllMedicalRecords()) {
            if (r.getPatientId().equals(patientId)) result.add(r);
        }
        return result;
    }

    public boolean updateMedicalRecord(String recordId, MedicalRecord updated) {
        List<MedicalRecord> records = getAllMedicalRecords();
        boolean found = false;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getRecordId().equals(recordId)) {
                records.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) {
            List<String> lines = new ArrayList<>();
            for (MedicalRecord r : records) lines.add(r.toFileString());
            writeAllLines(RECORDS_FILE, lines);
        }
        return found;
    }

    public boolean deleteMedicalRecord(String recordId) {
        List<MedicalRecord> records = getAllMedicalRecords();
        List<String> newLines = new ArrayList<>();
        boolean found = false;
        for (MedicalRecord r : records) {
            if (!r.getRecordId().equals(recordId)) {
                newLines.add(r.toFileString());
            } else {
                found = true;
            }
        }
        if (found) writeAllLines(RECORDS_FILE, newLines);
        return found;
    }
    //  BILLING CRUD OPERATIONS

    public void saveBilling(Billing billing) {
        appendLine(BILLING_FILE, billing.toFileString());
    }

    public List<Billing> getAllBillings() {
        List<String> lines = readAllLines(BILLING_FILE);
        List<Billing> billings = new ArrayList<>();
        for (String line : lines) {
            Billing b = Billing.fromFileString(line);
            if (b != null) billings.add(b);
        }
        return billings;
    }

    public Billing getBillingById(String billId) {
        for (Billing b : getAllBillings()) {
            if (b.getBillId().equals(billId)) return b;
        }
        return null;
    }

    public boolean updateBilling(String billId, Billing updated) {
        List<Billing> billings = getAllBillings();
        boolean found = false;
        for (int i = 0; i < billings.size(); i++) {
            if (billings.get(i).getBillId().equals(billId)) {
                billings.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) {
            List<String> lines = new ArrayList<>();
            for (Billing b : billings) lines.add(b.toFileString());
            writeAllLines(BILLING_FILE, lines);
        }
        return found;
    }

    public boolean deleteBilling(String billId) {
        List<Billing> billings = getAllBillings();
        List<String> newLines = new ArrayList<>();
        boolean found = false;
        for (Billing b : billings) {
            if (!b.getBillId().equals(billId)) {
                newLines.add(b.toFileString());
            } else {
                found = true;
            }
        }
        if (found) writeAllLines(BILLING_FILE, newLines);
        return found;
    }
    //  ADMIN CRUD OPERATIONS

    public void saveAdmin(Admin admin) {
        appendLine(ADMINS_FILE, admin.toFileString());
    }

    public List<Admin> getAllAdmins() {
        List<String> lines = readAllLines(ADMINS_FILE);
        List<Admin> admins = new ArrayList<>();
        for (String line : lines) {
            Admin a = Admin.fromFileString(line);
            if (a != null) admins.add(a);
        }
        return admins;
    }

    // Find admin by email (used for admin login)
    public Admin getAdminByEmail(String email) {
        for (Admin a : getAllAdmins()) {
            if (a.getEmail().equalsIgnoreCase(email)) return a;
        }
        return null;
    }

    public boolean deleteAdmin(String adminId) {
        List<Admin> admins = getAllAdmins();
        List<String> newLines = new ArrayList<>();
        boolean found = false;
        for (Admin a : admins) {
            if (!a.getAdminId().equals(adminId)) {
                newLines.add(a.toFileString());
            } else {
                found = true;
            }
        }
        if (found) writeAllLines(ADMINS_FILE, newLines);
        return found;
    }
}
