
package main;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class UserFileHandler extends RoleAuth {
    // Constructor
    public UserFileHandler() {
        super(); // Call the constructor of RoleAuth to initialize userCredentials
    }


    // Getter method to retrieve usernames with the role of "technician"
    public List<String> getTechnicianUsernames() {
        List<String> technicianUsernames = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : userCredentials.entrySet()) {
            if ("technician".equals(entry.getValue()[1])) { // Role is stored at index 1
                technicianUsernames.add(entry.getKey()); // Add username to the list
            }
        }
        return technicianUsernames;
    }

    // Getter method to retrieve usernames with the role of "centre manager"
    public List<String> getCentreManagerUsernames() {
        List<String> centreManagerUsernames = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : userCredentials.entrySet()) {
            if ("centre manager".equals(entry.getValue()[1])) { // Role is stored at index 1
                centreManagerUsernames.add(entry.getKey()); // Add username to the list
            }
        }
        return centreManagerUsernames;
    }
    
    // Getter method to retrieve all feedback that is not null
    public List<String> getAllFeedback() {
        List<String> allFeedback = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : userCredentials.entrySet()) {
            String feedback = entry.getValue()[9]; // Feedback is stored at index 2
            if (feedback != null && !feedback.equals("null")) { // Check if feedback is not null and not "null"
                allFeedback.add(feedback);
            }
        }
        return allFeedback;
    }
}