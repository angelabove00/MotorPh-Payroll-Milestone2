package model;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDatabase {
    private static final String CSV_FILE = "src/resources/employees.csv";
    private static List<Employee> employees = new ArrayList<>();
    private static boolean isInitialized = false;

    public static void initialize() {
        if (!isInitialized) {
            loadEmployees();
            isInitialized = true;
        }
    }

    public static List<Employee> getEmployees() {
        initialize();
        return new ArrayList<>(employees);
    }

    public static Employee findByEmployeeNumber(String employeeNumber) {
        initialize();
        return employees.stream()
                .filter(e -> e.getEmployeeNumber().equals(employeeNumber))
                .findFirst()
                .orElse(null);
    }

    public static void addEmployee(Employee employee) {
        initialize();
        if (findByEmployeeNumber(employee.getEmployeeNumber()) != null) {
            throw new IllegalArgumentException("Employee number already exists");
        }
        employees.add(employee);
        saveEmployees();
    }

    public static void loadEmployees() {
        employees.clear();
        File file = new File(CSV_FILE);
        
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 11) continue;

                    Employee emp = new Employee(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim(),
                        LocalDate.parse(parts[6].trim()),
                        parts[7].trim(),
                        parts[8].trim(),
                        parts[9].trim(),
                        parts[10].trim()
                    );
                    employees.add(emp);
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employees file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void saveEmployees() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Write header
            writer.println("Employee Number,First Name,Last Name,Contact Info,Position,Department,Birthday,SSS Number,PhilHealth Number,TIN,Pag-IBIG Number");
            
            // Write employees
            for (Employee emp : employees) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    emp.getEmployeeNumber(),
                    emp.getFirstName(),
                    emp.getLastName(),
                    emp.getContactInfo(),
                    emp.getPosition(),
                    emp.getDepartment(),
                    emp.getBirthday(),
                    emp.getSssNumber(),
                    emp.getPhilhealthNumber(),
                    emp.getTinNumber(),
                    emp.getPagibigNumber()
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving employees: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void updateEmployee(Employee employee) throws IOException {
        Employee existingEmployee = findByEmployeeNumber(employee.getEmployeeNumber());
        if (existingEmployee != null) {
            employees.remove(existingEmployee);
            employees.add(employee);
            saveEmployees();
        }
    }

    public static void deleteEmployee(String employeeNumber) throws IOException {
        Employee employee = findByEmployeeNumber(employeeNumber);
        if (employee != null) {
            employees.remove(employee);
            saveEmployees();
        }
    }
} 