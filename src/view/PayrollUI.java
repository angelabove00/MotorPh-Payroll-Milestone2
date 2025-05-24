package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import model.*;

public class PayrollUI extends JDialog {
    // Constants for calculations
    private static final double HOURLY_RATE = 150.0;  // Base hourly rate
    private static final double SSS_DEDUCTION = 500.0;
    private static final double PAGIBIG_DEDUCTION = 200.0;
    private static final double PHILHEALTH_DEDUCTION = 300.0;
    
    // Colors and Fonts
    private static final Color BG_COLOR = new Color(240, 244, 248);
    private static final Color BUTTON_COLOR = new Color(79, 109, 122);
    private static final Font MAIN_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font MONO_FONT = new Font("Consolas", Font.PLAIN, 14);
    
    private JTextField empNumField;
    private JTextArea resultArea;
    private Employee currentEmployee;
    
    public PayrollUI(JFrame parent, Employee employee) {
        super(parent, "Payroll Calculation", true);
        this.currentEmployee = employee;
        setupUI();
        pack();
        setLocationRelativeTo(parent);
        
        // Auto-calculate if employee is provided
        if (currentEmployee != null && currentEmployee.getEmployeeNumber() != null) {
            empNumField.setText(currentEmployee.getEmployeeNumber());
            calculatePayroll();
        }
    }
    
    private void setupUI() {
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Input panel with title border
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(BG_COLOR);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
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
        
        // Add employee number field
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(8, 8, 8, 8);
        
        JLabel empLabel = new JLabel("Employee Number:");
        empLabel.setFont(MAIN_FONT);
        empLabel.setForeground(BUTTON_COLOR);
        empNumField = createStyledField();
        
        gc.gridx = 0; gc.weightx = 0.3;
        inputPanel.add(empLabel, gc);
        gc.gridx = 1; gc.weightx = 0.7;
        inputPanel.add(empNumField, gc);
        
        // Calculate button
        JButton calcButton = createStyledButton("Calculate");
        calcButton.addActionListener(e -> calculatePayroll());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.add(calcButton);
        
        // Results area with border
        resultArea = new JTextArea(20, 50);
        resultArea.setFont(MONO_FONT);
        resultArea.setBackground(Color.WHITE);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BUTTON_COLOR),
                "Payroll Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                MAIN_FONT,
                BUTTON_COLOR
            )
        ));
        
        // Add components to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
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
    
    private void calculatePayroll() {
        String empNum = empNumField.getText().trim();
        if (empNum.isEmpty()) {
            showError("Please enter an employee number");
            return;
        }
        
        // First check if employee exists in database
        Employee employee = EmployeeDatabase.findByNumber(empNum);
        if (employee == null) {
            showError("Employee not found!");
            return;
        }
        
        // Get attendance records for current pay period
        List<Attendance> records = AttendanceDatabase.getRecordsForEmployee(empNum);
        if (records.isEmpty()) {
            showError("No attendance records found for employee " + empNum);
            return;
        }
        
        // Calculate total hours from attendance records
        double totalHours = records.stream()
                                 .mapToDouble(Attendance::calculateTotalHours)
                                 .sum();
        
        // Calculate pay and deductions
        double grossPay = totalHours * HOURLY_RATE;
        double totalDeductions = SSS_DEDUCTION + PAGIBIG_DEDUCTION + PHILHEALTH_DEDUCTION;
        double netPay = grossPay - totalDeductions;
        
        // Build detailed report
        StringBuilder report = new StringBuilder();
        report.append("PAYROLL CALCULATION REPORT\n");
        report.append("=========================\n\n");
        
        report.append("Employee Details\n");
        report.append("----------------\n");
        report.append("Name: ").append(employee.getName()).append("\n");
        report.append("Number: ").append(employee.getEmployeeNumber()).append("\n");
        report.append("Position: ").append(employee.getPosition()).append("\n");
        report.append("Department: ").append(employee.getDepartment()).append("\n\n");
        
        report.append("Attendance Summary\n");
        report.append("-----------------\n");
        report.append(String.format("Total Hours Worked: %.2f\n", totalHours));
        report.append(String.format("Hourly Rate: ₱%.2f\n\n", HOURLY_RATE));
        
        report.append("Pay Calculation\n");
        report.append("---------------\n");
        report.append(String.format("Gross Pay: ₱%.2f\n\n", grossPay));
        
        report.append("Deductions\n");
        report.append("----------\n");
        report.append(String.format("SSS:        ₱%.2f\n", SSS_DEDUCTION));
        report.append(String.format("Pag-IBIG:   ₱%.2f\n", PAGIBIG_DEDUCTION));
        report.append(String.format("PhilHealth: ₱%.2f\n", PHILHEALTH_DEDUCTION));
        report.append(String.format("Total:      ₱%.2f\n\n", totalDeductions));
        
        report.append("Final Computation\n");
        report.append("----------------\n");
        report.append(String.format("NET PAY:    ₱%.2f\n", netPay));
        
        // Show the report
        resultArea.setText(report.toString());
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
        resultArea.setText("Error: " + message);
    }
} 
