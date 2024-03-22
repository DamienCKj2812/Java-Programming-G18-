package main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class CentreManager {
    protected final HashMap<String, HashMap<String, Object>> customerDetails;
    protected final String customerDataFilePath = "customerDetails.txt";

    public CentreManager() {
        this.customerDetails = new HashMap<>();
        loadCustomerDataFromFile(customerDataFilePath);
    }

public void loadCustomerDataFromFile(String filePath) {
    try {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 11) {
                String orderID = parts[0].toLowerCase();
                System.out.println("Order ID: " + orderID);
                HashMap<String, Object> details = new HashMap<>();
                details.put("hostelName", parts[1].toLowerCase());
                details.put("address", parts[2].toLowerCase());
                details.put("technicianUsername", parts[3].toLowerCase());
                details.put("machineDetails", parts[4].toLowerCase());
                
                try {
                    // Parse appointmentDate into LocalDate object
                    LocalDate parsedDate = LocalDate.parse(parts[5], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    // Format appointmentDate into appropriate string format (ISO 8601)
                    String formattedDate = parsedDate.toString();
                    details.put("appointmentDate", formattedDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Skipping line: " + line);
                    continue; // Skip the current line if the date is invalid
                }

                details.put("additionalNotes", parts[6].toLowerCase());
                details.put("servicePrice", parts[7].toLowerCase());
                details.put("jobStatus", parts[8].toLowerCase());
                details.put("paymentStatus", parts[9].toLowerCase());
                details.put("feedback", parts[10].toLowerCase());
                customerDetails.put(orderID, details);
            } else {
                System.out.println("Skipping line: Insufficient data");
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
    try {
        Path filePath = Paths.get(customerDataFilePath);
        List<String> lines = Files.readAllLines(filePath);
        HashMap<String, Object> updatedDetails = customerDetails.get(orderID);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(customerDataFilePath))) {
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 11 && parts[0].equalsIgnoreCase(orderID)) {
                    // Update the line for the specified order ID
                    if (updatedDetails != null) {
                        writer.write(orderID + "," +
                                updatedDetails.get("hostelName") + "," +
                                updatedDetails.get("address") + "," +
                                updatedDetails.get("technicianUsername") + "," +
                                updatedDetails.get("machineDetails") + "," +
                                updatedDetails.get("appointmentDate") + "," +
                                updatedDetails.get("additionalNotes") + "," +
                                updatedDetails.get("servicePrice") + "," + // Include servicePrice here
                                updatedDetails.get("jobStatus") + "," +
                                updatedDetails.get("paymentStatus") + "," +
                                updatedDetails.get("feedback"));
                    }
                } else {
                    // Write the line as it is
                    writer.write(line);
                }
                writer.newLine();
            }
        }

        // Update the customerDetails HashMap with the modified details
        if (updatedDetails != null) {
            customerDetails.put(orderID, updatedDetails);
        }
    } catch (IOException e) {
        System.out.println("Error updating customer details in file: " + e.getMessage());
    }
}

    public HashMap<String, Object> processPaidPaymentsForMonth(String year, String month) {
        // Filter out entries with paymentStatus as "paid"
        HashMap<String, HashMap<String, Object>> paidCustomers = new HashMap<>();

        for (Map.Entry<String, HashMap<String, Object>> entry : customerDetails.entrySet()) {
            String paymentStatus = (String) entry.getValue().get("paymentStatus");
            if ("paid".equalsIgnoreCase(paymentStatus)) {
                paidCustomers.put(entry.getKey(), entry.getValue());
            }
        }

        // Separate and count payments for the given month and year
        double totalAmountPaid = 0.0;
        int totalCustomersPaidThisMonth = 0;

        for (Map.Entry<String, HashMap<String, Object>> entry : paidCustomers.entrySet()) {
            String appointmentDate = (String) entry.getValue().get("appointmentDate");
            if (isValidDate(appointmentDate, year, month)) {
                double servicePrice = Double.parseDouble((String) entry.getValue().get("servicePrice"));
                totalAmountPaid += servicePrice;
                totalCustomersPaidThisMonth++;
            }
        }

        // Create a HashMap to store the results
        HashMap<String, Object> results = new HashMap<>();
        results.put("totalAmountPaid", totalAmountPaid);
        results.put("totalCustomersPaid", totalCustomersPaidThisMonth);

        // Return the results
        return results;
    }

    public HashMap<String, Object> processUnpaidPaymentsForMonth(String year, String month) {
        // Filter out entries with paymentStatus as "not paid"
        HashMap<String, HashMap<String, Object>> unpaidCustomers = new HashMap<>();

        for (Map.Entry<String, HashMap<String, Object>> entry : customerDetails.entrySet()) {
            String paymentStatus = (String) entry.getValue().get("paymentStatus");
            if ("not paid".equalsIgnoreCase(paymentStatus)) {
                unpaidCustomers.put(entry.getKey(), entry.getValue());
            }
        }

        // Separate and count payments for the given month and year
        double totalAmountUnpaid = 0.0;
        int totalCustomersUnpaidThisMonth = 0;

        for (Map.Entry<String, HashMap<String, Object>> entry : unpaidCustomers.entrySet()) {
            String appointmentDate = (String) entry.getValue().get("appointmentDate");
            if (isValidDate(appointmentDate, year, month)) {
                double servicePrice = Double.parseDouble((String) entry.getValue().get("servicePrice"));
                totalAmountUnpaid += servicePrice;
                totalCustomersUnpaidThisMonth++;
            }
        }

        // Create a HashMap to store the results
        HashMap<String, Object> results = new HashMap<>();
        results.put("totalAmountUnpaid", totalAmountUnpaid);
        results.put("totalCustomersUnpaid", totalCustomersUnpaidThisMonth);

        // Return the results
        return results;
    }

    private boolean isValidDate(String appointmentDate, String targetYear, String targetMonth) {
        String[] dateParts = appointmentDate.split("-");
        return dateParts.length == 3 && dateParts[0].equals(targetYear) && dateParts[1].equals(targetMonth);
    }
    

public void deleteCustomerData(String orderID, String confirmation) {
    orderID = orderID.toLowerCase();

    if (!customerDetails.containsKey(orderID)) {
        System.out.println("Order with ID " + orderID + " does not exist.");
        return;
    }

    if (confirmation.equals("yes")) {
        customerDetails.remove(orderID);
        updateCustomerDetailsInFile(orderID);
        System.out.println("Customer details for order ID " + orderID + " deleted successfully.");
    } else {
        System.out.println("Deletion canceled.");
    }
}

    public Set<String> getUniqueYears() {
        Set<String> uniqueYears = new HashSet<>();

        for (HashMap<String, Object> details : customerDetails.values()) {
            String appointmentDate = (String) details.get("appointmentDate");
            String[] dateParts = appointmentDate.split("-");
            if (dateParts.length == 3) {
                uniqueYears.add(dateParts[0]);
            }
        }

        return uniqueYears;
    }

    // Getter method to retrieve unique months from appointment dates
    public Set<String> getUniqueMonths() {
        Set<String> uniqueMonths = new HashSet<>();

        for (HashMap<String, Object> details : customerDetails.values()) {
            String appointmentDate = (String) details.get("appointmentDate");
            String[] dateParts = appointmentDate.split("-");
            if (dateParts.length == 3) {
                uniqueMonths.add(dateParts[1]);
            }
        }

        return uniqueMonths;
    }
    
public void editOrderDetails(String orderId, String field, Object newDetails) {
    // Call loadCustomerDataFromFile to ensure customerDetails is updated
    loadCustomerDataFromFile(customerDataFilePath);
    
    // Retrieve the order details based on the orderId
    HashMap<String, Object> details = customerDetails.get(orderId.toLowerCase());
    if (details == null) {
        System.out.println("Order ID not found.");
        return;
    }

    // Print orderId and new details before updating customerDetails
    System.out.println("Updating order ID: " + orderId);
    System.out.println("New details for field " + field + ": " + newDetails);

    // Update the specified field
    switch (field.toLowerCase()) {
        case "hostelname":
            details.put("hostelName", newDetails.toString().toLowerCase());
            break;
        case "address":
            details.put("address", newDetails.toString().toLowerCase());
            break;
        case "technicianusername":
            details.put("technicianUsername", newDetails.toString().toLowerCase());
            break;
        case "machinedetails":
            details.put("machineDetails", newDetails.toString().toLowerCase());
            break;
        case "appointmentdate":
            // Parsing the new appointment date
            try {
                LocalDate parsedDate = LocalDate.parse(newDetails.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String formattedDate = parsedDate.toString();
                details.put("appointmentDate", formattedDate);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
                return;
            }
            break;
        case "additionalnotes":
            details.put("additionalNotes", newDetails.toString().toLowerCase());
            break;
        case "serviceprice":
            details.put("servicePrice", newDetails.toString().toLowerCase());
            break;
        default:
            System.out.println("Please select an appropriate field.");
            return;
    }


    // Update the file with modified details
    updateCustomerDetailsInFile(orderId.toLowerCase());
}



    public void displayCustomerDetails() {
        for (Map.Entry<String, HashMap<String, Object>> entry : customerDetails.entrySet()) {
            System.out.println("Order ID: " + entry.getKey());
            HashMap<String, Object> details = entry.getValue();
            for (Map.Entry<String, Object> detail : details.entrySet()) {
                System.out.println(detail.getKey() + ": " + detail.getValue());
            }
            System.out.println("---------------------");
        }
    
    }
    
    public List<Map.Entry<String, HashMap<String, Object>>> viewAppointments() {
        // Convert the customerDetails map to a list of map entries
        List<Map.Entry<String, HashMap<String, Object>>> appointments = new ArrayList<>(customerDetails.entrySet());

        // Sort the appointments by appointment date
        appointments.sort(Comparator.comparing(entry -> (String) entry.getValue().get("appointmentDate")));

        return appointments;
    }
}




    
