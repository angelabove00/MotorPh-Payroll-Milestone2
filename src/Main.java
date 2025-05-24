//
// MotorPH Employee Application Portal Main Class
//

// all UI-related classes are now moved to view package
// all modules moved to model package
import view.MainInterface;

public class Main {
    public static void main(String[] args) {
        MainInterface window = new MainInterface();
        window.setVisible(true);
    }
}
