package view;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import model.Employee;

public class EmployeeDetailFrame extends JFrame {
    private final Employee employee;
    private JComboBox<Month> monthSelector;
    private JLabel totalPayLabel; // Label to display total pay

    private static final String ATTENDANCE_CSV_PATH = "src/resources/attendance.csv";
    private static final double ATTENDANCE_HOURLY_RATE = 500.0;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm");

    public EmployeeDetailFrame(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        this.employee = employee;
        setTitle("View Employee: " + employee.getFirstName() + " " + employee.getLastName());
        setSize(UIConstants.DETAIL_WINDOW_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize month selector only
        monthSelector = new JComboBox<>(Month.values());
        monthSelector.setSelectedItem(Month.valueOf(java.time.LocalDate.now().getMonth().name()));

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(UIConstants.EMPTY_BORDER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Add employee details
        int row = 0;
        addField(mainPanel, gbc, "Employee No.:", employee.getEmployeeNumber(), row++);
        addField(mainPanel, gbc, "First Name:", employee.getFirstName(), row++);
        addField(mainPanel, gbc, "Last Name:", employee.getLastName(), row++);
        addField(mainPanel, gbc, "Birthday:", employee.getBirthday(), row++);
        addField(mainPanel, gbc, "Contact No.:", employee.getContactInfo(), row++);
        addField(mainPanel, gbc, "Address:", employee.getAddress(), row++);
        addField(mainPanel, gbc, "Employee Status:", employee.getStatus(), row++);        
        addField(mainPanel, gbc, "Position:", employee.getPosition(), row++);
        addField(mainPanel, gbc, "Direct Supervisor:", employee.getSupervisor(), row++);
        addField(mainPanel, gbc, "SSS No.:", employee.getSssNumber(), row++);
        addField(mainPanel, gbc, "PhilHealth No.:", employee.getPhilhealthNumber(), row++);
        addField(mainPanel, gbc, "TIN:", employee.getTinNumber(), row++);
        addField(mainPanel, gbc, "Pag-IBIG No.:", employee.getPagibigNumber(), row++);
        addField(mainPanel, gbc, "Basic Salary:", employee.getBaseSalary(), row++);
        addField(mainPanel, gbc, "Rice Subsidy:", employee.getRiceSubsidy(), row++);
        addField(mainPanel, gbc, "Phone Allowance:", employee.getPhoneAllowance(), row++);
        addField(mainPanel, gbc, "Clothing Allowance:", employee.getClothingAllowance(), row++);
        addField(mainPanel, gbc, "Gross Semi-monthly Rate:", employee.getSemiRate(), row++);
        addField(mainPanel, gbc, "Hourly Rate:", employee.getHourlyRate(), row++);
        
        // Add separator
        addSeparator(mainPanel, gbc, row++);

        // Add payroll section
        JLabel payrollLabel = new JLabel("Payroll Computation");
        payrollLabel.setFont(UIConstants.HEADER_FONT);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        mainPanel.add(payrollLabel, gbc);
        gbc.gridwidth = 1;

        // Add month selector
        gbc.gridx = 0;
        gbc.gridy = row;
        mainPanel.add(new JLabel("Month:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(monthSelector, gbc);
        row++;

        // Add total pay label below payroll fields
        totalPayLabel = new JLabel();
        totalPayLabel.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        mainPanel.add(totalPayLabel, gbc);
        gbc.gridwidth = 1;

        // Add the main panel to a scroll pane
        add(new JScrollPane(mainPanel));

        // Update total pay on window open and when month changes
        updateTotalPay();
        monthSelector.addActionListener(e -> updateTotalPay());
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, String value, int row) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(UIConstants.LABEL_FONT);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(UIConstants.LABEL_FONT);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        panel.add(valueComponent, gbc);
    }

    private void addSeparator(JPanel panel, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 15, 5);
        panel.add(new JSeparator(), gbc);
        gbc.insets = new Insets(5, 5, 5, 5);
    }

    // Add this method to compute and display total pay from attendance.csv
    private void updateTotalPay() {
        double totalPay = 0.0;
        String empNum = employee.getEmployeeNumber();
        Month selectedMonth = (Month) monthSelector.getSelectedItem();
        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_CSV_PATH))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                String recordEmpNum = parts[0].trim();
                String dateStr = parts[3].trim();
                String logInStr = parts[4].trim();
                String logOutStr = parts[5].trim();
                if (!recordEmpNum.equals(empNum)) continue;
                java.time.LocalDate date = java.time.LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                if (date.getMonth() != selectedMonth) continue;
                LocalTime logIn = LocalTime.parse(logInStr, TIME_FORMAT);
                LocalTime logOut = LocalTime.parse(logOutStr, TIME_FORMAT);
                double hoursWorked = java.time.Duration.between(logIn, logOut).toMinutes() / 60.0;
                if (hoursWorked < 0) hoursWorked = 0; // safety
                totalPay += hoursWorked * ATTENDANCE_HOURLY_RATE;
            }
        } catch (Exception ex) {
            totalPayLabel.setText("Total Pay (Attendance): Error reading attendance");
            return;
        }
        totalPayLabel.setText(String.format(Locale.US, "Total Pay (Attendance): PHP %,.2f", totalPay));
    }
}
