//
// MotorPH Employee Application Portal Main Class
//

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.LoginFrame; // Added import for LoginFrame

public class Main {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Start the login process
            SwingUtilities.invokeLater(() -> {
                try {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                } catch (Exception e) {
                    showError("Failed to start login screen: " + e.getMessage());
                }
            });
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {
            showError("Failed to set Look and Feel: " + e.getMessage());
        } catch (Exception e) { // Catch any other unexpected errors during initialization
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