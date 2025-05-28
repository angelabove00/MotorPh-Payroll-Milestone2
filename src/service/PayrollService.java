package service;

import java.time.Month;
import model.Constants;
import model.Employee;

public class PayrollService {
    public static class PayrollResult {
        private final double basicPay;
        private final double overtimeHours;
        private final double overtimePay;
        private final double bonus;
        private final double grossPay;
        private final double sssDeduction;
        private final double philhealthDeduction;
        private final double pagibigDeduction;
        private final double taxDeduction;
        private final double loans;
        private final double netPay;

        public PayrollResult(double basicPay, double overtimeHours, double overtimePay,
                           double bonus, double grossPay, double sssDeduction,
                           double philhealthDeduction, double pagibigDeduction,
                           double taxDeduction, double loans, double netPay) {
            this.basicPay = basicPay;
            this.overtimeHours = overtimeHours;
            this.overtimePay = overtimePay;
            this.bonus = bonus;
            this.grossPay = grossPay;
            this.sssDeduction = sssDeduction;
            this.philhealthDeduction = philhealthDeduction;
            this.pagibigDeduction = pagibigDeduction;
            this.taxDeduction = taxDeduction;
            this.loans = loans;
            this.netPay = netPay;
        }

        public double getBasicPay() { return basicPay; }
        public double getOvertimeHours() { return overtimeHours; }
        public double getOvertimePay() { return overtimePay; }
        public double getBonus() { return bonus; }
        public double getGrossPay() { return grossPay; }
        public double getSssDeduction() { return sssDeduction; }
        public double getPhilhealthDeduction() { return philhealthDeduction; }
        public double getPagibigDeduction() { return pagibigDeduction; }
        public double getTaxDeduction() { return taxDeduction; }
        public double getLoans() { return loans; }
        public double getNetPay() { return netPay; }
    }

    public static PayrollResult computePayroll(Employee employee, Month month,
                                             double hoursWorked, double overtimeHours,
                                             double bonus, double loans) {
        // Basic pay calculation
        double basicPay = hoursWorked * Constants.NORMAL_RATE_PER_HOUR;
        
        // Overtime pay calculation
        double overtimePay = overtimeHours * Constants.NORMAL_RATE_PER_HOUR * Constants.OVERTIME_RATE_MULTIPLIER;
        
        // Gross pay calculation
        double grossPay = basicPay + overtimePay + bonus;
        
        // Government deductions with maximum limits
        double sssDeduction = Math.min(
            grossPay * Constants.SSS_RATE,
            Constants.SSS_MAX_CONTRIBUTION
        );
        
        double philhealthDeduction = Math.min(
            grossPay * Constants.PHILHEALTH_RATE,
            Constants.PHILHEALTH_MAX_CONTRIBUTION
        );
        
        double pagibigDeduction = Math.min(
            grossPay * Constants.PAGIBIG_RATE,
            Constants.PAGIBIG_MAX_CONTRIBUTION
        );
        
        // Calculate total government deductions
        double govtDeductions = sssDeduction + philhealthDeduction + pagibigDeduction;
        
        // Calculate taxable income and tax
        double taxableIncome = grossPay - govtDeductions;
        double taxDeduction = taxableIncome * Constants.TAX_RATE;
        
        // Calculate net pay
        double totalDeductions = govtDeductions + taxDeduction + loans;
        double netPay = grossPay - totalDeductions;
        
        return new PayrollResult(
            basicPay,
            overtimeHours,
            overtimePay,
            bonus,
            grossPay,
            sssDeduction,
            philhealthDeduction,
            pagibigDeduction,
            taxDeduction,
            loans,
            netPay
        );
    }
} 