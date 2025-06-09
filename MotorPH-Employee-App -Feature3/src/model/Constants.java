package model;

public class Constants {
    // Pay rates
    public static final double NORMAL_RATE_PER_HOUR = 100.0;  // Base hourly rate
    public static final double OVERTIME_RATE_MULTIPLIER = 1.25;  // 25% additional for overtime
    
    // Standard working hours
    public static final double STANDARD_HOURS_PER_MONTH = 160.0;
    
    // Government deduction rates
    public static final double SSS_RATE = 0.045;          // 4.5% of gross
    public static final double PHILHEALTH_RATE = 0.035;   // 3.5% of gross
    public static final double PAGIBIG_RATE = 0.02;       // 2% of gross
    public static final double TAX_RATE = 0.15;           // 15% tax rate
    
    // Maximum values for deductions
    public static final double SSS_MAX_CONTRIBUTION = 3000.0;
    public static final double PHILHEALTH_MAX_CONTRIBUTION = 2000.0;
    public static final double PAGIBIG_MAX_CONTRIBUTION = 1000.0;
    
    // Input validation patterns
    public static final String SSS_NUMBER_PATTERN = "^\\d{2}-\\d{7}-\\d{1}$";  // XX-XXXXXXX-X
    public static final String PHILHEALTH_NUMBER_PATTERN = "^\\d{2}-\\d{9}-\\d{1}$";  // XX-XXXXXXXXX-X
    public static final String TIN_NUMBER_PATTERN = "^\\d{3}-\\d{3}-\\d{3}$";  // XXX-XXX-XXX
    public static final String PAGIBIG_NUMBER_PATTERN = "^\\d{4}-\\d{4}-\\d{4}$";  // XXXX-XXXX-XXXX
    public static final String CONTACT_NUMBER_PATTERN = "^09\\d{9}$";  // 09XXXXXXXXX

    // UI Messages
    public static final String REQUIRED_FIELDS_ERROR = "All fields are required.";
    public static final String DUPLICATE_EMP_NUM_ERROR = "Employee number already exists.";
    public static final String INVALID_CONTACT_ERROR = "Invalid contact number format. Must be 11 digits starting with '09'.";
    public static final String INVALID_SSS_ERROR = "Invalid SSS number format. Must be XX-XXXXXXX-X.";
    public static final String INVALID_PHILHEALTH_ERROR = "Invalid PhilHealth number format. Must be XX-XXXXXXXXX-X.";
    public static final String INVALID_TIN_ERROR = "Invalid TIN format. Must be XXX-XXX-XXX.";
    public static final String INVALID_PAGIBIG_ERROR = "Invalid Pag-IBIG number format. Must be XXXX-XXXX-XXXX.";
    public static final String NO_SELECTION_ERROR = "Please select an employee from the table.";
} 