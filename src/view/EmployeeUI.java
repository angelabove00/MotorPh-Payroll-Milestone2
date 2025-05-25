package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import model.Employee;
import model.EmployeeDatabase;


public class EmployeeUI extends JDialog {
    // Colors and Fonts
    private static final Color BG_COLOR = new Color(240, 244, 248);  // #f0f4f8
    private static final Color BUTTON_COLOR = new Color(79, 109, 122);  // #4f6d7a
    private static final Font MAIN_FONT = new Font("SansSerif", Font.PLAIN, 14);

    // Form fields
    private JTextField nameField;
    private JTextField empNumField;
    private JTextField contactField;
    private JTextField birthdayField;
    private JTextField positionField;
    private JTextField deptField;
    private JButton submitBtn;

    // Store the employee data
    private Employee employee;


    public EmployeeUI(JFrame parent) {
        super(parent, "Add New Employee", true);
        employee = new Employee();  // Create a fresh instance
        setupUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void setupUI() {
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Form panel with grid
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BUTTON_COLOR),
                        "Employee Details",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        MAIN_FONT,
                        BUTTON_COLOR
                )
        ));

        // Create form fields
        nameField = createStyledField();
        empNumField = createStyledField();
        contactField = createStyledField();
        birthdayField = createStyledField();
        positionField = createStyledField();
        deptField = createStyledField();

        // GridBag constraints
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(8, 8, 8, 8);
        gc.weightx = 0.1;
        gc.gridx = 0;

        // Add form components with labels
        gc.gridy = 0; addFormRow(formPanel, "Employee Name:", nameField, gc);
        gc.gridy = 1; addFormRow(formPanel, "Employee Number:", empNumField, gc);
        gc.gridy = 2; addFormRow(formPanel, "Contact Number:", contactField, gc);
        gc.gridy = 3; addFormRow(formPanel, "Birthday (YYYY-MM-DD):", birthdayField, gc);
        gc.gridy = 4; addFormRow(formPanel, "Position:", positionField, gc);
        gc.gridy = 5; addFormRow(formPanel, "Department:", deptField, gc);

        // Submit button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 5, 0));
        submitBtn = createStyledButton("Submit");
        submitBtn.addActionListener(e -> handleSubmit());
        buttonPanel.add(submitBtn);

        // Add panels to dialog
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    private JTextField createStyledField() {
        JTextField field = new JTextField(20);
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

    private void addFormRow(JPanel panel, String labelText, JComponent field, GridBagConstraints gc) {
        JLabel label = new JLabel(labelText);
        label.setFont(MAIN_FONT);
        label.setForeground(BUTTON_COLOR);

        gc.gridx = 0;
        gc.weightx = 0.3;
        panel.add(label, gc);

        gc.gridx = 1;
        gc.weightx = 0.7;
        panel.add(field, gc);
    }

    private void handleSubmit() {
        Employee newEmployee = new Employee();  // Create new instance for submission

        //EMPLOYEE NAME TRY CATCH (Not Blank and No Numbers)
        try {
            String nameInput = nameField.getText().trim();

            // Check if name is empty or contains digits
            if (nameInput.isEmpty() || nameInput.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Name cannot be blank or contain numbers.");
            }

            newEmployee.setName(nameInput);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Invalid Name",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //EMPLOYEE NUMBER TRY CATCH (Not Blank)
        try {
            String EmployeeNumberInput = empNumField.getText().trim();

            // Check if Employee Number is empty or contains letters
            if (EmployeeNumberInput.isEmpty()) {
                throw new IllegalArgumentException("Employee Number cannot be blank.");
            }

            newEmployee.setEmployeeNumber(EmployeeNumberInput);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Invalid Employee Number",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //CONTACT INFO TRY CATCH (Not Blank and No Letters)
        try {
            String EmployeeContactInfoInput = contactField.getText().trim();

            // Check if Contact Info is empty or contains digits
            if (EmployeeContactInfoInput.isEmpty() || EmployeeContactInfoInput.matches(".*[a-zA-Z].*")) {
                throw new IllegalArgumentException("Contact Info cannot be blank or contain letters.");
            }

            newEmployee.setContactInfo(EmployeeContactInfoInput);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Invalid Contact Info",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //POSITION TRY CATCH (Not Blank)
        try {
            String PositionInput = positionField.getText().trim();

            // Check if Position is empty
            if (PositionInput.isEmpty()) {
                throw new IllegalArgumentException("Position cannot be blank.");
            }

            newEmployee.setPosition(PositionInput);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Invalid Poistion",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //DEPARTMENT TRY CATCH (Not Blank)
        try {
            String DepartmentInput = deptField.getText().trim();

            // Check if Position is empty
            if (DepartmentInput.isEmpty()) {
                throw new IllegalArgumentException("Department cannot be blank.");
            }

            newEmployee.setDepartment(DepartmentInput);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Invalid Department",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (birthdayField.getText().length() != 10 || birthdayField.getText().isEmpty()) {
                throw new IllegalArgumentException("Invalid birthday format. Please use YYYY-MM-DD");
            }

            else if (birthdayField.getText().length() == 10 && birthdayField.getText().isEmpty() == false) {
                String[] date = birthdayField.getText().split("-");
                if (date.length == 3) {
                    newEmployee.setBirthday(
                            Integer.parseInt(date[0]),
                            Integer.parseInt(date[1]),
                            Integer.parseInt(date[2])
                    );
                }
            }
            else {
                throw new IllegalArgumentException("Blank Entry or Invalid birthday format");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid birthday format. Please use YYYY-MM-DD",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        employee = newEmployee;  // Store the new instance
        EmployeeDatabase.addEmployee(employee);  // Add to database
        dispose();
    }


    public Employee getData() {
        setVisible(true);
        return employee != null ? new Employee(employee) : null;  // Return a copy
    }
} 
