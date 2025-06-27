package view;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import model.Constants;
import model.Employee;
import model.EmployeeDatabase;

public class EmployeeMainFrame extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JButton viewButton;
    private JButton newButton;
    private JButton adminPanelButton;

    public EmployeeMainFrame() {
        System.out.println("DEBUG: EmployeeMainFrame constructor invoked.");
        System.out.println("EmployeeMainFrame constructor entered.");
        setTitle("MotorPH Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(UIConstants.MAIN_WINDOW_SIZE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        // Set application icon
        try {
            System.out.println("Loading application icon...");
            java.net.URL iconUrl = EmployeeMainFrame.class.getResource("/view/assets/logo.png");
            if (iconUrl == null) {
                System.err.println("ERROR: Icon resource not found in classpath: /view/assets/logo.png");
                // Try alternate path
                iconUrl = EmployeeMainFrame.class.getResource("assets/logo.png");
                if (iconUrl == null) {
                    System.err.println("ERROR: Icon not found in alternate path: assets/logo.png");
                }
            }
            if (iconUrl != null) {
                System.out.println("Icon found at: " + iconUrl);
                ImageIcon icon = new ImageIcon(iconUrl);
                setIconImage(icon.getImage());
            }
        } catch (Exception e) {
            System.err.println("ERROR loading application icon: " + e.getMessage());
            e.printStackTrace();
        }

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create and add components
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);

        setContentPane(mainPanel);
        refreshTable();
    }

    private JPanel createHeaderPanel() {
        // Create main header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        // Create logo and title panel with FlowLayout.LEFT
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Load and scale the logo
        try {
            System.out.println("Loading header logo...");
            java.net.URL logoUrl = EmployeeMainFrame.class.getResource("/view/assets/logo.png");
            if (logoUrl == null) {
                System.err.println("ERROR: Logo resource not found in classpath: /view/assets/logo.png");
                // Try alternate path
                logoUrl = EmployeeMainFrame.class.getResource("assets/logo.png");
                if (logoUrl == null) {
                    System.err.println("ERROR: Logo not found in alternate path: assets/logo.png");
                }
            }
            if (logoUrl != null) {
                System.out.println("Logo found at: " + logoUrl);
                ImageIcon logo = new ImageIcon(logoUrl);
                if (logo.getIconWidth() > 0) {
                    Image img = logo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    JLabel logoLabel = new JLabel(new ImageIcon(img));
                    logoLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
                    titlePanel.add(logoLabel);
                } else {
                    System.err.println("ERROR: Logo image appears to be invalid (width <= 0)");
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR loading logo: " + e.getMessage());
            e.printStackTrace();
        }

        // Create title label
        JLabel titleLabel = new JLabel("MotorPH Employee Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        contentPanel.add(createControlPanel(), BorderLayout.NORTH);
        contentPanel.add(createTablePanel(), BorderLayout.CENTER);
        return contentPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout(10, 0));
        controlPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(UIConstants.LABEL_FONT);
        
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(290, 25));
        searchField.setToolTipText("Search by name or employee number");
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        viewButton = new JButton("View Details");
        newButton = new JButton("New Employee");
        adminPanelButton = new JButton("Admin Panel");
        
        // Style buttons
        Dimension buttonSize = new Dimension(120, 30);
        viewButton.setPreferredSize(buttonSize);
        newButton.setPreferredSize(buttonSize);
        adminPanelButton.setPreferredSize(buttonSize);
        
        viewButton.setFont(UIConstants.BUTTON_FONT);
        newButton.setFont(UIConstants.BUTTON_FONT);
        adminPanelButton.setFont(UIConstants.BUTTON_FONT);
        
        viewButton.addActionListener(e -> viewSelectedEmployee());
        newButton.addActionListener(e -> openNewEmployeeForm());
        adminPanelButton.addActionListener(e -> openAdminPanel());

        buttonPanel.add(viewButton);
        buttonPanel.add(newButton);
        buttonPanel.add(adminPanelButton);

        controlPanel.add(searchPanel, BorderLayout.WEST);
        controlPanel.add(buttonPanel, BorderLayout.EAST);

        // Add search functionality
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }
        });

        return controlPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {
            "Employee No.", 
            "Last Name", 
            "First Name", 
            "SSS No.", 
            "PhilHealth No.", 
            "TIN", 
            "Pag-IBIG No."
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowSelectionAllowed(true);
        employeeTable.setDragEnabled(false);
        // Prevent row reordering by drag and drop as well as switching columns
        employeeTable.getTableHeader().setReorderingAllowed(false);

        employeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        employeeTable.setRowHeight(25);
        employeeTable.setIntercellSpacing(new Dimension(10, 5));
        employeeTable.setShowGrid(true);
        employeeTable.setGridColor(new Color(230, 230, 230));
        employeeTable.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Style the header
        JTableHeader header = employeeTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBackground(new Color(245, 245, 245));
        header.setForeground(Color.DARK_GRAY);
        header.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Add alternating row colors
        employeeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
                }
                
                setBorder(new EmptyBorder(5, 5, 5, 5));
                return c;
            }
        });

        // Set up sorting
        sorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(sorter);

        // Add double-click listener
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelectedEmployee();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return scrollPane;
    }

    private void search() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0); // Clear existing rows
        List<Employee> employees = EmployeeDatabase.getEmployees();
        for (Employee emp : employees) {
            Object[] rowData = {
                emp.getEmployeeNumber(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getSssNumber(),
                emp.getPhilhealthNumber(),
                emp.getTinNumber(),
                emp.getPagibigNumber()
            };
            tableModel.addRow(rowData);
        }
    }

    private void viewSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedRow = employeeTable.convertRowIndexToModel(selectedRow);
            String empNum = (String) tableModel.getValueAt(selectedRow, 0);
            Employee employee = EmployeeDatabase.findByEmployeeNumber(empNum);
            if (employee != null) {
                EmployeeDetailFrame detailFrame = new EmployeeDetailFrame(employee);
                detailFrame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                Constants.NO_SELECTION_ERROR,
                "No Selection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openNewEmployeeForm() {
        NewEmployeeFrame newEmpFrame = new NewEmployeeFrame(this);
        newEmpFrame.setVisible(true);
    }

    private void openAdminPanel() {
        AdminPanelUI adminPanel = new AdminPanelUI(this);
        adminPanel.setVisible(true);
    }

}
