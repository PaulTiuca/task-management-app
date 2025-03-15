package org.example;

import javax.swing.*;
import java.awt.*;

public class AddEmployeesPanel extends JPanel {
    public AddEmployeesPanel(MainFrame parentFrame, TasksManagement tasksManagement){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topMarginPanel = new JPanel();
        topMarginPanel.setPreferredSize(new Dimension(0,0));
        this.add(topMarginPanel);

        JLabel employeeNameLabel = new JLabel("Employee Name:");
        employeeNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(employeeNameLabel);

        JTextField employeeNameField = new JTextField();
        employeeNameField.setMaximumSize(new Dimension(305,20));
        this.add(employeeNameField);

        JButton confirmButton = new JButton("Add Employee");
        JButton backButton = new JButton("Back to Main Menu");
        App.configureButton(confirmButton);
        App.configureButton(backButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        confirmButton.addActionListener(e -> {
            String employeeName = employeeNameField.getText().trim();

            if(tasksManagement.isEmployeeValid(employeeName)) {
                JOptionPane.showMessageDialog(this, "The employee '" + employeeName + "' has been added succesfully!");
                tasksManagement.addEmployee(employeeName);
                employeeNameField.setText("");
            }
            else
                JOptionPane.showMessageDialog(this, "Please provide a valid name!");
        });
        backButton.addActionListener(e -> parentFrame.switchToMainPanel());

        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);

        this.add(buttonPanel);
    }
}
