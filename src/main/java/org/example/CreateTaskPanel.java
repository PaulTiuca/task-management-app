package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CreateTaskPanel extends JPanel {
    private JComboBox<String> taskTypeComboBox;
    private JPanel taskDetailsPanel;
    private JTextField startHourField;
    private JTextField endHourField;
    private JTextField descriptionField;
    private JTextField complexTaskNameField;
    private TasksManagement tasksManagement;
    private DefaultListModel<Task> selectedTasksModel;
    private JList<Task> selectedTasksList;
    private JComboBox<Task> taskComboBox;


    public CreateTaskPanel(MainFrame parentFrame, TasksManagement tasksManagement) {
        this.tasksManagement = tasksManagement;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel taskTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String[] taskTypes = { "Simple Task", "Complex Task" };
        taskTypeComboBox = new JComboBox<>(taskTypes);
        taskTypeComboBox.setSelectedIndex(0);

        taskTypePanel.add(new JLabel("Select Task Type:"));
        taskTypePanel.add(taskTypeComboBox);

        this.add(taskTypePanel);

        taskDetailsPanel = new JPanel();
        taskDetailsPanel.setLayout(new BoxLayout(taskDetailsPanel, BoxLayout.Y_AXIS));
        this.add(taskDetailsPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton createTaskButton = new JButton("Create Task");
        App.configureButton(createTaskButton);
        createTaskButton.addActionListener(e -> {
            createTask();
        });

        JButton backButton = new JButton("Back to Main Menu");
        App.configureButton(backButton);
        backButton.addActionListener(e -> parentFrame.switchToMainPanel());

        buttonPanel.add(createTaskButton);
        buttonPanel.add(backButton);
        this.add(buttonPanel);

        taskTypeComboBox.addActionListener(e -> updateTaskDetailsPanel());
        updateTaskDetailsPanel();
    }

    private void updateTaskDetailsPanel() {
        taskDetailsPanel.removeAll();
        String selectedTaskType = (String) taskTypeComboBox.getSelectedItem();

        if ("Simple Task".equals(selectedTaskType)) {
            createSimpleTaskPanel();
        }
        else if ("Complex Task".equals(selectedTaskType)) {
            createComplexTaskPanel();
        }

        taskDetailsPanel.revalidate();
        taskDetailsPanel.repaint();
    }

    private void createSimpleTaskPanel(){
        JLabel titleLabel = new JLabel("Create Simple Task");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel startHourLabel = new JLabel("Start Hour:");
        startHourLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        startHourField = new JTextField();
        startHourField.setMaximumSize(new Dimension(100,20));

        JLabel endHourLabel = new JLabel("End Hour:");
        endHourLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        endHourField = new JTextField();
        endHourField.setMaximumSize(new Dimension(100,20));

        JLabel descriptionLabel = new JLabel("Task's Name/Description:");
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        descriptionField = new JTextField();
        descriptionField.setMaximumSize(new Dimension(305,20));

        taskDetailsPanel.add(titleLabel);
        taskDetailsPanel.add(startHourLabel);
        taskDetailsPanel.add(startHourField);
        taskDetailsPanel.add(endHourLabel);
        taskDetailsPanel.add(endHourField);
        taskDetailsPanel.add(descriptionLabel);
        taskDetailsPanel.add(descriptionField);
    }

    private void createComplexTaskPanel(){
        JLabel titleLabel = new JLabel("Create Complex Task");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Task Name/Description:");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        complexTaskNameField = new JTextField();
        complexTaskNameField.setMaximumSize(new Dimension(200, 30));

        ArrayList<Task> availableTasks = tasksManagement.getUnassignedTasks();
        DefaultComboBoxModel<Task> taskComboBoxModel = new DefaultComboBoxModel<>();
        for (Task task : availableTasks) {
            taskComboBoxModel.addElement(task);
        }

        taskComboBox = new JComboBox<>(taskComboBoxModel);
        taskComboBox.setMaximumSize(new Dimension(200, 30));

        selectedTasksModel = new DefaultListModel<>();
        selectedTasksList = new JList<>(selectedTasksModel);
        JScrollPane listScrollPane = new JScrollPane(selectedTasksList);
        listScrollPane.setPreferredSize(new Dimension(200, 100));

        JButton addTaskButton = new JButton("Add Sub-Task");
        addTaskButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        App.configureButton(addTaskButton);
        addTaskButton.addActionListener(e -> {
            Task selectedTask = (Task) taskComboBox.getSelectedItem();
            if (selectedTask != null && !selectedTasksModel.contains(selectedTask)) {
                selectedTasksModel.addElement(selectedTask);
                taskComboBoxModel.removeElement(selectedTask);
            }
        });

        taskDetailsPanel.add(titleLabel);
        taskDetailsPanel.add(nameLabel);
        taskDetailsPanel.add(complexTaskNameField);
        taskDetailsPanel.add(taskComboBox);
        taskDetailsPanel.add(addTaskButton);
        taskDetailsPanel.add(listScrollPane);
    }

    private void createTask() {
        String selectedTaskType = (String) taskTypeComboBox.getSelectedItem();

        if ("Simple Task".equals(selectedTaskType)) {
            String taskName = descriptionField.getText().trim();
            String startHourText = startHourField.getText().trim();
            String endHourText = endHourField.getText().trim();

            if(tasksManagement.isSimpleTaskValid(taskName,startHourText,endHourText)){
                int startHour = Integer.parseInt(startHourText);
                int endHour = Integer.parseInt(endHourText);
                tasksManagement.createTask(taskName,startHour,endHour);

                startHourField.setText("");
                endHourField.setText("");
                descriptionField.setText("");
                JOptionPane.showMessageDialog(this, "The task has been created successfully!");
            }
            else
                JOptionPane.showMessageDialog(this, "Please provide valid task details!");
        }
        else if ("Complex Task".equals(selectedTaskType)) {
            String taskName = complexTaskNameField.getText();
            ArrayList<Task> selectedTasks = getSelectedTasksFromList();

            if(tasksManagement.isComplexTaskValid(taskName, selectedTasks)){
                tasksManagement.createTask(taskName, selectedTasks);
                selectedTasksModel.clear();
                complexTaskNameField.setText("");

                updateTaskComboBox();

                JOptionPane.showMessageDialog(this, "The task has been created successfully!");
            }
            else
                JOptionPane.showMessageDialog(this, "Please provide a valid name and non-empty Sub-Task List!");
        }

    }

    private ArrayList<Task> getSelectedTasksFromList() {
        ArrayList<Task> selectedTasks = new ArrayList<>();
        for (int i = 0; i < selectedTasksModel.size(); i++) {
            selectedTasks.add(selectedTasksModel.getElementAt(i));
        }
        return selectedTasks;
    }

    protected void updateTaskComboBox() {
        if(taskComboBox == null)
            return;

        ArrayList<Task> availableTasks = tasksManagement.getUnassignedTasks();

        DefaultComboBoxModel<Task> taskComboBoxModel = (DefaultComboBoxModel<Task>) taskComboBox.getModel();
        taskComboBoxModel.removeAllElements();

        for (Task task : availableTasks) {
            if (!selectedTasksModel.contains(task)) {
                taskComboBoxModel.addElement(task);
            }
        }
    }
}
