package org.example.Business;

import org.example.DataModels.ComplexTask;
import org.example.DataModels.Employee;
import org.example.DataModels.SimpleTask;
import org.example.DataModels.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TasksManagement implements Serializable {
    private static final long serialVersionUID = 1L;

    private HashMap<Employee, ArrayList<Task>> map;
    private ArrayList<Task> unassignedTasks;
    private int lastEmployeeId;

    public TasksManagement(){
        this.map = new HashMap<>();
        unassignedTasks = new ArrayList<>();
        lastEmployeeId = 1;
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
            parentTask.deleteTask(deletedTask);

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
        lastEmployeeId++;
    }

    public void removeEmployee(int employeeId){
        Employee deletedEmployee = findEmployeeById(employeeId);

        ArrayList<Task> tasks = map.get(deletedEmployee);
        if(tasks != null){
            for(Task task : tasks)
                removeTask(task);
        }

        map.remove(deletedEmployee);
    }

    public void setStartingEmployeeId(){
        Employee.setIdGenerator(lastEmployeeId);
    }

    public ArrayList<Task> getUnassignedTasks() {
        return unassignedTasks;
    }

    public HashMap<Employee, ArrayList<Task>> getMap(){
        return map;
    }
}
