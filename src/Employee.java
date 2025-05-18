import java.time.LocalDate;

public class Employee {
    private String employeeNumber;
    private String name;
    private String contactInfo;
    private LocalDate birthday;
    private String position;
    private String department;

    // … other fields …

    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getBirthday() { return birthday.toString(); }
    public void setBirthday(int b_year, int b_month, int b_day) { birthday = LocalDate.of(b_year, b_month, b_day); }
}
