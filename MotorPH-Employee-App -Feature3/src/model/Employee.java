package model;

import java.time.LocalDate;

public class Employee {
    private final String employeeNumber;
    private final String firstName;
    private final String lastName;
    private final String contactInfo;
    private final String position;
    private final String department;
    private final LocalDate birthday;
    private final String sssNumber;
    private final String philhealthNumber;
    private final String tinNumber;
    private final String pagibigNumber;

    public Employee(String employeeNumber, String firstName, String lastName, String contactInfo,
                   String position, String department, LocalDate birthday, String sssNumber,
                   String philhealthNumber, String tinNumber, String pagibigNumber) {
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactInfo = contactInfo;
        this.position = position;
        this.department = department;
        this.birthday = birthday;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getContactInfo() { return contactInfo; }
    public String getPosition() { return position; }
    public String getDepartment() { return department; }
    public LocalDate getBirthday() { return birthday; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilhealthNumber() { return philhealthNumber; }
    public String getTinNumber() { return tinNumber; }
    public String getPagibigNumber() { return pagibigNumber; }

    // CSV conversion methods
    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
            employeeNumber, firstName, lastName, contactInfo, birthday.toString(),
            position, department, sssNumber, philhealthNumber, tinNumber, pagibigNumber);
    }
    
    public static Employee fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 11) {
            throw new IllegalArgumentException("Invalid CSV format: expected 11 fields, got " + parts.length);
        }
        
        try {
            Employee emp = new Employee(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[5].trim(),
                parts[6].trim(),
                LocalDate.parse(parts[4].trim()),
                parts[7].trim(),
                parts[8].trim(),
                parts[9].trim(),
                parts[10].trim()
            );
            
            return emp;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing CSV line: " + e.getMessage());
        }
    }
} 
