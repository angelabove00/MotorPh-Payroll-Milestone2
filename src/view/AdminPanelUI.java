package view;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Employee;
import model.EmployeeDatabase;

public class AdminPanelUI extends JFrame {
    private final JTextField 
                        employeeNoField, 
                        lastNameField, 
                        firstNameField, 
                        birthdayField, 
                        addressField, 
                        contactField, 
                        sssField, 
                        philhealthField, 
                        tinField, 
                        pagibigField, 
                        statusField, 
                        positionField, 
                        bossField, 
                        basesalaryField, 
                        ricesubField, 
                        phoneallowField, 
                        clothingallowField, 
                        grossemiField, 
                        hourrateField;

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton updateButton, deleteButton;
    private String selectedEmployeeNumber = null;
    private final EmployeeMainFrame parentFrame;

    public AdminPanelUI(EmployeeMainFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Admin Panel");
        setSize(1280, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Employee Data Table
        String[] columns = {
            "Employee #",
            "Last Name",
            "First Name",
            "Birthday",
            "Address",
            "Phone Number",
            "SSS #",
            "Philhealth #",
            "TIN #",
            "Pag-ibig #",
            "Status",
            "Position",
            "Immediate Supervisor",
            "Basic Salary",
            "Rice Subsidy",
            "Phone Allowance",
            "Clothing Allowance",
            "Gross Semi-monthly Rate",
            "Hourly Rate"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowSelectionAllowed(true);
        // Prevent row drag and dropping
        employeeTable.setDragEnabled(false);
        // Prevent row reordering by drag-and-drop
        employeeTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane tableScroll = new JScrollPane(employeeTable);
        tableScroll.setPreferredSize(new Dimension(900, 400));
        refreshTable();

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        // Helper to add label+field
        employeeNoField = new JTextField(15); employeeNoField.setEditable(false);
        addFormRow(formPanel, gbc, 0, "Employee No. (read-only):", employeeNoField);
        lastNameField = new JTextField(15);
        addFormRow(formPanel, gbc, 1, "Last Name:", lastNameField);
        firstNameField = new JTextField(15);
        addFormRow(formPanel, gbc, 2, "First Name:", firstNameField);
        //based on motorph website employee csv, the format they use is sample: 06/19/1988 -or- mm/dd/yyyy
        birthdayField = new JTextField(15);
        addFormRow(formPanel, gbc, 3, "Birthday (mm/dd/yyyy):", birthdayField);
        addressField = new JTextField(15);
        addFormRow(formPanel, gbc, 4, "Address:", addressField);
        contactField = new JTextField(15);
        addFormRow(formPanel, gbc, 5, "Contact:", contactField);
        sssField = new JTextField(15);
        addFormRow(formPanel, gbc, 6, "SSS No.:", sssField);
        philhealthField = new JTextField(15);
        addFormRow(formPanel, gbc, 7, "PhilHealth No.:", philhealthField);
        tinField = new JTextField(15);
        addFormRow(formPanel, gbc, 8, "TIN:", tinField);
        pagibigField = new JTextField(15);
        addFormRow(formPanel, gbc, 9, "Pag-IBIG No.:", pagibigField);
        statusField = new JTextField(15);
        addFormRow(formPanel, gbc, 10, "Employee Status:", statusField);
        positionField = new JTextField(15);
        addFormRow(formPanel, gbc, 11, "Position:", positionField);
        bossField = new JTextField(15);
        addFormRow(formPanel, gbc, 12, "Direct Supervisor:", bossField);
        basesalaryField = new JTextField(15);
        addFormRow(formPanel, gbc, 13, "Base Salary:", basesalaryField);
        ricesubField = new JTextField(15);
        addFormRow(formPanel, gbc, 14, "Rice Subsidy:", ricesubField);
        phoneallowField = new JTextField(15);
        addFormRow(formPanel, gbc, 15, "Phone Allowance:", phoneallowField);
        clothingallowField = new JTextField(15);
        addFormRow(formPanel, gbc, 16, "Clothing Allowance:", clothingallowField);
        grossemiField = new JTextField(15);
        addFormRow(formPanel, gbc, 17, "Gross Semi-monthly Rate:", grossemiField);
        hourrateField = new JTextField(15);
        addFormRow(formPanel, gbc, 18, "Hourly Rate:", hourrateField);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        gbc.gridx = 0; gbc.gridy = 19; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Layout: table left, form right
        add(tableScroll, BorderLayout.WEST);
        add(formPanel, BorderLayout.CENTER);

        // Table selection logic
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = employeeTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = employeeTable.convertRowIndexToModel(row);
                    String empNum = (String) tableModel.getValueAt(modelRow, 0);
                    Employee emp = EmployeeDatabase.findByEmployeeNumber(empNum);
                    if (emp != null) {
                        populateFields(emp);
                        selectedEmployeeNumber = emp.getEmployeeNumber();
                        updateButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                    }
                } else {
                    clearFields();
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    selectedEmployeeNumber = null;
                }
            }
        });
        employeeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = employeeTable.getSelectedRow();
                if (row >= 0) {
                    int modelRow = employeeTable.convertRowIndexToModel(row);
                    String empNum = (String) tableModel.getValueAt(modelRow, 0);
                    Employee emp = EmployeeDatabase.findByEmployeeNumber(empNum);
                    if (emp != null) {
                        populateFields(emp);
                        selectedEmployeeNumber = emp.getEmployeeNumber();
                        updateButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                    }
                }
            }
        });

        updateButton.addActionListener(e -> {
            if (selectedEmployeeNumber != null) {
                try {
                    Employee updatedEmp = getEmployeeFromFields();
                    EmployeeDatabase.updateEmployee(updatedEmp);
                    refreshTable();
                    if (parentFrame != null) parentFrame.refreshTable();
                    JOptionPane.showMessageDialog(AdminPanelUI.this, "Employee updated successfully.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdminPanelUI.this, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            if (selectedEmployeeNumber != null) {
                int confirm = JOptionPane.showConfirmDialog(AdminPanelUI.this, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        EmployeeDatabase.deleteEmployee(selectedEmployeeNumber);
                        refreshTable();
                        if (parentFrame != null) parentFrame.refreshTable();
                        clearFields();
                        updateButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                        selectedEmployeeNumber = null;
                        JOptionPane.showMessageDialog(AdminPanelUI.this, "Employee deleted.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(AdminPanelUI.this, "Delete failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Employee> employees = EmployeeDatabase.getEmployees();
        for (Employee emp : employees) {
            tableModel.addRow(new Object[] {
                    emp.getEmployeeNumber(), 
                    emp.getLastName(), 
                    emp.getFirstName(), 
                    emp.getBirthday(), 
                    emp.getAddress(), 
                    emp.getContactInfo(), 
                    emp.getSssNumber(), 
                    emp.getPhilhealthNumber(), 
                    emp.getTinNumber(), 
                    emp.getPagibigNumber(), 
                    emp.getStatus(), 
                    emp.getPosition(), 
                    emp.getSupervisor(), 
                    emp.getBaseSalary(), 
                    emp.getRiceSubsidy(), 
                    emp.getPhoneAllowance(), 
                    emp.getClothingAllowance(), 
                    emp.getSemiRate(), 
                    emp.getHourlyRate()

                }
            );
        }
    }

    private void populateFields(Employee emp) {
        employeeNoField.setText(emp.getEmployeeNumber());
        lastNameField.setText(emp.getLastName());
        firstNameField.setText(emp.getFirstName());
        birthdayField.setText(emp.getBirthday());
        addressField.setText(emp.getAddress());
        contactField.setText(emp.getContactInfo());
        sssField.setText(emp.getSssNumber());
        philhealthField.setText(emp.getPhilhealthNumber());
        tinField.setText(emp.getTinNumber());
        pagibigField.setText(emp.getPagibigNumber());
        statusField.setText(emp.getStatus());
        positionField.setText(emp.getPosition());
        bossField.setText(emp.getSupervisor());
        basesalaryField.setText(emp.getBaseSalary());
        ricesubField.setText(emp.getRiceSubsidy());
        phoneallowField.setText(emp.getPhoneAllowance());
        clothingallowField.setText(emp.getClothingAllowance());
        grossemiField.setText(emp.getSemiRate());
        hourrateField.setText(emp.getHourlyRate());
    }

    private void clearFields() {
        employeeNoField.setText("");
        lastNameField.setText("");
        firstNameField.setText("");
        birthdayField.setText("");
        addressField.setText("");
        contactField.setText("");
        sssField.setText("");
        philhealthField.setText("");
        tinField.setText("");
        pagibigField.setText("");
        statusField.setText("");
        positionField.setText("");
        bossField.setText("");
        basesalaryField.setText("");
        ricesubField.setText("");
        phoneallowField.setText("");
        clothingallowField.setText("");
        grossemiField.setText("");
        hourrateField.setText("");
    }

    private Employee getEmployeeFromFields() {
        return new Employee(
            employeeNoField.getText().trim(),
            lastNameField.getText().trim(),
            firstNameField.getText().trim(),
            birthdayField.getText().trim(),
            addressField.getText().trim(),
            contactField.getText().trim(),
            sssField.getText().trim(),
            philhealthField.getText().trim(),
            tinField.getText().trim(),
            pagibigField.getText().trim(),
            statusField.getText().trim(),
            positionField.getText().trim(),
            bossField.getText().trim(),
            basesalaryField.getText().trim(),
            ricesubField.getText().trim(),
            phoneallowField.getText().trim(),
            clothingallowField.getText().trim(),
            grossemiField.getText().trim(),
            hourrateField.getText().trim()
        );
    }
}
