package org.example.Business;

import org.example.Data_Models.ComplexTask;
import org.example.Data_Models.Employee;
import org.example.Data_Models.SimpleTask;
import org.example.Data_Models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utility {
    public static ArrayList<Employee> filterEmployees(TasksManagement tasksManagement) {
        ArrayList<Employee> employees = new ArrayList<>();
        for(Employee e : tasksManagement.getMap().keySet())
            if(tasksManagement.calculateEmployeeWorkDuration(e) > 40)
                employees.add(e);
        /// lambda expression for compare method in interface Comparator for sorting the employees
        employees.sort((o1, o2) -> Integer.compare(tasksManagement.calculateEmployeeWorkDuration(o2), tasksManagement.calculateEmployeeWorkDuration(o1)));
        return employees;
    }

    public static Map<String, Map<String,Integer>> countTasks(TasksManagement tasksManagement) {
        Map<String, Map<String,Integer>> employeeTasksCount = new HashMap<>();

        for(Map.Entry<Employee, ArrayList<Task>> entry : tasksManagement.getMap().entrySet()){
            Employee employee = entry.getKey();
            ArrayList<Task> tasks = entry.getValue();

            Map<String,Integer> taskCount = new HashMap<>();
            taskCount.put("Completed",0);
            taskCount.put("Uncompleted",0);

            for (Task task : tasks) {
                countTasksRecursive(task, taskCount);
            }

            employeeTasksCount.put(employee.getName(), taskCount);
        }
        return employeeTasksCount;
    }

    private static void countTasksRecursive(Task task, Map<String,Integer> taskCount){
        if(task instanceof SimpleTask){
            if(task.isCompleted())
                taskCount.put("Completed", taskCount.get("Completed") + 1);
            else
                taskCount.put("Uncompleted", taskCount.get("Uncompleted") + 1);
        }
        else {
            if(task instanceof ComplexTask complexTask)
                for(Task subTask : complexTask.getIncludedTasks())
                    countTasksRecursive(subTask, taskCount);
        }
    }

    public static boolean isEmployeeValid(String employeeName){
        if(employeeName.isBlank())
            return false;
        return true;
    }

    public static boolean isSimpleTaskValid(String taskName, String startHourText, String endHourText){
        if(taskName.isBlank() || startHourText.isBlank() || endHourText.isBlank())
            return false;

        try{
            int startHour = Integer.parseInt(startHourText);
            int endHour = Integer.parseInt(endHourText);

            if(startHour < 0 || startHour > 24 || endHour < 0 || endHour > 24 || startHour >= endHour)
                return false;

        }
        catch(NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static boolean isComplexTaskValid(String taskName, ArrayList<Task> includedTasks){
        if(taskName.isBlank() || includedTasks.isEmpty())
            return false;

        return true;
    }
}
