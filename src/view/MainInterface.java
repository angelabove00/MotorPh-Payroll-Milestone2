package view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import model.*;

public class MainInterface extends JFrame {
    private static final Color BG_COLOR = new Color(245, 247, 250, 200);
    private static final Color BUTTON_COLOR = new Color(41, 84, 153);
    private static final Color BUTTON_HOVER = new Color(51, 104, 183);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color INFO_BG = new Color(240, 244, 248, 230);
    
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font MONO_FONT = new Font("Consolas", Font.PLAIN, 14);
    
    private JButton addEmployeeBtn;
    private JButton refreshBtn;
    private JButton attendanceBtn;
    private JButton payrollBtn;
    private JTextArea infoArea;
    private JLabel statusLabel;
    private JLabel timeLabel;
    private JComboBox<String> employeeComboBox;
    
    private Employee currentEmployee;
    private Attendance todayAttendance;
    private LoginSession loginInfo;

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(new File("src/resources/motorphlogo.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
    
    public MainInterface() {
        super("MotorPH Employee System");
        
        currentEmployee = new Employee();
        todayAttendance = new Attendance();
        loginInfo = new LoginSession();
        loginInfo.setLoginTime(LocalTime.now());
        
        setupUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);  //--removed ability to resize jFrame!!
                              //  resizing the window biiger and then downsizing it back produces scroll bars that wont disappear even if you go smaller and i could not remove them for the life of me. 
                              //  disabling the ability to resize the window preserves the beauty of the jTextArea ont he code down below.
    }
    
    private void setupUI() {
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JPanel headerPanel = new JPanel(new BorderLayout(15, 15));
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        addEmployeeBtn = createStyledButton("Add Employee");
        refreshBtn = createStyledButton("Refresh");
        attendanceBtn = createStyledButton("Attendance");
        payrollBtn = createStyledButton("Payroll");
        
        buttonPanel.add(addEmployeeBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(attendanceBtn);
        buttonPanel.add(payrollBtn);
        
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setOpaque(false);
        
        JLabel selectLabel = new JLabel("Select Employee:");
        selectLabel.setFont(MAIN_FONT);
        selectLabel.setForeground(TEXT_COLOR);
        
        employeeComboBox = new JComboBox<>();
        employeeComboBox.setFont(MAIN_FONT);
        employeeComboBox.setBackground(Color.WHITE);
        employeeComboBox.addActionListener(e -> handleEmployeeSelection());
        updateEmployeeList();
        
        selectionPanel.add(selectLabel);
        selectionPanel.add(employeeComboBox);
        
        infoArea = new JTextArea();
        infoArea.setFont(MONO_FONT);
        infoArea.setBackground(INFO_BG);
        infoArea.setForeground(TEXT_COLOR);
        infoArea.setEditable(false);
        infoArea.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        infoArea.setOpaque(true);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.add(selectionPanel, BorderLayout.NORTH);
        infoPanel.add(infoArea, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 15, 15, 15),
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(BUTTON_COLOR, 1, true),
                    "System Information",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    MAIN_FONT,
                    BUTTON_COLOR
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
        ));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        statusPanel.setOpaque(false);
        statusPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        
        statusLabel = new JLabel("Status: Ready");
        timeLabel = new JLabel("Login time: " + loginInfo.getLoginTime());
        styleLabel(statusLabel);
        styleLabel(timeLabel);
        statusPanel.add(statusLabel);
        statusPanel.add(timeLabel);
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);
        contentPanel.add(buttonPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(statusPanel, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        backgroundPanel.add(mainPanel);
        
        addEmployeeBtn.addActionListener(e -> addEmployeeBtnClicked(e));
        refreshBtn.addActionListener(e -> refreshBtnClicked(e));
        attendanceBtn.addActionListener(e -> attendanceBtnClicked(e));
        payrollBtn.addActionListener(e -> payrollBtnClicked(e));
        
        setContentPane(backgroundPanel);
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
        
        updateDisplay();
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(BUTTON_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(BUTTON_HOVER);
                } else {
                    g2.setColor(BUTTON_COLOR);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(MAIN_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        Dimension size = button.getPreferredSize();
        size.width += 30;
        size.height += 10;
        button.setPreferredSize(size);
        
        return button;
    }
    
    private void styleLabel(JLabel label) {
        label.setFont(MAIN_FONT);
        label.setForeground(TEXT_COLOR);
    }

    private void addEmployeeBtnClicked(java.awt.event.ActionEvent evt) {
        EmployeeUI empWindow = new EmployeeUI(this);
        Employee newEmployee = empWindow.getData();
        if (newEmployee != null) {
            currentEmployee = new Employee(newEmployee);
            updateDisplay();
        }
    }

    private void refreshBtnClicked(java.awt.event.ActionEvent evt) {
        updateEmployeeList();
        updateDisplay();
    }

    private void attendanceBtnClicked(java.awt.event.ActionEvent evt) {
        if (currentEmployee == null || currentEmployee.getEmployeeNumber() == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an employee first",
                "No Employee Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        AttendanceUI attWindow = new AttendanceUI(this);
        Attendance newAttendance = attWindow.getData();
        
        if (newAttendance != null) {
            newAttendance.setEmployeeNumber(currentEmployee.getEmployeeNumber());
            AttendanceDatabase.addRecord(newAttendance);
            updateDisplay();
        }
    }

    private void payrollBtnClicked(java.awt.event.ActionEvent evt) {
        if (currentEmployee == null || currentEmployee.getName() == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an employee first",
                "No Employee Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        PayrollUI payWindow = new PayrollUI(this, currentEmployee);
        payWindow.setVisible(true);
    }
    
    private void updateDisplay() {
        StringBuilder info = new StringBuilder();
        info.append("Current Employee: ");
        info.append(currentEmployee.getName() != null ? currentEmployee.getName() : "None");
        info.append("\n\n");
        
        if (currentEmployee.getEmployeeNumber() != null) {
            info.append("Today's Attendance:\n");
            Optional<Attendance> latest = AttendanceDatabase.getLatestRecord(currentEmployee.getEmployeeNumber());
            
            if (latest.isPresent()) {
                Attendance record = latest.get();
                info.append(String.format("Date: %s\n", record.getDate()));
                info.append(String.format("Time In: %s\n", record.getTimeIn()));
                info.append(String.format("Time Out: %s\n", record.getTimeOut()));
                info.append(String.format("Total Hours: %.2f\n", record.calculateTotalHours()));
                
                List<Attendance> allRecords = AttendanceDatabase.getRecordsForEmployee(currentEmployee.getEmployeeNumber());
                if (allRecords.size() > 1) {
                    info.append("\nAll Records Today:\n");
                    allRecords.stream()
                            .filter(a -> a.getDate().equals(LocalDate.now()))
                            .forEach(a -> info.append(String.format("  %s - %s (%.2f hrs)\n",
                                    a.getTimeIn(), a.getTimeOut(), a.calculateTotalHours())));
                }
            } else {
                info.append("No attendance recorded today\n");
            }
        }
        
        infoArea.setText(info.toString());
        statusLabel.setText("Status: Updated at " + LocalTime.now().toString());
    }
    
    private void updateEmployeeList() {
        employeeComboBox.removeAllItems();
        employeeComboBox.addItem("Select an employee...");
        EmployeeDatabase.getAllEmployees().forEach(emp -> 
            employeeComboBox.addItem(emp.getName())
        );
    }
    
    private void handleEmployeeSelection() {
        int selectedIndex = employeeComboBox.getSelectedIndex();
        if (selectedIndex > 0) {
            String selectedName = (String) employeeComboBox.getSelectedItem();
            List<Employee> employees = EmployeeDatabase.getAllEmployees();
            employees.stream()
                    .filter(emp -> emp.getName().equals(selectedName))
                    .findFirst()
                    .ifPresent(emp -> {
                        currentEmployee = new Employee(emp);
                        updateDisplay();
                    });
        }
    }
}
