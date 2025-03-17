package org.example.Presentation;

import javax.swing.*;
import java.awt.*;

public class AppUtility {
    public static void configureButton(JButton button){
        button.setPreferredSize(new Dimension(150,40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
    }
}
