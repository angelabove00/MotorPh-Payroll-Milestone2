package view;

import java.awt.*;
import java.text.NumberFormat;
import java.time.Month;
import java.util.Locale;
import javax.swing.*;
import model.Employee;
import service.PayrollService.PayrollResult;

public class PayslipFrame extends JFrame {
    private final Employee employee;
    private final PayrollResult result;
    private final Month selectedMonth;
    private final int currentYear;

    public PayslipFrame(Employee employee, PayrollResult result, Month selectedMonth) {
        this.employee = employee;
        this.result = result;
        this.selectedMonth = selectedMonth;
        this.currentYear = java.time.LocalDate.now().getYear();

        setTitle("View Employee: " + employee.getFirstName() + " " + employee.getLastName());
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Create main content panel with padding
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);

        int row = 0;

        // Company name
        JLabel companyLabel = new JLabel("MotorPH Corporation");
        companyLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        companyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row++;
        contentPanel.add(companyLabel, gbc);

        // Employee name
        JLabel nameLabel = new JLabel(employee.getFirstName() + " " + employee.getLastName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = row++;
        contentPanel.add(nameLabel, gbc);

        // Pay period
        String payPeriod = String.format("Pay Period: %s %d", selectedMonth.toString(), currentYear);
        JLabel periodLabel = new JLabel(payPeriod);
        periodLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        periodLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = row++;
        contentPanel.add(periodLabel, gbc);

        // Add separator with more spacing
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridy = row++;
        contentPanel.add(new JSeparator(), gbc);
        gbc.insets = new Insets(5, 0, 5, 0);

        // EARNINGS section
        JLabel earningsLabel = new JLabel("EARNINGS");
        earningsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridy = row++;
        contentPanel.add(earningsLabel, gbc);

        // Create table-like panel for earnings
        JPanel earningsPanel = createTablePanel();
        addTableRow(earningsPanel, "Monthly Salary", result.getBasicPay(), false);
        if (result.getOvertimeHours() > 0) {
            addTableRow(earningsPanel, "Overtime Pay", result.getOvertimePay(), false);
        }
        if (result.getBonus() > 0) {
            addTableRow(earningsPanel, "Bonus", result.getBonus(), false);
        }
        addTableRow(earningsPanel, "Gross Pay", result.getGrossPay(), true);

        gbc.gridy = row++;
        contentPanel.add(earningsPanel, gbc);

        // Add spacing
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridy = row++;
        contentPanel.add(new JSeparator(), gbc);
        gbc.insets = new Insets(5, 0, 5, 0);

        // DEDUCTIONS section
        JLabel deductionsLabel = new JLabel("DEDUCTIONS");
        deductionsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridy = row++;
        contentPanel.add(deductionsLabel, gbc);

        // Create table-like panel for deductions
        JPanel deductionsPanel = createTablePanel();
        addTableRow(deductionsPanel, "SSS Contribution", result.getSssDeduction(), false);
        addTableRow(deductionsPanel, "PhilHealth", result.getPhilhealthDeduction(), false);
        addTableRow(deductionsPanel, "Pag-IBIG", result.getPagibigDeduction(), false);
        addTableRow(deductionsPanel, "Withholding Tax", result.getTaxDeduction(), false);
        if (result.getLoans() > 0) {
            addTableRow(deductionsPanel, "Loans", result.getLoans(), false);
        }
        double totalDeductions = result.getSssDeduction() + result.getPhilhealthDeduction() + 
                               result.getPagibigDeduction() + result.getTaxDeduction() + result.getLoans();
        addTableRow(deductionsPanel, "Total Deductions", totalDeductions, true);

        gbc.gridy = row++;
        contentPanel.add(deductionsPanel, gbc);

        // Add spacing before net pay
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridy = row++;
        contentPanel.add(new JSeparator(), gbc);
        gbc.insets = new Insets(5, 0, 5, 0);

        // NET PAY section
        JPanel netPayPanel = new JPanel(new GridBagLayout());
        netPayPanel.setBackground(new Color(240, 240, 240));
        netPayPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints netGbc = new GridBagConstraints();
        netGbc.gridx = 0;
        netGbc.gridy = 0;
        netGbc.weightx = 1.0;
        netGbc.anchor = GridBagConstraints.EAST;

        JLabel netPayLabel = new JLabel("NET PAY:");
        netPayLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        netPayPanel.add(netPayLabel, netGbc);

        netGbc.gridx = 1;
        netGbc.insets = new Insets(0, 20, 0, 0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        JLabel netPayAmount = new JLabel(currencyFormat.format(result.getNetPay()));
        netPayAmount.setFont(new Font("SansSerif", Font.BOLD, 24));
        netPayPanel.add(netPayAmount, netGbc);

        gbc.gridy = row++;
        contentPanel.add(netPayPanel, gbc);

        // Add the content panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        setContentPane(scrollPane);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        return panel;
    }

    private void addTableRow(JPanel panel, String label, double amount, boolean bold) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = panel.getComponentCount();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 20);

        Font font = bold ? new Font("SansSerif", Font.BOLD, 14) : new Font("SansSerif", Font.PLAIN, 14);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(font);
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(5, 20, 5, 0);
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        JLabel amountComponent = new JLabel(currencyFormat.format(amount));
        amountComponent.setFont(font);
        amountComponent.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(amountComponent, gbc);
    }
} 