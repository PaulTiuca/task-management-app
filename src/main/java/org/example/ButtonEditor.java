package org.example;

import javax.swing.*;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private JTable table;
    private boolean isClicked;
    private TasksManagement tasksManagement;
    private EmployeeTablePanel employeeTablePanel;

    public ButtonEditor(JCheckBox checkBox, JTable table, TasksManagement tasksManagement, EmployeeTablePanel employeeTablePanel) {
        super(checkBox);
        this.table = table;
        this.tasksManagement = tasksManagement;
        this.employeeTablePanel = employeeTablePanel;
        button = new JButton();
        button.setOpaque(true);
        button.setFocusable(false);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText("View Tasks");
        isClicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if(isClicked) {
            int selectedRow = table.getSelectedRow();
            if(selectedRow != -1) {
                int employeeId = (int) table.getValueAt(selectedRow,0);

                new EmployeeTasksWindow(employeeId,tasksManagement,employeeTablePanel);
            }
        }
        isClicked = false;
        return "View Tasks";
    }

    @Override
    public boolean stopCellEditing() {
        isClicked = false;
        return super.stopCellEditing();
    }
}
