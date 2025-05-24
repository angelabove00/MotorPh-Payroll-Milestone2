package model;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDatabase {
    private static final List<Employee> employeeList = new ArrayList<>();
    
    // Add an employee to the database
    public static void addEmployee(Employee emp) {
        employeeList.add(new Employee(emp));
    }
    
    // Find employee by number
    public static Employee findByNumber(String empNum) {
        for (Employee emp : employeeList) {
            if (emp.getEmployeeNumber().equals(empNum)) {
                return new Employee(emp);
            }
        }
        return null;
    }
    
    // Get all employees
    public static List<Employee> getAllEmployees() {
        List<Employee> copies = new ArrayList<>();
        for (Employee emp : employeeList) {
            copies.add(new Employee(emp));
        }
        return copies;
    }
} 