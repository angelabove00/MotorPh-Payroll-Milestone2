package model;

import java.util.*;

public class AttendanceDatabase {
    private static final Map<String, List<Attendance>> employeeAttendance = new HashMap<>();
    
    public static void addRecord(Attendance record) {
        if (record == null || record.getEmployeeNumber() == null) return;
        
        String empNum = record.getEmployeeNumber().trim();
        Attendance copy = new Attendance(record);
        
        employeeAttendance.computeIfAbsent(empNum, k -> new ArrayList<>())
                         .add(copy);
    }
    
    public static List<Attendance> getRecordsForEmployee(String empNum) {
        if (empNum == null) return new ArrayList<>();
        
        empNum = empNum.trim();
        List<Attendance> records = employeeAttendance.get(empNum);
        
        if (records == null) return new ArrayList<>();
        
        return records.stream()
                     .map(Attendance::new)
                     .toList();
    }
    
    public static Optional<Attendance> getLatestRecord(String empNum) {
        if (empNum == null) return Optional.empty();
        
        List<Attendance> records = getRecordsForEmployee(empNum);
        if (records.isEmpty()) return Optional.empty();
        
        return records.stream()
                     .max(Comparator.comparing(Attendance::getDate))
                     .map(Attendance::new);
    }
}