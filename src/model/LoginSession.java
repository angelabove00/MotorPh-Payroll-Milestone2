import java.time.LocalTime;

public class LoginSession {
    private LocalTime loginTime;

    public LocalTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalTime loginTime) { this.loginTime = loginTime; }

    public boolean isLateLogin() {
        // TODO: implement grace‑period logic
        return false;
    }
}
