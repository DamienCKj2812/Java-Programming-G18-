package main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.stream.Collectors;

public class CentreManager {
    protected final HashMap<String, HashMap<String, Object>> customerDetails;
    protected final String customerDataFilePath = "customerDetails.txt";

    public CentreManager() {
        this.customerDetails = new HashMap<>();
        loadCustomerDataFromFile(customerDataFilePath);
    }

    private void loadCustomerDataFromFile(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 11) {
                    String orderID = parts[0].toLowerCase();
                    HashMap<String, Object> details = new HashMap<>();
                    details.put("hostelName", parts[1].toLowerCase());
                    details.put("address", parts[2].toLowerCase());
                    details.put("technicianUsername", parts[3].toLowerCase());
                    details.put("machineDetails", parts[4].toLowerCase());
                    details.put("appointmentDate", parts[5].toLowerCase());
                    details.put("additionalNotes", parts[6].toLowerCase());
                    details.put("servicePrice", parts[7].toLowerCase());
                    details.put("jobStatus", parts[8].toLowerCase());
                    details.put("paymentStatus", parts[9].toLowerCase());
                    details.put("feedback", parts[10].toLowerCase());
                    customerDetails.put(orderID, details);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customer data from file: " + e.getMessage());
        }
    }
    


    public String registerCustomer(String hostelName, String address, String technicianUsername, String machineDetails, String appointmentDate, 
            String additionalNotes, String servicePrice) throws DateTimeParseException {
        // Validate the customer details
        validateCustomer(hostelName, machineDetails, appointmentDate);

        // Generate a unique order ID
        String orderID = generateOrderID();

        // Parse appointmentDate into LocalDate object
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(appointmentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Error parsing appointment date. Please enter the date in the format YYYY-MM-DD.", appointmentDate, 0);
        }

        // Format appointmentDate into appropriate string format (ISO 8601)
        String formattedDate = parsedDate.toString();

        // Add the customer details to the hashmap
        HashMap<String, Object> details = new HashMap<>();
        details.put("hostelName", hostelName.toLowerCase());
        details.put("address", address.toLowerCase());
        details.put("technicianUsername", technicianUsername);
        details.put("machineDetails", machineDetails.toLowerCase());
        details.put("appointmentDate", formattedDate); // Store formatted date
        details.put("additionalNotes", additionalNotes.isEmpty() ? "no additional notes" : additionalNotes.toLowerCase());
        details.put("servicePrice", servicePrice);
        details.put("jobStatus", "in progress");
        details.put("paymentStatus", "not paid");
        details.put("feedback", "null");

        // Write customer details to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(customerDataFilePath, true))) {
            writer.write(orderID.toLowerCase() + "," +
            hostelName.toLowerCase() + "," +
            address.toLowerCase() + "," +
            technicianUsername.toLowerCase() + "," + // technicianUsername column
            machineDetails.toLowerCase() + "," +
            formattedDate + "," + // Use formatted date
            (additionalNotes.isEmpty() ? "no additional notes" : additionalNotes.toLowerCase()) + "," +
            servicePrice + "," + 
            "in progress" + "," + // jobStatus column
            "not paid" + "," + // paymentStatus column
            "null"); //feedback column
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing customer data to file: " + e.getMessage());
            return "Error writing customer data to file: " + e.getMessage();
        } 

        // Updating the hashmap with the new customer details.
        customerDetails.put(orderID.toLowerCase(), details);

        return orderID;
    }

    // Method to validate customer details
    private void validateCustomer(String hostelName, String machineDetails, String appointmentDate) {
        for (HashMap<String, Object> details : customerDetails.values()) {
            String existingHostelName = (String) details.get("hostelName");
            String existingMachineDetails = (String) details.get("machineDetails");
            String existingAppointmentDate = (String) details.get("appointmentDate");
            if (existingHostelName.equalsIgnoreCase(hostelName) && existingMachineDetails.equalsIgnoreCase(machineDetails) && existingAppointmentDate.equalsIgnoreCase(appointmentDate)) {
                throw new IllegalArgumentException("Order repeated, please check again");
            }
        } 
    }
    
    public List<Map.Entry<String, HashMap<String, Object>>> viewAppointments() {
        // Filter out completed and paid appointments
        List<Map.Entry<String, HashMap<String, Object>>> filteredAppointments = customerDetails.entrySet().stream()
                .filter(entry -> "in progress".equalsIgnoreCase((String) entry.getValue().get("jobStatus")) ||
                                  "not paid".equalsIgnoreCase((String) entry.getValue().get("paymentStatus")))
                .collect(Collectors.toList());

        // Sort the appointments by appointment date
        filteredAppointments.sort(Comparator.comparing(entry -> (String) entry.getValue().get("appointmentDate")));

        return filteredAppointments;
    }
    
    public List<Map.Entry<String, HashMap<String, Object>>> viewFeedbacks() {
        // Filter out completed and paid appointments
        List<Map.Entry<String, HashMap<String, Object>>> filteredAppointments = customerDetails.entrySet().stream()
                .filter(entry -> "completed".equalsIgnoreCase((String) entry.getValue().get("jobStatus")) ||
                                  "paid".equalsIgnoreCase((String) entry.getValue().get("paymentStatus")))
                .collect(Collectors.toList());

        // Sort the appointments by appointment date
        filteredAppointments.sort(Comparator.comparing(entry -> (String) entry.getValue().get("appointmentDate")));

        return filteredAppointments;
    }

    // Method to generate a unique order ID
    private String generateOrderID() {
        return UUID.randomUUID().toString().substring(0, 8); // Generates a random UUID and takes the first 8 characters
    }


    public void updateTechnicianUsername(String orderID, String newTechnicianUsername) {
        if (customerDetails.containsKey(orderID.toLowerCase())) {
            customerDetails.get(orderID.toLowerCase()).put("technicianUsername", newTechnicianUsername.toLowerCase());
            updateCustomerDetailsInFile(orderID);
        } else {
            throw new IllegalArgumentException("Order with ID " + orderID + " does not exist.");
        }
    }
    
    // Write file into text file
    private void updateCustomerDetailsInFile(String orderID) {
        boolean orderExists = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(customerDataFilePath))) {
            for (String id : customerDetails.keySet()) {
                if (id.equalsIgnoreCase(orderID)) {
                    orderExists = true;
                    HashMap<String, Object> details = customerDetails.get(id);
                    writer.write(id.toLowerCase() + "," +
                    details.get("hostelName") + "," +
                    details.get("address") + "," +
                    details.get("technicianUsername") + "," +
                    details.get("machineDetails") + "," +
                    details.get("appointmentDate") + "," +
                    details.get("additionalNotes") + "," +
                    details.get("jobStatus") + "," +
                    details.get("paymentStatus") + "," +
                    details.get("feedback"));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating customer details in file: " + e.getMessage());
        }
        if (!orderExists) {
                throw new IllegalArgumentException("Order with ID " + orderID + " does not exist.");
            }
        
    

    }
}
