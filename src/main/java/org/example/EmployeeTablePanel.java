package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

// remove after adding controller class
import static org.example.Utility.filterEmployees;
import static org.example.Utility.countTasks;

public class EmployeeTablePanel extends JPanel{
    TasksManagement tasksManagement;
    DefaultTableModel defaultTableModel;
    private boolean employeesFiltered = false;

    public EmployeeTablePanel(MainFrame parentFrame, TasksManagement tasksManagement){
        this.tasksManagement = tasksManagement;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        defaultTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        defaultTableModel.addColumn("UID");
        defaultTableModel.addColumn("Employee Name");
        defaultTableModel.addColumn("Worked Hours");
        defaultTableModel.addColumn("Completed/Uncompleted");
        defaultTableModel.addColumn("Tasks");

        JTable employeeTable = new JTable(defaultTableModel);
        employeeTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        employeeTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), employeeTable, tasksManagement, this));

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        employeeTable.setFillsViewportHeight(true);
        this.add(scrollPane);

        ArrayList<Employee> employees = new ArrayList<>(tasksManagement.getMap().keySet());
        loadEmployeeData(employees);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton backButton = new JButton("Back to Main Panel");
        App.configureButton(backButton);
        backButton.addActionListener(e -> parentFrame.switchToMainPanel());
        buttonPanel.add(backButton);

        JButton filterButton = new JButton("Filter > 40 Hours");
        App.configureButton(filterButton);
        filterButton.addActionListener(e -> {
            if(!employeesFiltered) {
                loadEmployeeData(new ArrayList<>(filterEmployees()));
                filterButton.setText("Unfiltered Employees");
                employeesFiltered = true;
            }
            else
            {
                loadEmployeeData( new ArrayList<>(tasksManagement.getMap().keySet()));
                filterButton.setText("Filter > 40 Hours");
                employeesFiltered = false;
            }
        });
        buttonPanel.add(filterButton);

        this.add(buttonPanel);
    }

    void loadEmployeeData(ArrayList<Employee> employees) {
        defaultTableModel.setRowCount(0);
        Map<String, Map<String,Integer>> taskCountMap = Utility.countTasks();

        for (Employee e : employees) {
            int workedHours = tasksManagement.calculateEmployeeWorkDuration(e);
            Map<String,Integer> employeeTaskCountMap = taskCountMap.get(e.getName());

            defaultTableModel.addRow(new Object[]{
                    e.getIdEmployee(),
                    e.getName(),
                    workedHours,
                    employeeTaskCountMap.get("Completed") + "," + employeeTaskCountMap.get("Uncompleted"),
                    "Tasks"
            });
        }

        defaultTableModel.fireTableDataChanged();
    }
}
