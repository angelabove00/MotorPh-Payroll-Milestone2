package view;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class UIConstants {
    // Colors
    public static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final Color TEXT_COLOR = Color.BLACK;

    // Fonts
    public static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 12);

    // Dimensions
    public static final Dimension MAIN_WINDOW_SIZE = new Dimension(800, 600);
    public static final Dimension DETAIL_WINDOW_SIZE = new Dimension(500, 700);
    public static final Dimension NEW_EMPLOYEE_WINDOW_SIZE = new Dimension(500, 600);
    public static final int STANDARD_FIELD_WIDTH = 250;

    // Borders
    public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    public static final Border LINE_BORDER = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
} 