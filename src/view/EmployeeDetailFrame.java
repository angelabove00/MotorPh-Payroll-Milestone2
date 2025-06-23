package view;

import java.awt.*;
import java.time.Month;
import javax.swing.*;
import model.Constants;
import model.Employee;
import service.PayrollService;
import service.PayrollService.PayrollResult;

public class EmployeeDetailFrame extends JFrame {
    private final Employee employee;
    private JComboBox<Month> monthSelector;
    private JSpinner hoursSpinner;
    private JSpinner overtimeSpinner;
    private JSpinner bonusSpinner;
    private JSpinner loansSpinner;

    public EmployeeDetailFrame(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        this.employee = employee;
        setTitle("View Employee: " + employee.getFirstName() + " " + employee.getLastName());
        setSize(UIConstants.DETAIL_WINDOW_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize spinners
        monthSelector = new JComboBox<>(Month.values());
        monthSelector.setSelectedItem(Month.valueOf(java.time.LocalDate.now().getMonth().name()));
        
        hoursSpinner = new JSpinner(new SpinnerNumberModel(Constants.STANDARD_HOURS_PER_MONTH, 0.0, 350.0, 1.0));
        hoursSpinner.setEditor(new JSpinner.NumberEditor(hoursSpinner, "#,##0.0"));
        
        overtimeSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 350.0, 1.0));
        overtimeSpinner.setEditor(new JSpinner.NumberEditor(overtimeSpinner, "#,##0.0"));
        
        bonusSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100000.0, 500.0));
        bonusSpinner.setEditor(new JSpinner.NumberEditor(bonusSpinner, "#,##0.00"));
        
        loansSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100000.0, 500.0));
        loansSpinner.setEditor(new JSpinner.NumberEditor(loansSpinner, "#,##0.00"));

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
        addField(mainPanel, gbc, "Position:", employee.getPosition(), row++);
        addField(mainPanel, gbc, "Department:", employee.getDepartment(), row++);
        addField(mainPanel, gbc, "Birthday:", employee.getBirthday().toString(), row++);
        addField(mainPanel, gbc, "Contact No.:", employee.getContactInfo(), row++);
        addField(mainPanel, gbc, "SSS No.:", employee.getSssNumber(), row++);
        addField(mainPanel, gbc, "PhilHealth No.:", employee.getPhilhealthNumber(), row++);
        addField(mainPanel, gbc, "TIN:", employee.getTinNumber(), row++);
        addField(mainPanel, gbc, "Pag-IBIG No.:", employee.getPagibigNumber(), row++);

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

        // Add spinners
        addSpinner(mainPanel, gbc, "Hours Worked:", hoursSpinner, row++);
        addSpinner(mainPanel, gbc, "Overtime Hours:", overtimeSpinner, row++);
        addSpinner(mainPanel, gbc, "Bonus:", bonusSpinner, row++);
        addSpinner(mainPanel, gbc, "Loans/Deductions:", loansSpinner, row++);

        // Add compute button
        JButton computeButton = new JButton("Compute Payroll");
        computeButton.addActionListener(e -> computePayroll());
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(computeButton, gbc);

        // Add the main panel to a scroll pane
        add(new JScrollPane(mainPanel));
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

    private void addSpinner(JPanel panel, GridBagConstraints gbc, String label, JSpinner spinner, int row) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(UIConstants.LABEL_FONT);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        panel.add(spinner, gbc);
    }

    private void addSeparator(JPanel panel, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 15, 5);
        panel.add(new JSeparator(), gbc);
        gbc.insets = new Insets(5, 5, 5, 5);
    }

    private void computePayroll() {
        try {
            PayrollResult result = PayrollService.computePayroll(
                employee,
                (Month) monthSelector.getSelectedItem(),
                ((Number) hoursSpinner.getValue()).doubleValue(),
                ((Number) overtimeSpinner.getValue()).doubleValue(),
                ((Number) bonusSpinner.getValue()).doubleValue(),
                ((Number) loansSpinner.getValue()).doubleValue()
            );

            PayslipFrame payslipFrame = new PayslipFrame(employee, result, (Month) monthSelector.getSelectedItem());
            payslipFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error computing payroll: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 