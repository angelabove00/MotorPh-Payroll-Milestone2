package model;

public class Payroll {
    // Pay info
    private double hoursWorked;
    private double overtimeHrs;
    private double bonus;
    private double grossPay;    // before deductions
    private double netPay;      // after deductions
    private double loans;       // any loans to be paid
    private Deductions deduc;   // all the government deductions

    // Pay rates in pesos
    private static final double NORMAL_RATE = 100.0;  // per hour
    private static final double OT_RATE = 125.0;      // overtime rate

    // Simple getters/setters
    public void setHoursWorked(double hrs) { this.hoursWorked = hrs; }
    public double getHoursWorked() { return hoursWorked; }

    public void setOvertimeHours(double hrs) { this.overtimeHrs = hrs; }
    public double getOvertimeHours() { return overtimeHrs; }

    public void setBonuses(double amt) { this.bonus = amt; }
    public double getBonuses() { return bonus; }

    public void setLoans(double amt) { this.loans = amt; }
    public double getLoans() { return loans; }

    public void setDeductions(Deductions d) { this.deduc = d; }
    public Deductions getDeductions() { return deduc; }

    // Main calculation methods
    public double getTotalPay() {
        // Calculate total pay including overtime and bonus
        grossPay = (hoursWorked * NORMAL_RATE) + 
                  (overtimeHrs * OT_RATE) + 
                  bonus;
        return grossPay;
    }

    public double getFinalPay() {
        // Get final pay after all deductions
        if (deduc != null) {
            double totalDeduc = deduc.getTotalDeductions() + loans;
            netPay = getTotalPay() - totalDeduc;
            return netPay;
        }
        return getTotalPay(); // if no deductions set
    }

    public void calculateGrossSalary() {
        if (hoursWorked > 0) {
            grossPay = (hoursWorked * NORMAL_RATE) + 
                      (overtimeHrs * OT_RATE) + 
                      bonus;
        } else {
            grossPay = 0;
        }
    }
} 
