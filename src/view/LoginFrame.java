package view;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.*;
import model.EmployeeDatabase; // Added for loading data
// view.EmployeeMainFrame import removed as it's in the same package

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JLabel statusLabel;

    public LoginFrame() {
        setTitle("MotorPH Employee App Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label and Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        // Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        add(loginButton, gbc);

        // Status Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        add(statusLabel, gbc);

        loginButton.addActionListener(_ -> authenticateUser());
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String csvFile = Paths.get("src", "resources", "users.csv").toString();
        String line; // Declare line here
        boolean loggedIn = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header line
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] user = line.split(","); // Use string literal for delimiter
                if (user.length == 2 && user[0].equals(username) && user[1].equals(password)) {
                    loggedIn = true;
                    break;
                }
            }
        } catch (IOException ex) {
            // Log the exception for debugging, but show a user-friendly message
            System.err.println("Error reading user data file: " + csvFile + " - " + ex.getMessage());
            statusLabel.setText("Error: Could not access user data.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        if (loggedIn) {
            statusLabel.setText("Login Successful!");
            statusLabel.setForeground(Color.GREEN);
            // Proceed to the main application
            // For now, just close the login window
            // In the next step, we'll open the main app window
            // Proceed to the main application
            dispose(); // Close the login window
            SwingUtilities.invokeLater(() -> {
                try {
                    // Load employees data
                    EmployeeDatabase.loadEmployees(); 
                    // Start the main application
                    System.out.println("DEBUG: Attempting to create EmployeeMainFrame."); // Added for console feedback
                    EmployeeMainFrame mainFrame = new EmployeeMainFrame();
                    mainFrame.setVisible(true);
                } catch (Exception ex) {
                    // Log the exception for debugging
                    System.err.println("Failed to start main application: " + ex.getMessage());
                    // Show error message to the user (e.g., in a dialog)
                    JOptionPane.showMessageDialog(null, 
                        "Failed to start the main application: " + ex.getMessage(), 
                        "Application Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
        } else {
            statusLabel.setText("Invalid username or password.");
            statusLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
