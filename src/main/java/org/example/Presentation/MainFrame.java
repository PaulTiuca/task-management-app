package org.example.Presentation;

import org.example.Business.Controller;
import org.example.Data_Models.Employee;

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
    private Controller controller;

    public MainFrame(Controller controller) {
        this.setTitle("Task Manager App");
        this.controller = controller;
        this.setSize(700, 700);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel mainMenuPanel = createMainMenu();
        employeeTablePanel = new EmployeeTablePanel(this,controller);
        addEmployeesPanel = new AddEmployeesPanel(this,controller);
        createTaskPanel = new CreateTaskPanel(this,controller);
        assignTaskToEmployeePanel = new AssignTaskToEmployeePanel(this,controller);

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
        AppUtility.configureButton(addTaskButton);
        addTaskButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "CreateTaskPanel");
            createTaskPanel.updateTaskComboBox();
        });

        mainMenuPanel.add(Box.createVerticalGlue());
        mainMenuPanel.add(addTaskButton);

        JButton addEmployeesButton = new JButton("Add Employees");
        AppUtility.configureButton(addEmployeesButton);
        addEmployeesButton.addActionListener(e -> cardLayout.show(contentPanel, "AddEmployeesPanel"));

        mainMenuPanel.add(Box.createVerticalGlue());
        mainMenuPanel.add(addEmployeesButton);

        JButton assignTaskToEmployeeButton = new JButton("Assign task to employee");
        AppUtility.configureButton(assignTaskToEmployeeButton);
        assignTaskToEmployeeButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "AssignTaskToEmployeePanel");
            assignTaskToEmployeePanel.createComboBoxes();
        });

        mainMenuPanel.add(Box.createVerticalGlue());
        mainMenuPanel.add(assignTaskToEmployeeButton);

        JButton viewEmployeesButton = new JButton("View Employees");
        AppUtility.configureButton(viewEmployeesButton);
        viewEmployeesButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "EmployeeTablePanel");
            ArrayList<Employee> employees = new ArrayList<>(controller.getEmployeeTaskMap().keySet());
            employeeTablePanel.loadEmployeeData(employees);
        });

        mainMenuPanel.add(Box.createVerticalGlue());
        mainMenuPanel.add(viewEmployeesButton);

        mainMenuPanel.add(Box.createVerticalGlue());

        return mainMenuPanel;
    }
}
