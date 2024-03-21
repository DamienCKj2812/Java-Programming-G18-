package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class TechnicianUI implements ActionListener{
    
    UserProfileInfo userProfileInfo = new UserProfileInfo();
    String username = userProfileInfo.getUsername();
    String role = userProfileInfo.getRole();
    Technician technician = new Technician();
    List<Map.Entry<String, HashMap<String, Object>>> appointments = technician.upcomingAppointmentForTechnician();
    UserFileHandler userFileHandler = new UserFileHandler();
    RoleAuthUI roleAuthUI = new RoleAuthUI();
    CustomerDetailsFileHandler  customerDetailsFileHandler = new CustomerDetailsFileHandler();
    
    private JFrame frame;
    //Button and Label for current main page
    private JLabel usernameLabel;
    private JLabel roleLabel;
    private JLabel upcomingAppoinmentLabel;
    private JButton logOutButton;
    private JButton appointmentManagementButton;
    private JButton provideFeedbackButton;
   
    //Button, Label, Fields for hostel(customer) registration frame
    private JButton backToMainPageButton;
    
    //Label for feedbackManagement
    private JLabel feedbackManagementLabel;

    
    public void technicianMainPageUI() {
        // Create and configure the frame
        frame = new JFrame("Upcoming Appoinment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(815, 600); // Increased width to accommodate the table
        frame.setLayout(null); // Use null layout for manual positioning
        
        // container for the labels
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(640, 20, 150, 85); // x, y, width, height values
        infoPanel.setBackground(Color.decode("#E0E0E0")); // Set background color to grey
        infoPanel.setLayout(null); // Use null layout for manual positioning

        // UI components (labels)
        usernameLabel = new JLabel(username);
        roleLabel = new JLabel(role);

        // Set bounds for username label and role label within the panel
        usernameLabel.setBounds(25, 10, 100, 25); 
        roleLabel.setBounds(25, 40, 100, 25);

        // Add labels to the panel
        infoPanel.add(usernameLabel);
        infoPanel.add(roleLabel);

        // Add the panel to the frame
        frame.add(infoPanel);

        // Create a panel to act as the container for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(10, 20, 365, 85); // Set bounds for the panel
        buttonPanel.setBackground(Color.decode("#E0E0E0"));
        buttonPanel.setLayout(null); // Use null layout for manual positioning

        // UI components (buttons and logout button)
        appointmentManagementButton = new JButton("<html>Appoinment<br>Management</html>");
        provideFeedbackButton = new JButton("<html>Provide<br>Feedback</html>");
        logOutButton = new JButton("Logout");

        // Set bounds for buttons and logout button within the buttonPanel
        int buttonWidth = 100;
        int buttonHeight = 65;
        appointmentManagementButton.setBounds(10, 10, buttonWidth, buttonHeight);
        provideFeedbackButton.setBounds(130, 10, buttonWidth, buttonHeight);
        logOutButton.setBounds(250, 10, buttonWidth, buttonHeight);
        appointmentManagementButton.setFocusable(false);
        appointmentManagementButton.addActionListener(this);
        provideFeedbackButton.setFocusable(false);
        provideFeedbackButton.addActionListener(this);
        logOutButton.setFocusable(false);
        logOutButton.addActionListener(this);


        // Add buttons and logout button to the buttonPanel
        buttonPanel.add(appointmentManagementButton);
        buttonPanel.add(provideFeedbackButton);
        buttonPanel.add(logOutButton);

        // Add buttonPanel to the frame
        frame.add(buttonPanel);

        // Retrieve appointments and display them in the main frame
        List<Map.Entry<String, HashMap<String, Object>>> appointments = technician.upcomingAppointmentForTechnician();
        displayAppointmentsTableInMainFrame(appointments);

        // Make the frame visible
        frame.setVisible(true);
    }

    
    public void displayAppointmentsTableInMainFrame(List<Map.Entry<String, HashMap<String, Object>>> appointments) {
        // Create a DefaultTableModel to hold the data for the JTable
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        upcomingAppoinmentLabel = new JLabel("Upcoming Appointments (Double Click to show full detail in a cell)");

        // Define column names for the table
        model.addColumn("Order ID");
        model.addColumn("Hostel Name");
        model.addColumn("Address");
        model.addColumn("Machine Details");
        model.addColumn("Appointment Date");
        model.addColumn("Additional Notes");
        model.addColumn("Service Price");
        model.addColumn("Job Status");
        model.addColumn("Payment Status");

        // Add appointment details to the table model
        for (Map.Entry<String, HashMap<String, Object>> entry : appointments) {
            HashMap<String, Object> details = entry.getValue();
            model.addRow(new Object[]{
                entry.getKey(), // Order ID
                details.get("hostelName"),
                details.get("address"),
                details.get("machineDetails"),
                details.get("appointmentDate"),
                details.get("additionalNotes"),
                details.get("servicePrice"),
                details.get("jobStatus"),
                details.get("paymentStatus"),
            });
        }

        // Create a JTable with the model
        JTable table = new JTable(model);

        // Add a MouseListener to the table
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col >= 0) {
                    String cellValue = table.getModel().getValueAt(row, col).toString();
                    JOptionPane.showMessageDialog(null, cellValue);
                }
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Set bounds for the appointments management label
        upcomingAppoinmentLabel.setBounds(10, 130, 600, 25);
        scrollPane.setBounds(10, 160, 780, 350);

        // Add the label and scroll pane to the existing frame
        frame.add(upcomingAppoinmentLabel);
        frame.add(scrollPane);

        // Refresh the frame to reflect the changes
        frame.revalidate();
        frame.repaint();
    }

//-------------------------------------------------------------------------------------------------------------------------------------------
    
    public void appointmentManagementUI(){
        // Create and configure the frame
        frame = new JFrame("Upcoming Appointment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(815, 600); // Increased width to accommodate the table
        frame.setLayout(null); // Use null layout for manual positioning
        
        // Container for the labels
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(640, 20, 150, 85); // x, y, width, height values
        infoPanel.setBackground(Color.decode("#E0E0E0")); // Set background color to grey
        infoPanel.setLayout(null); // Use null layout for manual positioning

        // UI components (labels)
        usernameLabel = new JLabel(username);
        roleLabel = new JLabel(role);

        // Set bounds for username label and role label within the panel
        usernameLabel.setBounds(25, 10, 100, 25); 
        roleLabel.setBounds(25, 40, 100, 25);

        // Add labels to the panel
        infoPanel.add(usernameLabel);
        infoPanel.add(roleLabel);

        // Add the panel to the frame
        frame.add(infoPanel);

        // Create a panel to act as the container for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(10, 20, 365, 85); // Set bounds for the panel
        buttonPanel.setBackground(Color.decode("#E0E0E0"));
        buttonPanel.setLayout(null); // Use null layout for manual positioning

        // UI components (buttons and logout button)
        backToMainPageButton = new JButton("<html>Back To<br>Main Page</html>");
        provideFeedbackButton = new JButton("<html>Provide<br>Feedback</html>");
        logOutButton = new JButton("Logout");

        // Set bounds for buttons and logout button within the buttonPanel
        int buttonWidth = 100;
        int buttonHeight = 65;
        backToMainPageButton.setBounds(10, 10, buttonWidth, buttonHeight);
        provideFeedbackButton.setBounds(130, 10, buttonWidth, buttonHeight);
        logOutButton.setBounds(250, 10, buttonWidth, buttonHeight);
        backToMainPageButton.setFocusable(false);
        backToMainPageButton.addActionListener(this);
        provideFeedbackButton.setFocusable(false);
        provideFeedbackButton.addActionListener(this);
        logOutButton.setFocusable(false);
        logOutButton.addActionListener(this);

        // Add buttons and logout button to the buttonPanel
        buttonPanel.add(backToMainPageButton);
        buttonPanel.add(provideFeedbackButton);
        buttonPanel.add(logOutButton);

        // Add buttonPanel to the frame
        frame.add(buttonPanel);
        
        // Create JLabels for instructions
        JLabel instructionLabel1 = new JLabel("1) Verify that every decision you make is in accord with truth; if not, you will be held responsible.");
        JLabel instructionLabel2 = new JLabel("2) Double-click the cell to update the job and payment status.");
        JLabel instructionLabel3 = new JLabel("3) If you unintentionally change the status, please contact the administrator.");
        
        // Set bounds for instruction labels
        instructionLabel1.setBounds(10, 110, 800, 25);
        instructionLabel2.setBounds(10, 125, 800, 25);
        instructionLabel3.setBounds(10, 140, 800, 25);

        // Add instruction labels to the frame
        frame.add(instructionLabel1);
        frame.add(instructionLabel2);
        frame.add(instructionLabel3);
        
        // Create a custom table model
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class; // All columns will contain String values
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This will make the cell non-editable on click
            }
        };

        // Define column names for the table
        model.addColumn("Order ID");
        model.addColumn("Hostel Name");
        model.addColumn("Address");
        model.addColumn("Machine Details");
        model.addColumn("Appointment Date");
        model.addColumn("Additional Notes");
        model.addColumn("Service Price");
        model.addColumn("Job Status");
        model.addColumn("Payment Status");

        // Add appointment details to the table model
        for (Map.Entry<String, HashMap<String, Object>> entry : appointments) {
            HashMap<String, Object> details = entry.getValue();
            model.addRow(new Object[]{
                entry.getKey(), // Order ID
                details.get("hostelName"),
                details.get("address"),
                details.get("machineDetails"),
                details.get("appointmentDate"),
                details.get("additionalNotes"),
                details.get("servicePrice"),
                details.get("jobStatus"),
                details.get("paymentStatus")
            });
        }

        // Create a JTable with the custom model
        JTable table = new JTable(model);

        // Add a MouseListener to the table to handle double-click events
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());
                    if (row >= 0 && column >= 0) {
                        String columnName = table.getColumnName(column);
                        String cellValue = table.getValueAt(row, column).toString();
                        String orderId = table.getValueAt(row, 0).toString(); // Get Order ID

                        // If the cell value is already "Completed" or "Paid", don't perform any action
                        if ("completed".equals(cellValue) || "paid".equals(cellValue)) {
                            return;
                        }

                        if ("Job Status".equals(columnName) || "Payment Status".equals(columnName)) {
                            String confirmationMessage = "";
                            if ("Job Status".equals(columnName)) {
                                confirmationMessage = "Are you sure you want to update job status to 'COMPLETED' ?";
                            } else if ("Payment Status".equals(columnName)) {
                                confirmationMessage = "Are you sure you want to update payment status to 'PAID' ?";
                            }

                            if (confirmAction(confirmationMessage)) {
                                // Update the status based on orderId
                                if ("Job Status".equals(columnName)) {
                                    // Update job status
                                    if (technician.updateJobStatusToCompleted(orderId)) {
                                        // Disable the cell
                                        table.setValueAt("completed", row, column);
                                        table.setEnabled(false);
                                    }
                                } else if ("Payment Status".equals(columnName)) {
                                    // Update payment status
                                    if (technician.updatePaymentStatusToPaid(orderId)) {
                                        // Disable the cell
                                        table.setValueAt("paid", row, column);
                                        table.setEnabled(false);
                                    }
                                }
                            }
                        } else {
                            // Show a prompt with the cell value for other columns
                            JOptionPane.showMessageDialog(null, cellValue);
                        }
                    }
                }
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Set bounds for the scroll pane
        scrollPane.setBounds(10, 165, 780, 370);

        // Add the scroll pane to the frame
        frame.add(scrollPane);

        // Make the frame visible
        frame.setVisible(true);
    }

    // Method to display confirmation dialog
    private boolean confirmAction(String message) {
        int dialogResult = JOptionPane.showConfirmDialog(null, message, "Confirmation", JOptionPane.YES_NO_OPTION);
        return dialogResult == JOptionPane.YES_OPTION;
    }
     
//-------------------------------------------------------------------------------------------------------------------------------------------
    
    public void provideFeedbackUI() {
        // Create and configure the frame
        frame = new JFrame("Feedback Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(815, 600); // Increased width to accommodate the table
        frame.setLayout(null); // Use null layout for manual positioning

        // Container for the labels
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(640, 20, 150, 85); // x, y, width, height values
        infoPanel.setBackground(Color.decode("#E0E0E0")); // Set background color to grey
        infoPanel.setLayout(null); // Use null layout for manual positioning

        // UI components (labels)
        usernameLabel = new JLabel(username);
        roleLabel = new JLabel(role);

        // Set bounds for username label and role label within the panel
        usernameLabel.setBounds(25, 10, 100, 25); 
        roleLabel.setBounds(25, 40, 100, 25);

        // Add labels to the panel
        infoPanel.add(usernameLabel);
        infoPanel.add(roleLabel);

        // Add the panel to the frame
        frame.add(infoPanel);

        // Create a panel to act as the container for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(10, 20, 365, 85); // Set bounds for the panel
        buttonPanel.setBackground(Color.decode("#E0E0E0"));
        buttonPanel.setLayout(null); // Use null layout for manual positioning

        // UI components (buttons and logout button)
        backToMainPageButton = new JButton("<html>Back To<br>Main Page</html>");
        appointmentManagementButton = new JButton("<html>Appointment<br>Management</html>");
        logOutButton = new JButton("Logout");

        // Set bounds for buttons and logout button within the buttonPanel
        int buttonWidth = 100;
        int buttonHeight = 65;
        backToMainPageButton.setBounds(10, 10, buttonWidth, buttonHeight);
        appointmentManagementButton.setBounds(130, 10, buttonWidth, buttonHeight);
        logOutButton.setBounds(250, 10, buttonWidth, buttonHeight);
        backToMainPageButton.setFocusable(false);
        backToMainPageButton.addActionListener(this);
        appointmentManagementButton.setFocusable(false);
        appointmentManagementButton.addActionListener(this);
        logOutButton.setFocusable(false);
        logOutButton.addActionListener(this);

        // Add buttons and logout button to the buttonPanel
        buttonPanel.add(backToMainPageButton);
        buttonPanel.add(appointmentManagementButton);
        buttonPanel.add(logOutButton);

        // Add buttonPanel to the frame
        frame.add(buttonPanel);

        // Get the waiting for feedback list
        Map<String, Map<String, Object>> waitingForFeedbackList = technician.getWaitingForFeedbackList();

        // Create a custom table model
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        // Define column names for the table
        model.addColumn("Order ID");
        model.addColumn("Hostel Name");
        model.addColumn("Machine Details");
        model.addColumn("Appointment Date");
        model.addColumn("Feedback");

        // Add waiting for feedback details to the table model
        for (Map.Entry<String, Map<String, Object>> entry : waitingForFeedbackList.entrySet()) {
            String orderId = entry.getKey();
            Map<String, Object> details = entry.getValue();
            model.addRow(new Object[]{
                orderId,
                details.get("hostelName"),
                details.get("machineDetails"),
                details.get("appointmentDate"),
                details.get("feedback")
            });
        }
        
        JLabel feedbackLabel = new JLabel("Double-click on a cell under 'Feedback' column to edit/add feedback or check full text.");
        feedbackLabel.setBounds(10, 110, 780, 25);

        frame.add(feedbackLabel);
              
        JTable table = new JTable(model);
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Check if it's a double-click
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());
                    if (row >= 0 && column >= 0 && table.getColumnName(column).equals("Feedback")) {
                        String orderId = table.getValueAt(row, 0).toString(); // Get Order ID
                        String currentFeedback = table.getValueAt(row, column).toString(); // Get current feedback

                        // Custom button text
                        Object[] options = {"Yes", "No", "Check full feedback"};
                        int option = JOptionPane.showOptionDialog(frame,
                            "Edit/Provide the feedback?",
                            "Feedback Confirmation",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[2]);

                        switch (option) {
                            case 0: // "Yes" option
                                // Prompt user to insert feedback text
                                String newFeedback = JOptionPane.showInputDialog(frame, "Enter the feedback:", currentFeedback);
                                if (newFeedback != null) { // If user inserts feedback
                                    if (!newFeedback.isEmpty()) { // Check if feedback is not empty
                                        table.setValueAt(newFeedback, row, column);
                                        String storedFeedback = newFeedback;
                                        try {
                                            technician.updateFeedback(orderId, storedFeedback);                 
                                            JOptionPane.showMessageDialog(frame, "Feedback has been updated");
                                        } catch (IllegalArgumentException ex) {
                                            JOptionPane.showMessageDialog(frame, ex.getMessage());
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(frame, "The feedback cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                                break;
                            case 1: // "No" option
                                // Do nothing
                                break;
                            case 2: // "Check full feedback" option
                                // Show the full feedback
                                JOptionPane.showMessageDialog(frame, currentFeedback);
                                break;
                        }
                    }
                }
            }
        });


        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Set bounds for the scroll pane
        scrollPane.setBounds(10, 140, 780, 400);

        // Add the scroll pane to the frame
        frame.add(scrollPane);

        // Make the frame visible
        frame.setVisible(true);
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks here
        if (e.getSource() == backToMainPageButton) {
            frame.dispose();
            technicianMainPageUI();
        } else if (e.getSource() == provideFeedbackButton) {
            frame.dispose();
            provideFeedbackUI();
        } else if (e.getSource() == logOutButton) {
            frame.dispose();
            roleAuthUI.LoginUI();
        } else if (e.getSource() == appointmentManagementButton) {
            frame.dispose();
            appointmentManagementUI();
        }
        
    }
    
}
