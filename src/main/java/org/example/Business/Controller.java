package org.example.Business;

import org.example.DataModels.Employee;
import org.example.DataModels.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private TasksManagement tasksManagement;

    public Controller(TasksManagement tasksManagement){
        this.tasksManagement = tasksManagement;
    }

    public boolean isEmployeeValid(String employeeName) {
        return Utility.isEmployeeValid(employeeName);
    }

    public boolean isSimpleTaskValid(String taskName, String startHourText, String endHourText){
        return Utility.isSimpleTaskValid(taskName,startHourText,endHourText);
    }

    public boolean isComplexTaskValid(String taskName, ArrayList<Task> tasks){
        return Utility.isComplexTaskValid(taskName,tasks);
    }

    public void addEmployee(String employeeName) {
        tasksManagement.addEmployee(employeeName);
    }

    public void removeEmployee(int idEmployee) {
        tasksManagement.removeEmployee(idEmployee);
    }

    public void createSimpleTask(String taskName, int startHour, int endHour) {
        tasksManagement.createTask(taskName,startHour,endHour);
    }

    public void createComplexTask(String taskName, ArrayList<Task> tasks){
        tasksManagement.createTask(taskName,tasks);
    }

    public void removeTask(Task task){
        tasksManagement.removeTask(task);
    }

    public void assignTaskToEmployee(Employee employee, Task task){
        tasksManagement.assignTaskToEmployee(employee,task);
    }

    public int calculateEmployeeWorkDuration(Employee employee){
        return tasksManagement.calculateEmployeeWorkDuration(employee);
    }

    public ArrayList<Employee> filterEmployees() {
        return Utility.filterEmployees(tasksManagement);
    }

    public Map<String, Map<String,Integer>> countTasks(){
        return Utility.countTasks(tasksManagement);
    }

    public void modifyTaskStatus(Task task){
        tasksManagement.modifyTaskStatus(task);
    }

    public Employee findEmployeeById(int employeeId){
        return tasksManagement.findEmployeeById(employeeId);
    }

    public HashMap<Employee, ArrayList<Task>> getEmployeeTaskMap(){
        return tasksManagement.getMap();
    }

    public ArrayList<Task> getUnassignedTasks(){
        return tasksManagement.getUnassignedTasks();
    }
}
