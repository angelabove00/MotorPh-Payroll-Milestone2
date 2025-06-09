package view;

import java.awt.*;
import java.time.LocalDate;
import java.util.regex.Pattern;
import javax.swing.*;
import model.Constants;
import model.Employee;
import model.EmployeeDatabase;

public class NewEmployeeFrame extends JFrame {
    private final EmployeeMainFrame parentFrame;
    private final JTextField employeeNoField;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField contactNoField;
    private final JTextField positionField;
    private final JTextField departmentField;
    private final JTextField sssNoField;
    private final JTextField philhealthNoField;
    private final JTextField tinField;
    private final JTextField pagibigNoField;
    private final JSpinner yearSpinner;
    private final JSpinner monthSpinner;
    private final JSpinner daySpinner;

    public NewEmployeeFrame(EmployeeMainFrame parent) {
        this.parentFrame = parent;
        setTitle("New Employee");
        setSize(500, 600);
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
        JLabel titleLabel = new JLabel("New Employee");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Initialize fields
        employeeNoField = createField("Enter employee number");
        firstNameField = createField("Enter first name");
        lastNameField = createField("Enter last name");
        contactNoField = createField("Format: 09XXXXXXXXX");
        positionField = createField("Enter position");
        departmentField = createField("Enter department");
        sssNoField = createField("Format: XX-XXXXXXX-X");
        philhealthNoField = createField("Format: XX-XXXXXXXXX-X");
        tinField = createField("Format: XXX-XXX-XXX");
        pagibigNoField = createField("Format: XXXX-XXXX-XXXX");

        // Date spinners
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        
        // Year spinner: allow ages 18-65
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear - 25, currentYear - 65, currentYear - 18, 1));
        JSpinner.NumberEditor yearEditor = new JSpinner.NumberEditor(yearSpinner, "#");
        yearSpinner.setEditor(yearEditor);
        
        // Month spinner: 1-12
        monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        monthSpinner.setEditor(new JSpinner.NumberEditor(monthSpinner, "00"));
        
        // Day spinner: 1-31
        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        daySpinner.setEditor(new JSpinner.NumberEditor(daySpinner, "00"));

        // Add month change listener to update day spinner maximum
        monthSpinner.addChangeListener(e -> updateDaySpinnerRange());
        yearSpinner.addChangeListener(e -> updateDaySpinnerRange());

        // Add fields in specified order
        int row = 0;
        addField(formPanel, gbc, "Employee No.:", employeeNoField, row++);
        addField(formPanel, gbc, "First Name:", firstNameField, row++);
        addField(formPanel, gbc, "Last Name:", lastNameField, row++);
        addField(formPanel, gbc, "Contact No.:", contactNoField, row++);
        addField(formPanel, gbc, "Position:", positionField, row++);
        addField(formPanel, gbc, "Department:", departmentField, row++);
        addField(formPanel, gbc, "SSS No.:", sssNoField, row++);
        addField(formPanel, gbc, "PhilHealth No.:", philhealthNoField, row++);
        addField(formPanel, gbc, "TIN:", tinField, row++);
        addField(formPanel, gbc, "Pag-IBIG No.:", pagibigNoField, row++);

        // Add birthday spinners
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        datePanel.add(new JLabel("Year:"));
        datePanel.add(yearSpinner);
        datePanel.add(Box.createHorizontalStrut(10));
        datePanel.add(new JLabel("Month:"));
        datePanel.add(monthSpinner);
        datePanel.add(Box.createHorizontalStrut(10));
        datePanel.add(new JLabel("Day:"));
        datePanel.add(daySpinner);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Birthday:"), gbc);

        gbc.gridy = row++;
        formPanel.add(datePanel, gbc);

        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        // Style buttons
        Dimension buttonSize = new Dimension(100, 30);
        saveButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);

        saveButton.addActionListener(e -> saveEmployee());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add shadow border to main panel
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            mainPanel.getBorder()
        ));

        setContentPane(new JScrollPane(mainPanel));
    }

    private JTextField createField(String tooltip) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(250, 25));
        field.setToolTipText(tooltip);
        return field;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int row) {
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

    private boolean validateFields() {
        // Check if required fields are empty
        if (employeeNoField.getText().trim().isEmpty() ||
            firstNameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
            contactNoField.getText().trim().isEmpty() ||
            positionField.getText().trim().isEmpty() ||
            departmentField.getText().trim().isEmpty() ||
            sssNoField.getText().trim().isEmpty() ||
            philhealthNoField.getText().trim().isEmpty() ||
            tinField.getText().trim().isEmpty() ||
            pagibigNoField.getText().trim().isEmpty()) {
            
            showError(Constants.REQUIRED_FIELDS_ERROR);
            return false;
        }

        // Check if employee number is unique
        if (EmployeeDatabase.findByEmployeeNumber(employeeNoField.getText().trim()) != null) {
            showError(Constants.DUPLICATE_EMP_NUM_ERROR);
            employeeNoField.requestFocus();
            return false;
        }

        // Validate patterns
        if (!Pattern.matches(Constants.CONTACT_NUMBER_PATTERN, contactNoField.getText().trim())) {
            showError(Constants.INVALID_CONTACT_ERROR);
            contactNoField.requestFocus();
            return false;
        }

        if (!Pattern.matches(Constants.SSS_NUMBER_PATTERN, sssNoField.getText().trim())) {
            showError(Constants.INVALID_SSS_ERROR);
            sssNoField.requestFocus();
            return false;
        }

        if (!Pattern.matches(Constants.PHILHEALTH_NUMBER_PATTERN, philhealthNoField.getText().trim())) {
            showError(Constants.INVALID_PHILHEALTH_ERROR);
            philhealthNoField.requestFocus();
            return false;
        }

        if (!Pattern.matches(Constants.TIN_NUMBER_PATTERN, tinField.getText().trim())) {
            showError(Constants.INVALID_TIN_ERROR);
            tinField.requestFocus();
            return false;
        }

        if (!Pattern.matches(Constants.PAGIBIG_NUMBER_PATTERN, pagibigNoField.getText().trim())) {
            showError(Constants.INVALID_PAGIBIG_ERROR);
            pagibigNoField.requestFocus();
            return false;
        }

        return true;
    }

    private void saveEmployee() {
        if (!validateFields()) {
            return;
        }

        try {
            LocalDate birthday = LocalDate.of(
                (Integer) yearSpinner.getValue(),
                (Integer) monthSpinner.getValue(),
                (Integer) daySpinner.getValue()
            );

            Employee newEmployee = new Employee(
                employeeNoField.getText().trim(),
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                contactNoField.getText().trim(),
                positionField.getText().trim(),
                departmentField.getText().trim(),
                birthday,
                sssNoField.getText().trim(),
                philhealthNoField.getText().trim(),
                tinField.getText().trim(),
                pagibigNoField.getText().trim()
            );

            EmployeeDatabase.addEmployee(newEmployee);
            parentFrame.refreshTable();
            dispose();
            
            JOptionPane.showMessageDialog(this,
                "Employee added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            showError("Failed to save employee: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private void updateDaySpinnerRange() {
        int year = (Integer) yearSpinner.getValue();
        int month = (Integer) monthSpinner.getValue();
        int currentDay = (Integer) daySpinner.getValue();
        
        // Get days in month
        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        
        // Update day spinner model
        SpinnerNumberModel model = (SpinnerNumberModel) daySpinner.getModel();
        model.setMaximum(daysInMonth);
        
        // Adjust current value if needed
        if (currentDay > daysInMonth) {
            daySpinner.setValue(daysInMonth);
        }
    }
} 