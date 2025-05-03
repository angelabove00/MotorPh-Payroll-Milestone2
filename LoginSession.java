public class LoginSession {
    private String loginTime;

    public String getLoginTime() { return loginTime; }
    public void setLoginTime(String loginTime) { this.loginTime = loginTime; }

    public boolean isLateLogin() {
        // TODO: implement grace‑period logic
        return false;
    }
}
