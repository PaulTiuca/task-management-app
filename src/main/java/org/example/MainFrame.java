package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame{
    protected JPanel contentPanel;
    private CardLayout cardLayout;
    private EmployeeTablePanel employeeTablePanel;
    private AddEmployeesPanel addEmployeesPanel;
    private AssignTaskToEmployeePanel assignTaskToEmployeePanel;
    private CreateTaskPanel createTaskPanel;
    private TasksManagement tasksManagement;

    public MainFrame(TasksManagement tasksManagement) {
        this.setTitle("Task Manager App");
        this.tasksManagement = tasksManagement;
        this.setSize(700, 700);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel mainMenuPanel = createMainMenu();
        employeeTablePanel = new EmployeeTablePanel(this, tasksManagement);
        addEmployeesPanel = new AddEmployeesPanel(this,tasksManagement);
        createTaskPanel = new CreateTaskPanel(this,tasksManagement);
        assignTaskToEmployeePanel = new AssignTaskToEmployeePanel(this, tasksManagement);

        contentPanel.add(mainMenuPanel, "MainMenu");
        contentPanel.add(employeeTablePanel, "EmployeeTablePanel");
        contentPanel.add(addEmployeesPanel, "AddEmployeesPanel");
        contentPanel.add(createTaskPanel, "CreateTaskPanel");
        contentPanel.add(assignTaskToEmployeePanel, "AssignTaskToEmployeePanel");

        this.setContentPane(contentPanel);
        this.setVisible(true);
    }

    protected void switchToMainPanel() {
        cardLayout.show(contentPanel, "MainMenu");
    }

    private JPanel createMainMenu(){
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));

        JButton addTaskButton = new JButton("Create Task");
        App.configureButton(addTaskButton);
        addTaskButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "CreateTaskPanel");
            createTaskPanel.updateTaskComboBox();
        });

        mainMenuPanel.add(Box.createVerticalGlue());
        mainMenuPanel.add(addTaskButton);

        JButton addEmployeesButton = new JButton("Add Employees");
        App.configureButton(addEmployeesButton);
        addEmployeesButton.addActionListener(e -> cardLayout.show(contentPanel, "AddEmployeesPanel"));

        mainMenuPanel.add(Box.createVerticalGlue());
        mainMenuPanel.add(addEmployeesButton);

        JButton assignTaskToEmployeeButton = new JButton("Assign task to employee");
        App.configureButton(assignTaskToEmployeeButton);
        assignTaskToEmployeeButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "AssignTaskToEmployeePanel");
            assignTaskToEmployeePanel.createComboBoxes();
        });

        mainMenuPanel.add(Box.createVerticalGlue());
        mainMenuPanel.add(assignTaskToEmployeeButton);

        JButton viewEmployeesButton = new JButton("View Employees");
        App.configureButton(viewEmployeesButton);
        viewEmployeesButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "EmployeeTablePanel");
            ArrayList<Employee> employees = new ArrayList<>(tasksManagement.getMap().keySet());
            employeeTablePanel.loadEmployeeData(employees);
        });

        mainMenuPanel.add(Box.createVerticalGlue());
        mainMenuPanel.add(viewEmployeesButton);

        mainMenuPanel.add(Box.createVerticalGlue());

        return mainMenuPanel;
    }
}
