package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class TasksManagement {
    private HashMap<Employee, ArrayList<Task>> map;
    private ArrayList<Task> unassignedTasks;

    public TasksManagement(){
        this.map = new HashMap<>();
        unassignedTasks = new ArrayList<>();
    }

    public void assignTaskToEmployee(Employee employee, Task task){
        for(Employee e : map.keySet())
            if (e.equals(employee)) {
                map.get(e).add(task);
                unassignedTasks.remove(task);
            }
    }

    public int calculateEmployeeWorkDuration(Employee employee){
        int duration = 0;
        for(Task t : map.get(employee))
            duration = duration + t.estimateDuration();
        return duration;
    }

    public void modifyTaskStatus(Task task){
        task.completeTask();
    }

    public Employee findEmployeeById(int employeeId) {
        for(Employee e : map.keySet()){
            if(e.getIdEmployee() == employeeId)
                return e;
        }
        return null;
    }

    public void createTask(String taskName, int startHour, int endHour) {
        // creates a SimpleTask
        SimpleTask newTask = new SimpleTask(taskName, startHour, endHour);
        unassignedTasks.add(newTask);
    }

    public void createTask(String taskName, ArrayList<Task> includedTasks){
        // creates a ComplexTask
        ComplexTask newTask = new ComplexTask(taskName, includedTasks);

        // removes the simple sub-tasks of the complex task from unassignedTasks and sets their parentTask to the new complex task
        for(Task t : includedTasks){
            unassignedTasks.remove(t);
            t.setParentTask(newTask);
        }
        unassignedTasks.add(newTask);
    }

    public void removeTask(Task deletedTask){
        if(!deletedTask.isCompleted())
            unassignedTasks.add(deletedTask);

        ComplexTask parentTask = (ComplexTask) deletedTask.getParentTask();

        if(parentTask != null) {
            parentTask.getIncludedTasks().remove(deletedTask);

            if (parentTask.getIncludedTasks().isEmpty()) {
                removeTask(parentTask);
            }
        }
        else {
            for (ArrayList<Task> tasks : map.values()) {
                if (tasks.contains(deletedTask)) {
                    tasks.remove(deletedTask);
                    break;
                }
            }
        }

        if(deletedTask instanceof ComplexTask complexTask) {
            if (complexTask.getIncludedTasks().isEmpty()) {
                unassignedTasks.remove(deletedTask);
            }
        }
    }

    public void addEmployee(String name){
        map.put(new Employee(name),new ArrayList<>());
    }

    public boolean isEmployeeValid(String employeeName){
        if(employeeName.isBlank())
            return false;
        return true;
    }

    public boolean isSimpleTaskValid(String taskName, String startHourText, String endHourText){
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

    public boolean isComplexTaskValid(String taskName, ArrayList<Task> includedTasks){
        if(taskName.isBlank() || includedTasks.isEmpty())
            return false;

        return true;
    }

    public ArrayList<Task> getUnassignedTasks() {
        return unassignedTasks;
    }

    public HashMap<Employee, ArrayList<Task>> getMap(){
        return map;
    }
}
