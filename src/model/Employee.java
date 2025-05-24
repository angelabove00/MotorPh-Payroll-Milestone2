package model;

import java.time.LocalDate;

public class Employee {
    private String employeeNumber;
    private String name;
    private String contactInfo;
    private LocalDate birthday;
    private String position;
    private String department;

    public Employee() {
    }
    
    public Employee(Employee other) {
        this.employeeNumber = other.employeeNumber;
        this.name = other.name;
        this.contactInfo = other.contactInfo;
        this.birthday = other.birthday != null ? LocalDate.from(other.birthday) : null;
        this.position = other.position;
        this.department = other.department;
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String empNum) { this.employeeNumber = empNum; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contact) { this.contactInfo = contact; }

    public String getPosition() { return position; }
    public void setPosition(String pos) { this.position = pos; }

    public String getDepartment() { return department; }
    public void setDepartment(String dept) { this.department = dept; }

    public String getBirthday() { 
        return birthday != null ? birthday.toString() : "Not set"; 
    }
    
    public void setBirthday(int year, int month, int day) { 
        birthday = LocalDate.of(year, month, day); 
    }
} 
