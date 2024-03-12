package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;                


public class CentreManagerUI implements ActionListener{
        
    UserProfileInfo userProfileInfo = new UserProfileInfo();
    String username = userProfileInfo.getUsername();
    String role = userProfileInfo.getRole();
    CentreManager centreManager = new CentreManager();
    List<Map.Entry<String, HashMap<String, Object>>> appoinment = centreManager.viewAppointments();
    UserFileHandler userFileHandler = new UserFileHandler();
    RoleAuthUI roleAuthUI = new RoleAuthUI();
    CustomerDetailsFileHandler  customerDetailsFileHandler = new CustomerDetailsFileHandler();
    HostelDetailsHandler hostelDetailsHandler = new HostelDetailsHandler();
    
    private JFrame frame;
    //Button and Label for current main page
    private JLabel usernameLabel;
    private JLabel roleLabel;
    private JLabel appoinmentsManagementLabel;
    private JButton logOutButton;
    private JButton createNewAppoinmentButton;
    private JButton feedbackManagementButton;
    private JButton registerNewAccountButton;
    private JButton reportButton;
    private JButton manageTechinicianButton;
   
    //Button, Label, Fields for createNewAppoinment frame 
    private JComboBox<String> hostelNameDropdown;
    private JButton createOrderButton;
    private JButton clearButton;
    private JButton backToMainPageButton;
    private JLabel hostelNameLabel;
    private JLabel addressLabel;
    private JLabel technicianUsernameLabel;
    private JLabel machineDetailsLabel;
    private JLabel appoinmentDateLabel; 
    private JLabel additionalNotesLabel; 
    private JLabel servicePriceLabel;
    private JComboBox<String> technicianComboBox;
    private JTextField hostelNameField;
    private JTextField addressField;
    private JTextField machineDetailsField;
    private JFormattedTextField appoinmentDateField; // Use JFormattedTextField for date input
    private JTextArea additionalNotesField; // Use JTextArea for multiline text input
    private JTextField servicePriceField;

    
    //Button, Label, Fields for hostel(customer) registration frame
    private JButton registerButton;
   
    
    //Label for feedbackManagement
    private JLabel feedbackManagementLabel;
    
    
    

    // initialize technicianComboBox inside a separate initialization method
    public void initializeTechnicianComboBox() {
        List<String> technicianUsernames = userFileHandler.getTechnicianUsernames();
        
        if (technicianUsernames.isEmpty()) {
            technicianComboBox = null;
            return;
        }
        
        String[] technicianNamesArray = technicianUsernames.toArray(new String[0]);
        technicianComboBox = new JComboBox<>(technicianNamesArray);
    }
    
  
    
    public void centreManagerMainPageUI() {
        // Create and configure the frame
        frame = new JFrame("Appointment Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 600); // Increased width to accommodate the table
        frame.setLayout(null); // Use null layout for manual positioning
        
        // container for the labels
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(900, 20, 150, 85); // x, y, width, height values
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
        buttonPanel.setBounds(10, 20, 730, 85); // Set bounds for the panel
        buttonPanel.setBackground(Color.decode("#E0E0E0"));
        buttonPanel.setLayout(null); // Use null layout for manual positioning

        // UI components (buttons and logout button)
        createNewAppoinmentButton = new JButton("<html>Create New<br>Appointment</html>");
        feedbackManagementButton = new JButton("<html>Feedback<br>Management</html>");
        registerNewAccountButton = new JButton("<html>Register<br>New Account</html>");
        reportButton = new JButton("<html>Monthly<br>Report</html>");
        manageTechinicianButton = new JButton("<html>Manage<br>Technician</html>");
        logOutButton = new JButton("Logout");

        // Set bounds for buttons and logout button within the buttonPanel
        int buttonWidth = 100;
        int buttonHeight = 65;
        createNewAppoinmentButton.setBounds(10, 10, buttonWidth, buttonHeight);
        feedbackManagementButton.setBounds(130, 10, buttonWidth, buttonHeight);
        registerNewAccountButton.setBounds(250, 10, buttonWidth, buttonHeight);
        reportButton.setBounds(370, 10, buttonWidth, buttonHeight);
        manageTechinicianButton.setBounds(490, 10, buttonWidth, buttonHeight);
        logOutButton.setBounds(610, 10, buttonWidth, buttonHeight);
        createNewAppoinmentButton.setFocusable(false);
        createNewAppoinmentButton.addActionListener(this);
        feedbackManagementButton.setFocusable(false);
        feedbackManagementButton.addActionListener(this);
        registerNewAccountButton.setFocusable(false);
        registerNewAccountButton.addActionListener(this);
        reportButton.setFocusable(false);
        reportButton.addActionListener(this);
        manageTechinicianButton.setFocusable(false);
        manageTechinicianButton.addActionListener(this);
        logOutButton.setFocusable(false);
        logOutButton.addActionListener(this);


        // Add buttons and logout button to the buttonPanel
        buttonPanel.add(createNewAppoinmentButton);
        buttonPanel.add(feedbackManagementButton);
        buttonPanel.add(registerNewAccountButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(manageTechinicianButton);
        buttonPanel.add(logOutButton);

        // Add buttonPanel to the frame
        frame.add(buttonPanel);

        // Retrieve appointments and display them in the main frame
        List<Map.Entry<String, HashMap<String, Object>>> appointments = centreManager.viewAppointments();
        displayAppointmentsTableInMainFrame(appointments);

        // Make the frame visible
        frame.setVisible(true);
    }   

    
    public void displayAppointmentsTableInMainFrame(List<Map.Entry<String, HashMap<String, Object>>> appointments) {
        // Create a DefaultTableModel to hold the data for the JTable
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells to be non-editable
                return false;
            }
        };

        appoinmentsManagementLabel = new JLabel("Appointments Management (Double Click to show full detail in a cell)");

        // Define column names for the table
        model.addColumn("Order ID");
        model.addColumn("Hostel Name");
        model.addColumn("Address");
        model.addColumn("Technician");
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
                details.get("technicianUsername"),
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
        appoinmentsManagementLabel.setBounds(10, 130, 860, 25); 
        scrollPane.setBounds(10, 160, 1040, 350); 

        // Add the label and scroll pane to the existing frame
        frame.add(appoinmentsManagementLabel);
        frame.add(scrollPane);

        // Refresh the frame to reflect the changes
        frame.revalidate();
        frame.repaint();
    }
    
    public void createNewAppointmentUI() {
        // Create and configure the frame
        frame = new JFrame("Create New Appointment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Increased width to accommodate the table
        frame.setLayout(null); // Use null layout for manual positioning

        // container for the labels
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(10, 20, 150, 465); // x, y, width, height values
        infoPanel.setBackground(Color.decode("#E0E0E0")); // Set background color to grey
        infoPanel.setLayout(null);

        // UI components (labels)
        hostelNameLabel = new JLabel("Hostel Name:");
        addressLabel = new JLabel("Address:");
        technicianUsernameLabel = new JLabel("Technician Username:");
        machineDetailsLabel = new JLabel("Machine Details:");
        appoinmentDateLabel = new JLabel("Appointment Date:");
        additionalNotesLabel = new JLabel("Additional Notes:");
        servicePriceLabel = new JLabel("Service Price:");

        // Set bounds for labels
        int labelX = 10;
        int labelY = 15;
        int labelWidth = 130;
        int labelHeight = 25;
        hostelNameLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        addressLabel.setBounds(labelX, labelY + 60, labelWidth, labelHeight);
        technicianUsernameLabel.setBounds(labelX, labelY + 120, labelWidth, labelHeight);
        machineDetailsLabel.setBounds(labelX, labelY + 175, labelWidth, labelHeight);
        appoinmentDateLabel.setBounds(labelX, labelY + 235, labelWidth, labelHeight);
        additionalNotesLabel.setBounds(labelX, labelY + 290, labelWidth, labelHeight);
        servicePriceLabel.setBounds(labelX, labelY + 390, labelWidth, labelHeight);

        // Add labels to the panel
        infoPanel.add(hostelNameLabel);
        infoPanel.add(addressLabel);
        infoPanel.add(technicianUsernameLabel);
        infoPanel.add(machineDetailsLabel);
        infoPanel.add(appoinmentDateLabel);
        infoPanel.add(additionalNotesLabel);
        infoPanel.add(servicePriceLabel);

        // Create a panel to act as the container for the fields and buttons
        JPanel formPanel = new JPanel();
        formPanel.setBounds(10, 20, 615, 465); // Set bounds for the panel
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null); // Use null layout for manual positioning

        // Initialize the hostelNameDropdown JComboBox
        
        Set<String> hostelNamesSet = hostelDetailsHandler.getHostelNames();
        List<String> hostelNamesList = new ArrayList<>(hostelNamesSet);

        hostelNameDropdown = new JComboBox<>(hostelNamesList.toArray(new String[0]));

        // ActionListener to update the addressField when hostel selection changes
        hostelNameDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedHostel = (String) hostelNameDropdown.getSelectedItem();
                String hostelAddress = hostelDetailsHandler.getHostelAddress(selectedHostel);
                addressField.setText(hostelAddress);
            }
        });
        
        
        try {
            MaskFormatter dateFormatter = new MaskFormatter("####-##-##");
            appoinmentDateField = new JFormattedTextField(dateFormatter);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(frame, "Invalid date format. Please enter the date in the format YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); 
        }
        
        addressField = new JTextField();
        addressField.setEditable(false);
        machineDetailsField = new JTextField();
        additionalNotesField = new JTextArea(); // Use JTextArea for multiline text input
        additionalNotesField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        initializeTechnicianComboBox(); // Call the method to initialize the combo box
        servicePriceField = new JTextField(10);
        
        servicePriceField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') || c == '.' ||
                   (c == KeyEvent.VK_BACK_SPACE) ||
                   (c == KeyEvent.VK_DELETE))) {
                    e.consume();  // ignore the event
                }
            }
        });


        // Set bounds for fields and buttons within the formPanel
        int fieldX = 200;
        int fieldY = 15;
        int fieldWidth = 400;
        int fieldHeight = 25;
        hostelNameDropdown.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        addressField.setBounds(fieldX, fieldY + 60, fieldWidth, fieldHeight);
        machineDetailsField.setBounds(fieldX, fieldY + 175, fieldWidth, fieldHeight);
        appoinmentDateField.setBounds(fieldX, fieldY + 235, fieldWidth, fieldHeight);
        additionalNotesField.setBounds(fieldX, fieldY + 290, fieldWidth, 75);
        servicePriceField.setBounds(fieldX, fieldY + 390, fieldWidth, 25);
        
        if (technicianComboBox != null) {
            technicianComboBox.setBounds(fieldX, fieldY + 120, 400, fieldHeight);
            formPanel.add(technicianComboBox); // Add the combo box to the form panel
        }else {
            JLabel noTechnicianLabel = new JLabel("  No technicians available");
            noTechnicianLabel.setBorder(BorderFactory.createLineBorder(Color.RED)); 
            noTechnicianLabel.setBounds(fieldX, fieldY + 120, 400, fieldHeight);
            formPanel.add(noTechnicianLabel);
        }
        
        // Add components to the formPanel
        formPanel.add(hostelNameDropdown);
        formPanel.add(addressField);
        formPanel.add(machineDetailsField);
        formPanel.add(appoinmentDateField);
        formPanel.add(additionalNotesField);
        formPanel.add(servicePriceField);


        // Create a panel to act as the container for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(10, 500, 615, 50); // Set bounds for the panel
        buttonPanel.setBackground(Color.decode("#E0E0E0"));
        buttonPanel.setLayout(null); // Use null layout for manual positioning
        // UI components (buttons)
        createOrderButton = new JButton("Create Order");
        clearButton = new JButton("Clear");
        backToMainPageButton = new JButton("<html>Back To<br>Main Page</html>");
        feedbackManagementButton = new JButton("<html>Feedback<br>Management</html>");
        registerNewAccountButton = new JButton("<html>Register<br>New Account</html>");
        reportButton = new JButton("<html>Monthly<br>Report</html>");
        manageTechinicianButton = new JButton("<html>Manage<br>Technician</html>");
        logOutButton = new JButton("Logout");
        createOrderButton.setFocusable(false);
        clearButton.setFocusable(false);
        backToMainPageButton.setFocusable(false);
        feedbackManagementButton.setFocusable(false);
        registerNewAccountButton.setFocusable(false);
        reportButton.setFocusable(false);
        manageTechinicianButton.setFocusable(false);
        logOutButton.setFocusable(false);

        // Set bounds for buttons within the buttonPanel
        createOrderButton.setBounds(10, 10, 120, 35);
        clearButton.setBounds(160, 10, 120, 35);
        backToMainPageButton.setBounds(650, 30, 120, 40);
        feedbackManagementButton.setBounds(650, 90, 120, 40);
        registerNewAccountButton.setBounds(650, 150, 120, 40);
        reportButton.setBounds(650, 210, 120, 40);
        manageTechinicianButton.setBounds(650, 270, 120, 40);
        logOutButton.setBounds(650, 320, 120, 40);

        // Add action listeners to the buttons
        createOrderButton.addActionListener(this);
        clearButton.addActionListener(this);
        backToMainPageButton.addActionListener(this); 
        feedbackManagementButton.addActionListener(this);
        registerNewAccountButton.addActionListener(this);
        reportButton.addActionListener(this);
        manageTechinicianButton.addActionListener(this);
        logOutButton.addActionListener(this);

        // Add buttons to the buttonPanel
        frame.add(backToMainPageButton);
        frame.add(feedbackManagementButton);
        frame.add(registerNewAccountButton);
        frame.add(reportButton);
        frame.add(manageTechinicianButton);
        frame.add(logOutButton);
        buttonPanel.add(createOrderButton);
        buttonPanel.add(clearButton);

        // Add panels to the frame
        frame.add(infoPanel);
        frame.add(formPanel);
        frame.add(buttonPanel);

        // Make the frame visible
        frame.setVisible(true);
    }
    
    public void createNewAccountForCustomer(){
        // Create and configure the frame
        frame = new JFrame("Register new customer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Increased width to accommodate the table
        frame.setLayout(null); // Use null layout for manual positioning
        
        // container for the labels
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(10, 20, 150, 465); // x, y, width, height values
        infoPanel.setBackground(Color.decode("#E0E0E0")); // Set background color to grey
        infoPanel.setLayout(null);

        // UI components (labels)
        hostelNameLabel = new JLabel("Hostel Name:");
        addressLabel = new JLabel("Address:");

        // Set bounds for labels
        int labelX = 10;
        int labelY = 15;
        int labelWidth = 130;
        int labelHeight = 25;
        hostelNameLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        addressLabel.setBounds(labelX, labelY + 60, labelWidth, labelHeight);

        // Add labels to the panel
        infoPanel.add(hostelNameLabel);
        infoPanel.add(addressLabel);

        // Create a panel to act as the container for the fields and buttons
        JPanel formPanel = new JPanel();
        formPanel.setBounds(10, 20, 615, 465); // Set bounds for the panel
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null); // Use null layout for manual positioning

        // UI components (fields and buttons)
        hostelNameField = new JTextField();
        addressField = new JTextField();
        machineDetailsField = new JTextField();

        // Set bounds for fields and buttons within the formPanel
        int fieldX = 200;
        int fieldY = 15;
        int fieldWidth = 400;
        int fieldHeight = 25;
        hostelNameField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        addressField.setBounds(fieldX, fieldY + 60, fieldWidth, fieldHeight);
 
        // Add components to the formPanel
        formPanel.add(hostelNameField);
        formPanel.add(addressField);

        // Create a panel to act as the container for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(10, 500, 615, 50); // Set bounds for the panel
        buttonPanel.setBackground(Color.decode("#E0E0E0"));
        buttonPanel.setLayout(null); // Use null layout for manual positioning

        // UI components (buttons)
        registerButton = new JButton("<html>Register<br>account</html>");
        clearButton = new JButton("Clear");
        backToMainPageButton = new JButton("<html>Back To<br>Main Page</html>");
        feedbackManagementButton = new JButton("<html>Feedback<br>Management</html>");
        createNewAppoinmentButton = new JButton("<html>Create New<br>Appointment</html>");
        reportButton = new JButton("<html>Monthly<br>Report</html>");
        manageTechinicianButton = new JButton("<html>Manage<br>Technician</html>");
        logOutButton = new JButton("Logout");
        registerButton.setFocusable(false);
        clearButton.setFocusable(false);
        backToMainPageButton.setFocusable(false);
        feedbackManagementButton.setFocusable(false);
        createNewAppoinmentButton.setFocusable(false);
        reportButton.setFocusable(false);
        manageTechinicianButton.setFocusable(false);
        logOutButton.setFocusable(false);

        // Set bounds for buttons within the buttonPanel
        registerButton.setBounds(10, 10, 120, 35);
        clearButton.setBounds(160, 10, 120, 35);
        backToMainPageButton.setBounds(650, 30, 120, 40);
        feedbackManagementButton.setBounds(650, 90, 120, 40);
        createNewAppoinmentButton.setBounds(650, 150, 120, 40);
        reportButton.setBounds(650, 210, 120, 40);
        manageTechinicianButton.setBounds(650, 270, 120, 40);
        logOutButton.setBounds(650, 320, 120, 40);

        // Add action listeners to the buttons
        registerButton.addActionListener(this);
        clearButton.addActionListener(this);
        backToMainPageButton.addActionListener(this); 
        feedbackManagementButton.addActionListener(this);
        createNewAppoinmentButton.addActionListener(this);
        reportButton.addActionListener(this);
        manageTechinicianButton.addActionListener(this);
        logOutButton.addActionListener(this);

        // Add buttons to the buttonPanel
        frame.add(backToMainPageButton);
        frame.add(feedbackManagementButton);
        frame.add(createNewAppoinmentButton);
        frame.add(logOutButton);
        frame.add(reportButton);
        frame.add(manageTechinicianButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);

        // Add panels to the frame
        frame.add(infoPanel);
        frame.add(formPanel);
        frame.add(buttonPanel);

        // Make the frame visible
        frame.setVisible(true);
    }
   

    public void feedbackManagementUI(){
        // Create and configure the frame
        frame = new JFrame("Feedback Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 600); 
        frame.setLayout(null); // Use null layout for manual positioning
        
        // container for the labels
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(900, 20, 150, 85); // x, y, width, height values
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
         // Create a panel to act as the container for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(10, 20, 730, 85); // Set bounds for the panel
        buttonPanel.setBackground(Color.decode("#E0E0E0"));
        buttonPanel.setLayout(null); // Use null layout for manual positioning

        // UI components (buttons and logout button)
        createNewAppoinmentButton = new JButton("<html>Create New<br>Appointment</html>");
        backToMainPageButton = new JButton("<html>Back To<br>Main Page</html>");
        registerNewAccountButton = new JButton("<html>Register<br>New Account</html>");
        reportButton = new JButton("<html>Monthly<br>Report</html>");
        manageTechinicianButton = new JButton("<html>Manage<br>Technician</html>");
        logOutButton = new JButton("Logout");

        // Set bounds for buttons and logout button within the buttonPanel
        int buttonWidth = 100;
        int buttonHeight = 65;
        backToMainPageButton.setBounds(10, 10, buttonWidth, buttonHeight);
        createNewAppoinmentButton.setBounds(130, 10, buttonWidth, buttonHeight);
        registerNewAccountButton.setBounds(250, 10, buttonWidth, buttonHeight);
        reportButton.setBounds(370, 10, buttonWidth, buttonHeight);
        manageTechinicianButton.setBounds(490, 10, buttonWidth, buttonHeight);
        logOutButton.setBounds(610, 10, buttonWidth, buttonHeight);
        createNewAppoinmentButton.setFocusable(false);
        createNewAppoinmentButton.addActionListener(this);
        backToMainPageButton.setFocusable(false);
        backToMainPageButton.addActionListener(this);
        registerNewAccountButton.setFocusable(false);
        registerNewAccountButton.addActionListener(this);
        reportButton.setFocusable(false);
        reportButton.addActionListener(this);
        manageTechinicianButton.setFocusable(false);
        manageTechinicianButton.setFocusable(false);
        logOutButton.setFocusable(false);
        logOutButton.addActionListener(this);


        // Add buttons and logout button to the buttonPanel
        buttonPanel.add(createNewAppoinmentButton);
        buttonPanel.add(backToMainPageButton);
        buttonPanel.add(registerNewAccountButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(manageTechinicianButton);
        buttonPanel.add(logOutButton);
        
        // Retrieve all details with feedback
        List<Map<String, Object>> detailsWithFeedback = customerDetailsFileHandler.getAllDetailsWithFeedback();
        displayFeedbackTableInMainFrame(detailsWithFeedback);

        // Add buttonPanel to the frame
        frame.add(buttonPanel);
        frame.setVisible(true);
    }
    
    public void displayFeedbackTableInMainFrame(List<Map<String, Object>> feedbacks) {
        // Create a DefaultTableModel to hold the data for the JTable
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        // Define column names for the table
        model.addColumn("Order ID");
        model.addColumn("Hostel Name");
        model.addColumn("Address");
        model.addColumn("Technician");
        model.addColumn("Machine Details");
        model.addColumn("Appointment Time");
        model.addColumn("Service Price");
        model.addColumn("Feedback");
        
        feedbackManagementLabel = new JLabel("Feedback Management (Double Click to show full detail in a cell)");

        // Add feedback details to the table model
        for (Map<String, Object> feedback : feedbacks) {
            model.addRow(new Object[]{
                    feedback.get("orderID"),
                    feedback.get("hostelName"),
                    feedback.get("address"),
                    feedback.get("technicianName"),
                    feedback.get("machineDetails"),
                    feedback.get("appointmentTime"),
                    feedback.get("servicePrice"),
                    feedback.get("feedback")
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
        
        appoinmentsManagementLabel.setBounds(10, 130, 860, 25); 
        scrollPane.setBounds(10, 160, 1040, 350); 
        
        frame.add(feedbackManagementLabel);
        frame.add(scrollPane);
        frame.revalidate();
        frame.repaint();
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hostelNameDropdown) {
            String selectedHostel = (String) hostelNameDropdown.getSelectedItem();
            String address = hostelDetailsHandler.getHostelAddress(selectedHostel);
            addressField.setText(address);
            frame.revalidate();
            frame.repaint();
        } else if (e.getSource() == createNewAppoinmentButton) {
            try {
                frame.dispose();
                createNewAppointmentUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error occurred while creating a new appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                centreManagerMainPageUI();
            }
        } else if (e.getSource() == feedbackManagementButton) {
            frame.dispose();
            feedbackManagementUI();
        } else if (e.getSource() == registerNewAccountButton){
            frame.dispose();
            createNewAccountForCustomer();
        }else if (e.getSource() == logOutButton) {
            frame.dispose();
            roleAuthUI.LoginUI();
        } else if (e.getSource() == backToMainPageButton) {
            frame.dispose();
            centreManagerMainPageUI();
        }   else if (e.getSource() == clearButton) {
            // Clear all text fields
            hostelNameField.setText("");
            addressField.setText("");
            machineDetailsField.setText("");
            appoinmentDateField.setText("");
            additionalNotesField.setText("");
        }  else if (e.getSource() == createOrderButton) {
            // Retrieve text field values
            String hostelName = (String) hostelNameDropdown.getSelectedItem();
            String address = addressField.getText().replace(",", " "); 
            String machineDetails = machineDetailsField.getText().replace(",", " "); 
            String appointmentDate = appoinmentDateField.getText().replace(",", " "); 
            String additionalNotes = additionalNotesField.getText().replace(",", " "); 
            String selectedTechnician = (String) technicianComboBox.getSelectedItem();
            String servicePrice = servicePriceField.getText();
            
            if (hostelName.isEmpty() || address.isEmpty() || machineDetails.isEmpty() || appointmentDate.isEmpty() || servicePrice.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop processing if any field is empty
            }
            
            if (technicianComboBox == null) {
                JOptionPane.showMessageDialog(frame, "No technicians available. Please contact the administrator.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                String orderID = centreManager.registerCustomer(hostelName, address, selectedTechnician, machineDetails, appointmentDate, additionalNotes, servicePrice);
                JOptionPane.showMessageDialog(frame, "Order created successfully. Your order ID is: " + orderID, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                // Show error message if there is a date parsing error
                JOptionPane.showMessageDialog(frame, "Error parsing appointment date. Please enter the date in the format YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // Show error message for other exceptions
                JOptionPane.showMessageDialog(frame, "An error occurred while registering the customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }   
        } else if (e.getSource() == registerButton) {
            String hostelName = hostelNameField.getText();
            String address = addressField.getText();

            // Validate hostelName and address
            if (hostelName.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in both hostel name and address fields", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    hostelDetailsHandler.writeToFile(hostelName, address);
                    JOptionPane.showMessageDialog(frame, "Account successfully added", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }   
}
