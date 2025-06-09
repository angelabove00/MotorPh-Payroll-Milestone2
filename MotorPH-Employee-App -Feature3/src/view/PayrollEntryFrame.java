package view;

import java.awt.*;
import java.time.Month;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import model.Employee;
import model.Constants;
import service.PayrollService;
import service.PayrollService.PayrollResult;

public class PayrollEntryFrame extends JFrame {
    private final Employee employee;
    private final JSpinner hoursWorkedSpinner;
    private final JSpinner overtimeHoursSpinner;
    private final JFormattedTextField bonusField;
    private final JFormattedTextField loanField;
    private final JComboBox<Month> monthComboBox;

    public PayrollEntryFrame(Employee employee) {
        this.employee = employee;
        setTitle("Compute Payroll - " + employee.getFirstName() + " " + employee.getLastName());
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.gridwidth = 1;

        // Add title
        JLabel titleLabel = new JLabel("Payroll Entry");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Initialize spinners
        SpinnerNumberModel hoursModel = new SpinnerNumberModel(160.0, 0.0, 200.0, 0.5);
        hoursWorkedSpinner = new JSpinner(hoursModel);
        JSpinner.NumberEditor hoursEditor = new JSpinner.NumberEditor(hoursWorkedSpinner, "0.0");
        hoursWorkedSpinner.setEditor(hoursEditor);

        SpinnerNumberModel overtimeModel = new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5);
        overtimeHoursSpinner = new JSpinner(overtimeModel);
        JSpinner.NumberEditor overtimeEditor = new JSpinner.NumberEditor(overtimeHoursSpinner, "0.0");
        overtimeHoursSpinner.setEditor(overtimeEditor);

        // Initialize formatted text fields for currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        NumberFormatter currencyFormatter = new NumberFormatter(currencyFormat);
        currencyFormatter.setMinimum(0.0);
        currencyFormatter.setAllowsInvalid(false);

        bonusField = new JFormattedTextField(currencyFormatter);
        bonusField.setValue(0.0);
        bonusField.setColumns(10);

        loanField = new JFormattedTextField(currencyFormatter);
        loanField.setValue(0.0);
        loanField.setColumns(10);

        // Month selection
        monthComboBox = new JComboBox<>(Month.values());
        monthComboBox.setSelectedItem(Month.valueOf(java.time.LocalDate.now().getMonth().name()));

        // Add fields
        int row = 0;
        addField(formPanel, gbc, "Pay Period:", monthComboBox, row++);
        addField(formPanel, gbc, "Hours Worked:", hoursWorkedSpinner, row++);
        addField(formPanel, gbc, "Overtime Hours:", overtimeHoursSpinner, row++);
        addField(formPanel, gbc, "Bonus Amount:", bonusField, row++);
        addField(formPanel, gbc, "Loan Deduction:", loanField, row++);

        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton computeButton = new JButton("Compute Payroll");
        JButton cancelButton = new JButton("Cancel");

        // Style buttons
        Dimension buttonSize = new Dimension(130, 30);
        computeButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);

        computeButton.addActionListener(e -> computePayroll());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(computeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add shadow border
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            mainPanel.getBorder()
        ));

        setContentPane(mainPanel);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelComponent.setHorizontalAlignment(SwingConstants.RIGHT);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 5);
        panel.add(field, gbc);
        gbc.insets = new Insets(5, 5, 5, 5);
    }

    private void computePayroll() {
        try {
            double hoursWorked = ((Number) hoursWorkedSpinner.getValue()).doubleValue();
            double overtimeHours = ((Number) overtimeHoursSpinner.getValue()).doubleValue();
            double bonus = ((Number) bonusField.getValue()).doubleValue();
            double loans = ((Number) loanField.getValue()).doubleValue();
            Month selectedMonth = (Month) monthComboBox.getSelectedItem();

            PayrollResult result = PayrollService.computePayroll(
                employee, selectedMonth, hoursWorked, overtimeHours, bonus, loans
            );

            PayslipFrame payslipFrame = new PayslipFrame(employee, result, selectedMonth);
            payslipFrame.setVisible(true);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error computing payroll: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 