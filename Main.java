import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        Employee emp = new Employee();
        emp.setName("Juan Dela Cruz");
        emp.setEmployeeNumber("EMP001");
        emp.setContactInfo("09171234567");
        System.out.println("Employee: " + emp.getName() + ", ID: " + emp.getEmployeeNumber());

        Attendance att = new Attendance();
        att.setDate(LocalDate.now());
        att.setTimeIn(LocalTime.of(8, 0));
        att.setTimeOut(LocalTime.of(17, 30));
        System.out.println("Hours worked: " + att.calculateTotalHours());

        Deductions ded = new Deductions();
        ded.setSss(100); ded.setPhilHealth(50);
        ded.setPagIbig(30); ded.setWithholdingTax(200);
        ded.setOtherDeductions(20);

        Payroll pay = new Payroll();
        pay.setTotalHoursWorked(att.calculateTotalHours());
        pay.setOvertimeHours(1.5);
        pay.setBonuses(200);
        pay.setDeductions(ded);
        System.out.println("Gross Salary: " + pay.calculateGrossSalary() + " PHP");
        System.out.println("Net Salary:   " + pay.calculateNetSalary() + " PHP");
    }
}
