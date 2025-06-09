// MotorPH Employee Application Portal Main Class

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import model.EmployeeDatabase;
import view.EmployeeMainFrame;

public class Main {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Load employees data
            EmployeeDatabase.loadEmployees();
            
            // Start the application
            SwingUtilities.invokeLater(() -> {
                try {
                    EmployeeMainFrame mainFrame = new EmployeeMainFrame();
                    mainFrame.setVisible(true);
                } catch (Exception e) {
                    showError("Failed to start application: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showError("Failed to initialize application: " + e.getMessage());
        }
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
} 