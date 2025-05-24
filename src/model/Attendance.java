import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;

    // Getter & setter for date
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTimeIn() { return timeIn; }
    public void setTimeIn(LocalTime timeIn) { this.timeIn = timeIn; }

    public LocalTime getTimeOut() { return timeOut; }
    public void setTimeOut(LocalTime timeOut) { this.timeOut = timeOut; }

    public double calculateTotalHours() {
        if (timeIn != null && timeOut != null) {
            return Duration.between(timeIn, timeOut).toHours();
        }
        return 0.0;
    }
}
