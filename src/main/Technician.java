package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Technician extends CentreManager {
    UserProfileInfo userProfileInfo = new UserProfileInfo();
   
    public Technician() {
        super(); // Call the constructor of the superclass (CentreManager) to initialize the HashMap
    }

    // Method to access the inherited customerDetails HashMap
    public void accessCustomerDetails() {
        // Access the customerDetails HashMap inherited from CentreManager
        HashMap<String, HashMap<String, Object>> customerDetails = this.customerDetails;

      
    }
    
    public boolean updateJobStatusToCompleted(String orderID) {
        if (customerDetails.containsKey(orderID.toLowerCase())) {
            customerDetails.get(orderID.toLowerCase()).put("jobStatus", "completed");
            updateCustomerDetailsInFile(orderID);
            return true;
        } else {
            throw new IllegalArgumentException("Order with ID " + orderID + " does not exist.");
        }
    }

  
    public boolean updatePaymentStatusToPaid(String orderID) {
        if (customerDetails.containsKey(orderID.toLowerCase())) {
            customerDetails.get(orderID.toLowerCase()).put("paymentStatus", "paid");
            updateCustomerDetailsInFile(orderID);
            return true;
        } else {
            throw new IllegalArgumentException("Order with ID " + orderID + " does not exist.");
        }
    }
    
    
    public void updateFeedback(String orderID, String feedback) {
        if (customerDetails.containsKey(orderID.toLowerCase())) {
            HashMap<String, Object> details = customerDetails.get(orderID.toLowerCase());
            String paymentStatus = (String) details.get("paymentStatus");
            String jobStatus = (String) details.get("jobStatus");

            if ("paid".equalsIgnoreCase(paymentStatus) && "completed".equalsIgnoreCase(jobStatus)) {
                details.put("feedback", feedback);
                updateCustomerDetailsInFile(orderID);
            } else {
                throw new IllegalArgumentException("Feedback can only be provided for orders with paid status and completed job.");
            }
        } else {
            throw new IllegalArgumentException("Order with ID " + orderID + " does not exist.");
        }
    }
    
        // Write file into text file
       private void updateCustomerDetailsInFile(String orderId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(customerDataFilePath))) {
            for (Map.Entry<String, HashMap<String, Object>> entry : customerDetails.entrySet()) {
                String id = entry.getKey();
                HashMap<String, Object> details = entry.getValue();
                writer.write(id.toLowerCase() + "," +
                        details.get("hostelName") + "," +
                        details.get("address") + "," +
                        details.get("technicianUsername") + "," +
                        details.get("machineDetails") + "," +
                        details.get("appointmentDate") + "," +
                        details.get("additionalNotes") + "," +
                        details.get("servicePrice") + "," +
                        details.get("jobStatus") + "," +
                        details.get("paymentStatus") + "," +
                        details.get("feedback"));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating customer details in file: " + e.getMessage());
        }
    }

    public List<Map.Entry<String, HashMap<String, Object>>> upcomingAppointmentForTechnician() {
        String currentTechnician = userProfileInfo.getUsername();
        List<Map.Entry<String, HashMap<String, Object>>> filteredOrders = new ArrayList<>();

        for (Map.Entry<String, HashMap<String, Object>> entry : customerDetails.entrySet()) {
            String technicianUsername = (String) entry.getValue().get("technicianUsername");
            if (technicianUsername.equalsIgnoreCase(currentTechnician)) {
                filteredOrders.add(entry);
            }
        }
        
        // Sort the orders by appointment date
        filteredOrders.sort(Comparator.comparing(entry -> (String) entry.getValue().get("appointmentDate")));

        return filteredOrders;
    }
    
    public Map<String, Map<String, Object>> getWaitingForFeedbackList() {
        Map<String, Map<String, Object>> filteredOrders = new HashMap<>();
        String currentTechnician = UserProfileInfo.getUsername(); 

        for (Map.Entry<String, HashMap<String, Object>> entry : customerDetails.entrySet()) {
            Map<String, Object> details = entry.getValue();
            String jobStatus = (String) details.get("jobStatus");
            String paymentStatus = (String) details.get("paymentStatus");
            String technicianUsername = (String) details.get("technicianUsername");

            if ("completed".equalsIgnoreCase(jobStatus) && "paid".equalsIgnoreCase(paymentStatus)
                    && technicianUsername.equalsIgnoreCase(currentTechnician)) {
                // Copy required details to a new HashMap
                Map<String, Object> filteredDetails = new HashMap<>();
                filteredDetails.put("hostelName", details.get("hostelName"));
                filteredDetails.put("machineDetails", details.get("machineDetails"));
                filteredDetails.put("appointmentDate", details.get("appointmentDate"));
                filteredDetails.put("feedback", details.get("feedback")); 

                // Store filtered details with order ID as key in filteredOrders HashMap
                filteredOrders.put(entry.getKey(), filteredDetails);
            }
        }

        return filteredOrders;
    }
}
