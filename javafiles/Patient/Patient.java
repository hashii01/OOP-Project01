package healthcare.patient;
// Demonstrates: ENCAPSULATION
public abstract class Patient {

    // Private fields - hidden from outside (Encapsulation)
    private String patientId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String patientType; // "Regular" or "Premium"

    // Constructor
    public Patient(String patientId, String name, String email, String phone, String password, String patientType) {
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.patientType = patientType;
    }

    // ---- Getters ----
    public String getPatientId() { return patientId; }
    public String getName()      { return name; }
    public String getEmail()     { return email; }
    public String getPhone()     { return phone; }
    public String getPassword()  { return password; }
    public String getPatientType() { return patientType; }

    // ---- Setters ----
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public void setName(String name)           { this.name = name; }
    public void setEmail(String email)         { this.email = email; }
    public void setPhone(String phone)         { this.phone = phone; }
    public void setPassword(String password)   { this.password = password; }
    public void setPatientType(String patientType) { this.patientType = patientType; }

    // Converts a patient object into one line of text for saving to file
    // Format: patientId,name,email,phone,password,patientType
    public String toFileString() {
        return patientId + "," + name + "," + email + "," + phone + "," + password + "," + patientType;
    }

    // Creates a Patient object from one line of text read from file
    public static Patient fromFileString(String line) {
        if (line == null || line.trim().startsWith("#")) return null;
        
        String[] parts = line.split(",");
        if (parts.length < 6) return null;

        String type = parts[5].trim();
        if (type.equals("Premium")) {
            return new PremiumPatient(parts[0], parts[1], parts[2], parts[3], parts[4]);
        } else {
            return new RegularPatient(parts[0], parts[1], parts[2], parts[3], parts[4]);
        }
    }

    // A method all patients can use
    public void displayInfo() {
        System.out.println("ID: " + patientId + " | Name: " + name + " | Type: " + patientType);
    }

    // Polymorphism / Abstraction
    public abstract String getMembershipBenefits();
}
