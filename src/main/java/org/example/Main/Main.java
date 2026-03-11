package org.example.Main;

import org.example.Business.Controller;
import org.example.Business.TasksManagement;
import org.example.DataAccess.SerializationHandler;
import org.example.Presentation.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        TasksManagement tasksManagement = SerializationHandler.loadData();
        if(tasksManagement == null)
            tasksManagement = new TasksManagement();

        Controller controller = new Controller(tasksManagement);
        tasksManagement.setStartingEmployeeId();

        TasksManagement serializedTasksManagement = tasksManagement;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> SerializationHandler.saveData(serializedTasksManagement)));
        SwingUtilities.invokeLater(() -> new MainFrame(controller));
    }
}