package org.example;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;

public class EmployeeTasksWindow extends JFrame {
    private JTree taskTree;
    private JButton completeTaskButton;
    private JButton removeTaskButton;
    private DefaultTreeModel treeModel;
    private TasksManagement tasksManagement;
    private EmployeeTablePanel employeeTablePanel;
    private Employee selectedEmployee;

    public EmployeeTasksWindow(int employeeId, TasksManagement tasksManagement, EmployeeTablePanel employeeTablePanel) {
        this.tasksManagement = tasksManagement;
        this.employeeTablePanel = employeeTablePanel;
        this.setTitle("Employee Tasks");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        selectedEmployee = tasksManagement.findEmployeeById(employeeId);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tasks");

        for (Task task : tasksManagement.getMap().get(selectedEmployee)) {
            addTaskToTree(root, task);
        }

        treeModel = new DefaultTreeModel(root);
        taskTree = new JTree(treeModel);
        taskTree.setCellRenderer(new TaskTreeCellRenderer());
        this.add(new JScrollPane(taskTree), BorderLayout.CENTER);

        taskTree.addTreeSelectionListener(event -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) taskTree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.getLevel() != 0) {
                removeTaskButton.setEnabled(true);
                if(selectedNode.getUserObject() instanceof SimpleTask simpleTask)
                    completeTaskButton.setEnabled(!simpleTask.isCompleted());
                else
                    completeTaskButton.setEnabled(false);
            }
            else {
                completeTaskButton.setEnabled(false);
                removeTaskButton.setEnabled(false);
            }
        });

        completeTaskButton = new JButton("Complete Task");
        App.configureButton(completeTaskButton);
        completeTaskButton.setEnabled(false);
        completeTaskButton.addActionListener(e -> markSelectedTaskAsCompleted());

        removeTaskButton = new JButton("Remove Task");
        App.configureButton(removeTaskButton);
        removeTaskButton.setEnabled(false);
        removeTaskButton.addActionListener(e -> removeSelectedTask());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(completeTaskButton);
        buttonPanel.add(removeTaskButton);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void markSelectedTaskAsCompleted() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) taskTree.getLastSelectedPathComponent();

        if (selectedNode != null && selectedNode.getUserObject() instanceof SimpleTask selectedTask) {
            tasksManagement.modifyTaskStatus(selectedTask);

            treeModel.nodeChanged(selectedNode);

            taskTree.revalidate();
            taskTree.repaint();

            ArrayList<Employee> employees = new ArrayList<>(tasksManagement.getMap().keySet());
            employeeTablePanel.loadEmployeeData(employees);

            if(selectedTask.isCompleted())
                completeTaskButton.setEnabled(false);
        }
    }

    private void addTaskToTree(DefaultMutableTreeNode parent, Task task) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(task);
        parent.add(node);

        if (task instanceof ComplexTask) {
            for (Task subTask : ((ComplexTask) task).getIncludedTasks()) {
                addTaskToTree(node, subTask);
            }
        }
    }

    private void removeSelectedTask() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) taskTree.getLastSelectedPathComponent();

        if (selectedNode == null || !(selectedNode.getUserObject() instanceof Task selectedTask)) {
            return;
        }

        tasksManagement.removeTask(selectedTask);

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Tasks");

        for (Task task : tasksManagement.getMap().get(selectedEmployee)) {
            addTaskToTree(rootNode, task);
        }

        treeModel.setRoot(rootNode);
        treeModel.reload();

        taskTree.revalidate();
        taskTree.repaint();

        ArrayList<Employee> employees = new ArrayList<>(tasksManagement.getMap().keySet());
        employeeTablePanel.loadEmployeeData(employees);
    }
}