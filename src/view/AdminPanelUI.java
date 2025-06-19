package view;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Employee;
import model.EmployeeDatabase;

public class AdminPanelUI extends JFrame {
    private JTextField employeeNoField, firstNameField, lastNameField, contactField, positionField, departmentField, birthdayField, sssField, philhealthField, tinField, pagibigField;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton updateButton, deleteButton;
    private String selectedEmployeeNumber = null;
    private final EmployeeMainFrame parentFrame;

    public AdminPanelUI(EmployeeMainFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Admin Panel");
        setSize(1100, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Table
        String[] columns = {"Employee No.", "Last Name", "First Name", "Contact", "Position", "Department", "Birthday", "SSS", "PhilHealth", "TIN", "Pag-IBIG"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowSelectionAllowed(true);
        employeeTable.setDragEnabled(false);
        // Prevent row reordering by drag-and-drop
        employeeTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane tableScroll = new JScrollPane(employeeTable);
        tableScroll.setPreferredSize(new Dimension(700, 400));
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
        firstNameField = new JTextField(15);
        addFormRow(formPanel, gbc, 1, "First Name:", firstNameField);
        lastNameField = new JTextField(15);
        addFormRow(formPanel, gbc, 2, "Last Name:", lastNameField);
        contactField = new JTextField(15);
        addFormRow(formPanel, gbc, 3, "Contact:", contactField);
        positionField = new JTextField(15);
        addFormRow(formPanel, gbc, 4, "Position:", positionField);
        departmentField = new JTextField(15);
        addFormRow(formPanel, gbc, 5, "Department:", departmentField);
        birthdayField = new JTextField(15);
        addFormRow(formPanel, gbc, 6, "Birthday (yyyy-mm-dd):", birthdayField);
        sssField = new JTextField(15);
        addFormRow(formPanel, gbc, 7, "SSS No.:", sssField);
        philhealthField = new JTextField(15);
        addFormRow(formPanel, gbc, 8, "PhilHealth No.:", philhealthField);
        tinField = new JTextField(15);
        addFormRow(formPanel, gbc, 9, "TIN:", tinField);
        pagibigField = new JTextField(15);
        addFormRow(formPanel, gbc, 10, "Pag-IBIG No.:", pagibigField);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 2;
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
                emp.getEmployeeNumber(), emp.getLastName(), emp.getFirstName(), emp.getContactInfo(), emp.getPosition(), emp.getDepartment(), emp.getBirthday().toString(), emp.getSssNumber(), emp.getPhilhealthNumber(), emp.getTinNumber(), emp.getPagibigNumber()
            });
        }
    }

    private void populateFields(Employee emp) {
        employeeNoField.setText(emp.getEmployeeNumber());
        firstNameField.setText(emp.getFirstName());
        lastNameField.setText(emp.getLastName());
        contactField.setText(emp.getContactInfo());
        positionField.setText(emp.getPosition());
        departmentField.setText(emp.getDepartment());
        birthdayField.setText(emp.getBirthday().toString());
        sssField.setText(emp.getSssNumber());
        philhealthField.setText(emp.getPhilhealthNumber());
        tinField.setText(emp.getTinNumber());
        pagibigField.setText(emp.getPagibigNumber());
    }

    private void clearFields() {
        employeeNoField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        contactField.setText("");
        positionField.setText("");
        departmentField.setText("");
        birthdayField.setText("");
        sssField.setText("");
        philhealthField.setText("");
        tinField.setText("");
        pagibigField.setText("");
    }

    private Employee getEmployeeFromFields() {
        return new Employee(
            employeeNoField.getText().trim(),
            firstNameField.getText().trim(),
            lastNameField.getText().trim(),
            contactField.getText().trim(),
            positionField.getText().trim(),
            departmentField.getText().trim(),
            LocalDate.parse(birthdayField.getText().trim()),
            sssField.getText().trim(),
            philhealthField.getText().trim(),
            tinField.getText().trim(),
            pagibigField.getText().trim()
        );
    }
}
