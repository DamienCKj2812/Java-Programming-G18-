package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleAuth{
    
    // HashMap to store user credentials (username, password, role)
    protected HashMap<String, String[]> userCredentials;
   
    
    public RoleAuth() {
        userCredentials = new HashMap<>();
        loadUserDataFromFile("roleInfo.txt");
    }

    private void loadUserDataFromFile(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];
                    userCredentials.put(username, new String[]{password, role});
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user data from file: " + e.getMessage());
        }
    }

    
    // Method to sign up a new user
    public void signUpRole(String username, String password, String role) {
        username.toLowerCase();
        // Check if the username already exists
        if (userCredentials.containsKey(username)) {
            System.out.println("Username repeated!");
            throw new IllegalArgumentException("Username already exists. Please choose a different username.");
        } else {
            // Store the username, password, and role in the HashMap
            userCredentials.put(username, new String[]{password, role});
            System.out.println("User signed up successfully!");
            
             // Write user information to the text file
            try {
                FileWriter writer = new FileWriter("roleInfo.txt", true);
                writer.write(username + "," + password + "," + role.toLowerCase() + "\n");
                writer.close();
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        }
    }
    
    public String signInRole(String username, String password) {
        String[] credentials = userCredentials.get(username.toLowerCase());
        if (credentials != null && credentials[0].equals(password)) {
            return credentials[1]; // Return the role associated with the username
        }
        return null; // Return null if user not found or password is incorrect
    }
    
   public HashMap<String, String[]> getTechnicianCredentials() {
        HashMap<String, String[]> technicianCredentials = new HashMap<>();
        for (Map.Entry<String, String[]> entry : userCredentials.entrySet()) {
            String username = entry.getKey();
            String[] userInfo = entry.getValue();
            String role = userInfo[1];
            if ("technician".equalsIgnoreCase(role) || "technician(b)".equalsIgnoreCase(role)) {
                technicianCredentials.put(username, userInfo);
            }
        }
        return technicianCredentials;
    }
    
    public String banUser(String username) {
        // Check if the username exists
        if (userCredentials.containsKey(username)) {
            String[] credentials = userCredentials.get(username);
            String role = credentials[1];

            // Check if the user has the role "technician"
            if ("technician".equalsIgnoreCase(role)) {
                // Append "(b)" to the role
                String newRole = role + "(b)";
                credentials[1] = newRole;

                // Update the HashMap
                userCredentials.put(username, credentials);

                // Update the file with the new information
                updateUserInfoInFile(username, credentials);

                return "User " + username + " has been banned.";
            } else {
                return "User " + username + " is already be banned.";
            }
        } else {
            return "User " + username + " not found.";
        }
    }

    public String unbanUser(String username) {
        username = username.toLowerCase();

        // Check if the username exists in the userCredentials
        if (userCredentials.containsKey(username)) {
            String[] credentials = userCredentials.get(username);
            String role = credentials[1];

            // Check if the role is a technician with "(b)"
            if ("technician(b)".equalsIgnoreCase(role)) {
                // Remove "(b)" from the role
                role = "technician";

                // Update the role in the userCredentials
                userCredentials.put(username, new String[]{credentials[0], role});

                // Update the file with the modified role
                updateRoleInFile(username, role);

                return "User " + username + " has been unbanned.";
            } else {
                return "User " + username + " is not banned.";
            }
        } else {
            return "Username " + username + " not found.";
        }
    }


    // Method to update user information in the text file
    private void updateUserInfoInFile(String username, String[] credentials) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("roleInfo.txt"))) {
            for (HashMap.Entry<String, String[]> entry : userCredentials.entrySet()) {
                String currentUsername = entry.getKey();
                String[] currentCredentials = entry.getValue();

                if (currentUsername.equals(username)) {
                    writer.write(username + "," + credentials[0] + "," + credentials[1] + "\n");
                } else {
                    writer.write(currentUsername + "," + currentCredentials[0] + "," + currentCredentials[1] + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating user information in file: " + e.getMessage());
        }
    }
    
    private void updateRoleInFile(String username, String role) {
        // Update the role information in the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("roleInfo.txt"))) {
            for (String user : userCredentials.keySet()) {
                String[] credentials = userCredentials.get(user);
                writer.write(user + "," + credentials[0] + "," + credentials[1] + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating role information in the file: " + e.getMessage());
        }
    }
    
}