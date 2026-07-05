package healthcare.doctor;
// Demonstrates: ENCAPSULATION
public class Doctor {

    private String doctorId;
    private String name;
    private String email;
    private String password;      // NEW: for login
    private String phone;
    private String specialization;
    private String doctorType;    // "General" or "Specialist"

    // Constructor (7 fields)
    public Doctor(String doctorId, String name, String email, String password,
                  String phone, String specialization, String doctorType) {
        this.doctorId       = doctorId;
        this.name           = name;
        this.email          = email;
        this.password       = password;
        this.phone          = phone;
        this.specialization = specialization;
        this.doctorType     = doctorType;
    }

    // ---- Getters ----
    public String getDoctorId()       { return doctorId; }
    public String getName()           { return name; }
    public String getEmail()          { return email; }
    public String getPassword()       { return password; }
    public String getPhone()          { return phone; }
    public String getSpecialization() { return specialization; }
    public String getDoctorType()     { return doctorType; }

    // ---- Setters ----
    public void setDoctorId(String doctorId)             { this.doctorId = doctorId; }
    public void setName(String name)                     { this.name = name; }
    public void setEmail(String email)                   { this.email = email; }
    public void setPassword(String password)             { this.password = password; }
    public void setPhone(String phone)                   { this.phone = phone; }
    public void setSpecialization(String s)              { this.specialization = s; }
    public void setDoctorType(String doctorType)         { this.doctorType = doctorType; }

    // Converts to one line of text for saving
    // Format: doctorId,name,email,password,phone,specialization,doctorType
    public String toFileString() {
        return doctorId + "," + name + "," + email + "," + password + ","
             + phone + "," + specialization + "," + doctorType;
    }

    // Creates a Doctor object from a line of text
    // Format: doctorId,name,email,password,phone,specialization,doctorType
    public static Doctor fromFileString(String line) {
        if (line == null || line.trim().startsWith("#")) return null;
        String[] parts = line.split(",");
        if (parts.length < 7) return null;

        // parts[0]=id  [1]=name  [2]=email  [3]=password
        // parts[4]=phone  [5]=specialization  [6]=doctorType
        String type = parts[6].trim();
        if (type.equalsIgnoreCase("Specialist")) {
            return new SpecialistDoctor(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        } else {
            return new GeneralDoctor(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        }
    }

    public void displayInfo() {
        System.out.println("ID: " + doctorId + " | Dr. " + name + " | " + specialization + " | Type: " + doctorType);
    }

    // Abstraction: Methods to check doctor availability before booking.
    public boolean isAvailable(String date) {
        // In a real database, this would query appointments.
        // For this flat-file implementation, we abstract the logic and default to true
        // unless it's a Sunday (as a mock business rule).
        try {
            java.time.LocalDate requestedDate = java.time.LocalDate.parse(date);
            if (requestedDate.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
                return false; // Doctors don't work on Sundays
            }
        } catch (Exception e) {
            // Invalid format
        }
        return true;
    }
}
