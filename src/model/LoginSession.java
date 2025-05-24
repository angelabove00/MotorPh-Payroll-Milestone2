package model;

import java.time.LocalTime;

public class LoginSession {
    private LocalTime loginTime;
    private LocalTime logoutTime;
    private boolean isActive;

    public LocalTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalTime time) {
        this.loginTime = time;
        this.isActive = true;
    }

    public LocalTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalTime logoutTime) {
        this.logoutTime = logoutTime;
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }
} 
