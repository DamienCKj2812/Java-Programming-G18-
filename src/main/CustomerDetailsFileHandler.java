
package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerDetailsFileHandler extends CentreManager{
    
    public CustomerDetailsFileHandler(){
        super();
    }
    
    // Getter method for retrieving all details where feedback is not null
    public List<Map<String, Object>> getAllDetailsWithFeedback() {
        List<Map<String, Object>> detailsWithFeedback = new ArrayList<>();
        System.out.println(customerDetails);
        for (Map.Entry<String, HashMap<String, Object>> entry : customerDetails.entrySet()) {
            Object feedback = entry.getValue().get("feedback");
            if (feedback != null && !feedback.toString().equalsIgnoreCase("null")) {
                Map<String, Object> details = new HashMap<>();
                details.put("orderID", entry.getKey());
                details.put("hostelName", entry.getValue().get("hostelName"));
                details.put("address", entry.getValue().get("address"));
                details.put("technicianName", entry.getValue().get("technicianUsername"));
                details.put("machineDetails", entry.getValue().get("machineDetails"));
                details.put("appointmentTime", entry.getValue().get("appointmentDate"));
                details.put("servicePrice", entry.getValue().get("servicePrice"));
                details.put("feedback", feedback); // Use the original object
                detailsWithFeedback.add(details);
            }
        }
        System.out.println(detailsWithFeedback);
        return detailsWithFeedback;
 }
}
