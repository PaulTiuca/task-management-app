package org.example;

import javax.swing.*;
import java.awt.*;

public class App {
    public App(){
        TasksManagement tasksManagement = new TasksManagement();
        Utility.tasksManagement = tasksManagement;
        MainFrame mainFrame = new MainFrame(tasksManagement);
    }

    protected static void configureButton(JButton button){
        button.setPreferredSize(new Dimension(150,40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
    }
}
