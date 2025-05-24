package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private String employeeNumber;
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private double totalHours;

    public Attendance() {
    }
    
    /**
     * Creates a deep copy of an Attendance record
     */
    public Attendance(Attendance other) {
        if (other != null) {
            this.employeeNumber = other.employeeNumber != null ? other.employeeNumber : null;
            this.date = other.date != null ? LocalDate.from(other.date) : null;
            this.timeIn = other.timeIn != null ? LocalTime.from(other.timeIn) : null;
            this.timeOut = other.timeOut != null ? LocalTime.from(other.timeOut) : null;
            this.totalHours = other.totalHours;
        }
    }
    
    /**
     * Creates a clone of this attendance record
     */
    @Override
    public Attendance clone() {
        return new Attendance(this);
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalTime timeOut) {
        this.timeOut = timeOut;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    // Convert time to minutes to make calculation easier
    public double calculateTotalHours() {
        if (timeIn != null && timeOut != null) {
            int startMinutes = timeIn.getHour() * 60 + timeIn.getMinute();
            int endMinutes = timeOut.getHour() * 60 + timeOut.getMinute();
            return (endMinutes - startMinutes) / 60.0;  // convert back to hours
        }
        return 0.0;
    }
} 
