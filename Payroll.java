public class Payroll {
    private double totalHoursWorked;
    private double overtimeHours;
    private double bonuses;
    private double grossSalary;
    private double netSalary;
    private double loanRepayments;
    private Deductions deductions;

    // Setters & getters for fields used in Main
    public void setTotalHoursWorked(double h) { this.totalHoursWorked = h; }
    public double getTotalHoursWorked() { return totalHoursWorked; }

    public void setOvertimeHours(double o) { this.overtimeHours = o; }
    public double getOvertimeHours() { return overtimeHours; }

    public void setBonuses(double b) { this.bonuses = b; }
    public double getBonuses() { return bonuses; }

    public void setLoanRepayments(double l) { this.loanRepayments = l; }
    public double getLoanRepayments() { return loanRepayments; }

    public void setDeductions(Deductions d) { this.deductions = d; }
    public Deductions getDeductions() { return deductions; }

    public double calculateGrossSalary() {
        grossSalary = (totalHoursWorked * 100) + (overtimeHours * 125) + bonuses;
        return grossSalary;
    }

    public double calculateNetSalary() {
        double totalDeductions = deductions.getTotalDeductions() + loanRepayments;
        netSalary = calculateGrossSalary() - totalDeductions;
        return netSalary;
    }
}
