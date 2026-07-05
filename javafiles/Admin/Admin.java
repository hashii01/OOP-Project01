package healthcare.admin;
public class Admin extends User {

    // Constructor
    public Admin(String adminId, String name, String email, String password) {
        super(adminId, name, email, password);
    }

    // ---- Getters ----
    public String getAdminId() { return super.getId(); }

    // ---- Setters ----
    public void setAdminId(String adminId) { super.setId(adminId); }

    @Override
    public String getRole() {
        return "Administrator";
    }

    // Format: adminId,name,email,password
    public String toFileString() {
        return super.getId() + "," + super.getName() + "," + super.getEmail() + "," + super.getPassword();
    }

    public static Admin fromFileString(String line) {
        if (line == null || line.trim().startsWith("#")) return null;
        String[] parts = line.split(",");
        if (parts.length < 4) return null;
        return new Admin(parts[0], parts[1], parts[2], parts[3]);
    }
}
