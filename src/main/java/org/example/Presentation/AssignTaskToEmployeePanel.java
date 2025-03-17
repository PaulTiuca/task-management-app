package org.example.Presentation;

import org.example.Business.Controller;
import org.example.Data_Models.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AssignTaskToEmployeePanel extends JPanel {
    private Controller controller;
    private JPanel comboBoxPanel;
    private JComboBox<Task> taskComboBox;
    private JComboBox<Employee> employeeComboBox;

    public AssignTaskToEmployeePanel(MainFrame parentFrame, Controller controller){
        this.controller = controller;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topMarginPanel = new JPanel();
        topMarginPanel.setPreferredSize(new Dimension(0,0));
        this.add(topMarginPanel);

        JLabel label = new JLabel("Assign task to employee:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(label);

        comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.X_AXIS));
        this.add(comboBoxPanel);

        createComboBoxes();

        JButton confirmButton = new JButton("Confirm Assignation");
        JButton backButton = new JButton("Back to Main Menu");
        AppUtility.configureButton(confirmButton);
        AppUtility.configureButton(backButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        confirmButton.addActionListener(e -> {
            Task selectedTask = (Task) taskComboBox.getSelectedItem();
            Employee selectedEmployee = (Employee) employeeComboBox.getSelectedItem();

            if (selectedTask != null && selectedEmployee != null) {
                controller.assignTaskToEmployee(selectedEmployee, selectedTask);

                DefaultComboBoxModel<Task> model = (DefaultComboBoxModel<Task>) taskComboBox.getModel();
                model.removeElement(selectedTask);

                JOptionPane.showMessageDialog(this, "Task assigned successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select both a task and an employee.");
            }
        });
        backButton.addActionListener(e -> parentFrame.switchToMainPanel());

        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);

        this.add(buttonPanel);
    }

    protected void createComboBoxes() {
        comboBoxPanel.removeAll();

        ArrayList<Task> unassignedTasks = controller.getUnassignedTasks();
        DefaultComboBoxModel<Task> taskComboBoxModel = new DefaultComboBoxModel<>();
        for (Task task : unassignedTasks) {
            taskComboBoxModel.addElement(task);
        }

        taskComboBox = new JComboBox<>(taskComboBoxModel);
        taskComboBox.setMaximumSize(new Dimension(200, 30));

        taskComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBoxPanel.add(taskComboBox);

        DefaultComboBoxModel<Employee> employeeComboBoxModel = new DefaultComboBoxModel<>();
        for (Employee employee : controller.getEmployeeTaskMap().keySet()) {
            employeeComboBoxModel.addElement(employee);
        }

        employeeComboBox = new JComboBox<>(employeeComboBoxModel);
        employeeComboBox.setMaximumSize(new Dimension(200, 30));
        employeeComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBoxPanel.add(employeeComboBox);

        comboBoxPanel.revalidate();
        comboBoxPanel.repaint();
    }
}
