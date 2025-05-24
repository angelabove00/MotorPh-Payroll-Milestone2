package view;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
import javax.swing.border.*;
import model.Attendance;

/**
 * Window for entering time in/out.
 * Format should be HH:mm (like 08:30)
 */
public class AttendanceUI extends JDialog {
    // Colors and Fonts
    private static final Color BG_COLOR = new Color(240, 244, 248);  // #f0f4f8
    private static final Color BUTTON_COLOR = new Color(79, 109, 122);  // #4f6d7a
    private static final Font MAIN_FONT = new Font("SansSerif", Font.PLAIN, 14);
    
    private JTextField empNumField;
    private JTextField dateField;
    private JTextField timeInField;
    private JTextField timeOutField;
    private JButton submitBtn;
    
    private Attendance attendance;
    
    public AttendanceUI(JFrame parent) {
        super(parent, "Record Attendance", true);
        attendance = new Attendance();
        setupUI();
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void setupUI() {
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Create panels for each section
        JPanel employeePanel = createEmployeePanel();
        JPanel timePanel = createTimePanel();
        
        // Combine sections vertically
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        formPanel.setBackground(BG_COLOR);
        formPanel.add(employeePanel);
        formPanel.add(timePanel);
        
        // Submit button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 5, 0));
        submitBtn = createStyledButton("Submit");
        submitBtn.addActionListener(e -> handleSubmit());
        buttonPanel.add(submitBtn);
        
        // Add everything to dialog
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }
    
    private JPanel createEmployeePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BUTTON_COLOR),
                "Employee Information",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                MAIN_FONT,
                BUTTON_COLOR
            )
        ));
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(8, 8, 8, 8);
        
        empNumField = createStyledField();
        dateField = createStyledField();
        dateField.setText(LocalDate.now().toString());  // Default to today
        
        addFormRow(panel, "Employee Number:", empNumField, gc, 0);
        addFormRow(panel, "Date (YYYY-MM-DD):", dateField, gc, 1);
        
        return panel;
    }
    
    private JPanel createTimePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BUTTON_COLOR),
                "Time Records",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                MAIN_FONT,
                BUTTON_COLOR
            )
        ));
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(8, 8, 8, 8);
        
        timeInField = createStyledField();
        timeOutField = createStyledField();
        
        addFormRow(panel, "Time In (HH:mm):", timeInField, gc, 0);
        addFormRow(panel, "Time Out (HH:mm):", timeOutField, gc, 1);
        
        return panel;
    }
    
    private JTextField createStyledField() {
        JTextField field = new JTextField(15);
        field.setFont(MAIN_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BUTTON_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(MAIN_FONT);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BUTTON_COLOR),
            BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));
        return button;
    }
    
    private void addFormRow(JPanel panel, String labelText, JComponent field, 
                          GridBagConstraints gc, int gridy) {
        JLabel label = new JLabel(labelText);
        label.setFont(MAIN_FONT);
        label.setForeground(BUTTON_COLOR);
        
        gc.gridy = gridy;
        gc.gridx = 0;
        gc.weightx = 0.3;
        panel.add(label, gc);
        
        gc.gridx = 1;
        gc.weightx = 0.7;
        panel.add(field, gc);
    }
    
    private void handleSubmit() {
        try {
            // Parse date from YYYY-MM-DD format
            String[] dateParts = dateField.getText().split("-");
            LocalDate date = LocalDate.of(
                Integer.parseInt(dateParts[0]),
                Integer.parseInt(dateParts[1]),
                Integer.parseInt(dateParts[2])
            );
            
            // Parse times from HH:mm format
            String[] timeInParts = timeInField.getText().split(":");
            String[] timeOutParts = timeOutField.getText().split(":");
            
            LocalTime timeIn = LocalTime.of(
                Integer.parseInt(timeInParts[0]),
                Integer.parseInt(timeInParts[1])
            );
            
            LocalTime timeOut = LocalTime.of(
                Integer.parseInt(timeOutParts[0]),
                Integer.parseInt(timeOutParts[1])
            );
            
            // Create a new attendance record
            Attendance newAttendance = new Attendance();
            newAttendance.setEmployeeNumber(empNumField.getText().trim());
            newAttendance.setDate(date);
            newAttendance.setTimeIn(timeIn);
            newAttendance.setTimeOut(timeOut);
            
            // Store a deep copy
            attendance = new Attendance(newAttendance);
            
            // Close dialog
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Invalid date/time format. Please use YYYY-MM-DD for date and HH:mm for times.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Attendance getData() {
        setVisible(true);
        // Always return a deep copy
        return attendance != null ? new Attendance(attendance) : null;
    }
} 
