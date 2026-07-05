package healthcare.admin;

import healthcare.admin.LoggedInUser;

import java.io.File;
import java.util.Scanner;
// This is the "Security Guard" of the system.
// It checks admins.txt, doctors.txt, and patients.txt
// using a simple Scanner (no database needed).
public class AuthService {

    // File paths — same as FileHandler
    private static final String DATA_FOLDER   = "data/";
    private static final String ADMINS_FILE   = DATA_FOLDER + "admins.txt";
    private static final String DOCTORS_FILE  = DATA_FOLDER + "doctors.txt";
    private static final String PATIENTS_FILE = DATA_FOLDER + "patients.txt";

    // -------------------------------------------------------
    // MAIN METHOD: Try to log in a user with email, password, and specific role.
    // Returns a LoggedInUser if credentials match in the corresponding file, or null.
    // -------------------------------------------------------
    public LoggedInUser login(String email, String password, String expectedRole) {

        if ("ADMIN".equalsIgnoreCase(expectedRole)) {
            // Step 1: Check admins.txt
            // Format: adminId,name,email,password
            return searchFile(ADMINS_FILE, email, password, "ADMIN",
                    /* idIndex= */ 0, /* nameIndex= */ 1, /* emailIndex= */ 2, /* passIndex= */ 3);
        }
        else if ("DOCTOR".equalsIgnoreCase(expectedRole)) {
            // Step 2: Check doctors.txt
            // Format: doctorId,name,email,password,phone,specialization,doctorType
            return searchFile(DOCTORS_FILE, email, password, "DOCTOR",
                    /* idIndex= */ 0, /* nameIndex= */ 1, /* emailIndex= */ 2, /* passIndex= */ 3);
        }
        else {
            // Step 3: Check patients.txt
            // Format: patientId,name,email,phone,password,patientType
            return searchFile(PATIENTS_FILE, email, password, "PATIENT",
                    /* idIndex= */ 0, /* nameIndex= */ 1, /* emailIndex= */ 2, /* passIndex= */ 4);
        }
    }

    // -------------------------------------------------------
    // HELPER: Reads a file line-by-line using Scanner.
    // Returns a LoggedInUser if email+password match, else null.
    // -------------------------------------------------------
    private LoggedInUser searchFile(String filePath, String email, String password,
                                    String role, int idIdx, int nameIdx,
                                    int emailIdx, int passIdx) {
        File file = new File(filePath);
        if (!file.exists()) return null; // File not created yet — skip

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Skip blank lines and comment lines (lines starting with #)
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");

                // Make sure the line has enough columns
                int maxIdx = Math.max(Math.max(idIdx, nameIdx), Math.max(emailIdx, passIdx));
                if (parts.length <= maxIdx) continue;

                String fileEmail    = parts[emailIdx].trim();
                String filePassword = parts[passIdx].trim();

                // Case-insensitive email check, exact password check
                if (fileEmail.equalsIgnoreCase(email) && filePassword.equals(password)) {
                    String id   = parts[idIdx].trim();
                    String name = parts[nameIdx].trim();
                    return new LoggedInUser(id, name, email, role);
                }
            }
        } catch (Exception e) {
            System.err.println("AuthService error reading " + filePath + ": " + e.getMessage());
        }

        return null; // Not found in this file
    }
}
