package org.example.Presentation;

import org.example.Business.Controller;
import org.example.Data_Models.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class EmployeeTablePanel extends JPanel{
    private Controller controller;
    DefaultTableModel defaultTableModel;
    private boolean employeesFiltered = false;

    public EmployeeTablePanel(MainFrame parentFrame, Controller controller){
        this.controller = controller;
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
        employeeTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), employeeTable, controller, this));

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        employeeTable.setFillsViewportHeight(true);
        this.add(scrollPane);

        ArrayList<Employee> employees = new ArrayList<>(controller.getEmployeeTaskMap().keySet());
        loadEmployeeData(employees);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton backButton = new JButton("Back to Main Panel");
        AppUtility.configureButton(backButton);
        backButton.addActionListener(e -> parentFrame.switchToMainPanel());
        buttonPanel.add(backButton);

        JButton filterButton = new JButton("Filter > 40 Hours");
        AppUtility.configureButton(filterButton);
        filterButton.addActionListener(e -> {
            if(!employeesFiltered) {
                loadEmployeeData(new ArrayList<>(controller.filterEmployees()));
                filterButton.setText("Unfilter Employees");
                employeesFiltered = true;
            }
            else
            {
                loadEmployeeData( new ArrayList<>(controller.getEmployeeTaskMap().keySet()));
                filterButton.setText("Filter > 40 Hours");
                employeesFiltered = false;
            }
        });
        buttonPanel.add(filterButton);

        JButton removeEmployeeButton = new JButton("Remove Employee");
        AppUtility.configureButton(removeEmployeeButton);
        removeEmployeeButton.setEnabled(false);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (employeeTable.getSelectedRow() == -1) {
                removeEmployeeButton.setEnabled(false);
            } else {
                removeEmployeeButton.setEnabled(true);
            }
        });

        removeEmployeeButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();

            int employeeId = (int) defaultTableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this employee?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.removeEmployee(employeeId);
                defaultTableModel.removeRow(selectedRow);
            }
        });

        buttonPanel.add(removeEmployeeButton);

        this.add(buttonPanel);
    }

    void loadEmployeeData(ArrayList<Employee> employees) {
        defaultTableModel.setRowCount(0);
        Map<String, Map<String,Integer>> taskCountMap = controller.countTasks();

        for (Employee e : employees) {
            int workedHours = controller.calculateEmployeeWorkDuration(e);
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
