package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class RoleAuthUI implements ActionListener {
    RoleAuth roleAuth = new RoleAuth();
    
    private String loggedInUsername;
    private String loggedInRole;
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    private JButton signUpButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel rolesLabel;
    private JLabel messageLabel;
    private JLabel signUpLabel; 
    private JLabel loginLabel; 
    JRadioButton centreManagerRB = new JRadioButton("Centre Manager");
    JRadioButton technicianRB = new JRadioButton("Technician");
    
    
    public void LoginUI() {
        // Create and configure the frame
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null); // Use null layout for manual positioning


        // Create UI components
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        resetButton = new JButton("Reset");
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");   
        messageLabel = new JLabel();
        signUpLabel = new JLabel("<html><u>Sign Up here</u></html>"); // JLabel styled like a hyperlink

        // Set bounds for labels
        usernameLabel.setBounds(50, 100, 75, 25); //x position, y position, width, height
        passwordLabel.setBounds(50, 150, 75, 25);
        messageLabel.setBounds(65, 50, 300, 35);
        usernameField.setBounds(125, 100, 200, 25);
        passwordField.setBounds(125, 150, 200, 25);
        loginButton.setBounds(125, 200, 100, 25);
        resetButton.setBounds(225, 200, 100, 25);
        signUpLabel.setBounds(180, 250, 100, 25);

        
        messageLabel.setFont(new Font(null, Font.ITALIC, 18));
        messageLabel.setForeground(Color.GREEN);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);
        
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.setVisible(true);
        
        // Add ActionListener to the "Sign Up here" label
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                signUpLabel.setForeground(Color.BLUE);
                frame.dispose();// Dispose of the current frame
                SignUpUI();
            }
        });


        // Add components to the frame
        frame.add(usernameLabel);
        frame.add(passwordLabel);
        frame.add(messageLabel);
        frame.add(usernameField);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.add(signUpLabel);
    }
    
//--------------- Sign  Up -------------------------------------------------------------------------------------
    
    public void SignUpUI(){
        // Create and configure the frame
        frame = new JFrame("Sign Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null); // Use null layout for manual positioning
        

        // Create UI components
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        signUpButton = new JButton("Sign Up");
        resetButton = new JButton("Reset");
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");   
        rolesLabel = new JLabel("Roles:");
        loginLabel = new JLabel("<html><u>Login here</u></html>"); // JLabel styled like a hyperlink
        
        // Add radio buttons to a button group
        ButtonGroup roleButtonGroup = new ButtonGroup();
        roleButtonGroup.add(technicianRB);
    
        // Set bounds for labels
        usernameLabel.setBounds(50, 100, 75, 25); //x position, y position, width, height
        passwordLabel.setBounds(50, 150, 75, 25);
        rolesLabel.setBounds(50, 200, 75, 25);
        technicianRB.setBounds(125, 200, 100, 25);
        usernameField.setBounds(125, 100, 200, 25);
        passwordField.setBounds(125, 150, 200, 25);
        signUpButton.setBounds(125, 240, 100, 25);
        resetButton.setBounds(225, 240, 100, 25);
        loginLabel.setBounds(180, 280, 100, 25);
        
        messageLabel.setForeground(Color.GREEN);
        signUpButton.setFocusable(false);
        signUpButton.addActionListener(this);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);
        
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        frame.setVisible(true);
        // Add ActionListener to the "Sign Up here" label
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginLabel.setForeground(Color.BLUE);
                frame.dispose();// Dispose of the current frame
                LoginUI();
            }
        });


        // Add components to the frame
        frame.add(usernameLabel);
        frame.add(passwordLabel);
        frame.add(rolesLabel);
        frame.add(messageLabel);
        frame.add(technicianRB);
        frame.add(usernameField);
        frame.add(passwordField);
        frame.add(signUpButton);
        frame.add(resetButton);
        frame.add(loginLabel);
        
    }


//--------------- Submit Handler -------------------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            usernameField.setText("");
            passwordField.setText("");
        }
        
        else if (e.getSource() == signUpButton) {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            String selectedRole = "";
            if (technicianRB.isSelected()) {
                selectedRole = "technician";
            } 

            if (username.isEmpty() || password.isEmpty() || selectedRole.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username, password, or role cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit method if any field is empty
            }

            if (username.contains(" ")) {
                JOptionPane.showMessageDialog(frame, "No spaces allowed in the username", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.contains(" ")) {
                JOptionPane.showMessageDialog(frame, "No spaces allowed in the password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                roleAuth.signUpRole(username, password, selectedRole);
                JOptionPane.showMessageDialog(frame, "Sign up success!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException err) {
                JOptionPane.showMessageDialog(frame, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
       else if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username or password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit method if either is empty
            }

            String role = roleAuth.signInRole(username, password);

            if (role != null) {
                loggedInUsername = username;
                loggedInRole = role;

                // Set the username and role in the UserProfileInfo class after successful login
                UserProfileInfo.setUsername(username);
                UserProfileInfo.setRole(role);
                
                if (loggedInRole.equals("centre manager")) {
                    CentreManagerUI centreManagerUI = new CentreManagerUI();
                    centreManagerUI.centreManagerMainPageUI();
                    frame.dispose();
                } else if (loggedInRole.equals("technician")) {
                    TechnicianUI technicianUI = new TechnicianUI();
                    technicianUI.technicianMainPageUI();
                    frame.dispose();
                } else if (loggedInRole.equals("technician(b)")) {
                    JOptionPane.showMessageDialog(frame, "Your account has been banned by the center manager.\nPlease contact the center manager for further information.", "Account Banned", JOptionPane.ERROR_MESSAGE);
                }
            } 
            else {
                JOptionPane.showMessageDialog(frame, "User not found or password is wrong", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
