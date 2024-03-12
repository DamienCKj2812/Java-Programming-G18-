package main;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

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
}