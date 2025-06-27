package model;

//import java.time.LocalDate;

public class Employee {
    private final String employeeNumber;
    private final String lastName;
    private final String firstName;
    private final String employeeBirthday;
    private final String employeeAddress;
    private final String contactInfo;
    private final String sssNumber;
    private final String philhealthNumber;
    private final String tinNumber;
    private final String pagibigNumber;
    private final String employeeStatus;
    private final String employeePosition;
    private final String employeeBoss;
    private final String employeeSalarybase;
    private final String riceSubsidy;
    private final String phoneAllowance;
    private final String clothingAllowance;
    private final String grossSemiRate;
    private final String hourlyRate;


    public Employee(
                        String employeeNumber,
                        String lastName,
                        String firstName,
                        String employeeBirthday,
                        String employeeAddress,
                        String contactInfo,
                        String sssNumber,
                        String philhealthNumber,
                        String tinNumber,
                        String pagibigNumber,
                        String employeeStatus,
                        String employeePosition,
                        String employeeBoss,
                        String employeeSalarybase,
                        String riceSubsidy,
                        String phoneAllowance,
                        String clothingAllowance,
                        String grossSemiRate,
                        String hourlyRate
                ) 
    {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.employeeBirthday = employeeBirthday;
        this.employeeAddress = employeeAddress;
        this.contactInfo = contactInfo;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
        this.employeeStatus = employeeStatus;
        this.employeePosition = employeePosition;
        this.employeeBoss = employeeBoss;
        this.employeeSalarybase = employeeSalarybase;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiRate = grossSemiRate;
        this.hourlyRate = hourlyRate;
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getBirthday() { return employeeBirthday; }
    public String getAddress() { return employeeAddress; }
    public String getContactInfo() { return contactInfo; }
    
    public String getSssNumber() { return sssNumber; }
    public String getPhilhealthNumber() { return philhealthNumber; }
    public String getTinNumber() { return tinNumber; }
    public String getPagibigNumber() { return pagibigNumber; }
    public String getStatus() { return employeeStatus; }
    public String getPosition() { return employeePosition; }
    public String getSupervisor() { return employeeBoss; }

    public String getBaseSalary() { return employeeSalarybase; }
    public String getRiceSubsidy() { return riceSubsidy; }
    public String getPhoneAllowance() { return phoneAllowance; }
    public String getClothingAllowance() { return clothingAllowance; }
    public String getSemiRate() { return grossSemiRate; }
    public String getHourlyRate() { return hourlyRate; }

    // CSV conversion methods
    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
            employeeNumber,
            lastName,
            firstName,
            employeeBirthday,
            employeeAddress,
            contactInfo,
            sssNumber,
            philhealthNumber,
            tinNumber,
            pagibigNumber,
            employeeStatus,
            employeePosition,
            employeeBoss,
            employeeSalarybase,
            riceSubsidy,
            phoneAllowance,
            clothingAllowance,
            grossSemiRate,
            hourlyRate
        );
    }
    
    public static Employee fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        if (parts.length != 19) {
            throw new IllegalArgumentException("Invalid CSV format: expected 19 fields, got " + parts.length);
        }
        
        try {
            Employee emp = new Employee(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[4].trim(),
                parts[5].trim(),
                parts[6].trim(),
                parts[7].trim(),
                parts[8].trim(),
                parts[9].trim(),
                parts[10].trim(),
                parts[11].trim(),
                parts[12].trim(),
                parts[13].trim(),
                parts[14].trim(),
                parts[15].trim(),
                parts[16].trim(),
                parts[17].trim(),
                parts[18].trim()
            );
            
            return emp;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing CSV line: " + e.getMessage());
        }
    }
}
