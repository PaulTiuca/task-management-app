package org.example.Data_Models;

import java.io.Serializable;
import java.util.ArrayList;

public final class ComplexTask extends Task implements Serializable  {
    private ArrayList<Task> includedTasks;

    public ComplexTask(String taskName, ArrayList<Task> includedTasks){
        super(taskName);
        this.includedTasks = includedTasks;
    }

    public int estimateDuration() {
        int duration = 0;
        for(Task task : includedTasks)
            duration = duration + task.estimateDuration();
        return duration;
    }

    public boolean isCompleted(){
        for(Task t : includedTasks) {
            if(!t.isCompleted())
                return false;
        }
        return true;
    }

    public void completeTask(){
        if(this.isCompleted()) {
            this.setCompleted();
            if(this.getParentTask() instanceof ComplexTask complexTask)
                complexTask.completeTask();
        }
    }

    public void deleteTask(Task task){
        includedTasks.remove(task);
    }

    public ArrayList<Task> getIncludedTasks() {
        return includedTasks;
    }

    @Override
    public String toString() {
        return this.getTaskName() + " (C)";
    }
}
