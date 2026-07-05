package healthcare.admin;

import java.io.Serializable;

// session user details

public class LoggedInUser implements Serializable {

    private String id;       // e.g. "ADM001", "D001", "P001"
    private String name;     // e.g. "John Doe"
    private String email;    // e.g. "john@example.com"
    private String role;     // "ADMIN", "DOCTOR", or "PATIENT"

    // constructor
    public LoggedInUser(String id, String name, String email, String role) {
        this.id    = id;
        this.name  = name;
        this.email = email;
        this.role  = role;
    }
    public String getId()    { return id; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public String getRole()  { return role; }
    public boolean isAdmin()   { return "ADMIN".equals(role); }
    public boolean isDoctor()  { return "DOCTOR".equals(role); }
    public boolean isPatient() { return "PATIENT".equals(role); }
}
